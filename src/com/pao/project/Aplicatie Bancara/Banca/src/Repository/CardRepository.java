package Repository;

import Model.Card.Card;
import Model.Card.CardCredit;
import Model.Card.CardDebit;
import Util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepository implements Repository<Card, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Card card) {
        String cnpClient = getCnpClientPentruIban(card.getIdCont());

        String sql =
            "INSERT INTO carduri (numar_card, iban, cnp_client, tip_card, data_expirare, cvv, activ, " +
            "limita_zilnica, contactless, limita_credit, sold_utilizat) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, card.getNumarCard());
            ps.setString(2, card.getIdCont());
            ps.setString(3, cnpClient);
            ps.setDate(5, Date.valueOf(card.getDataExpirare()));
            ps.setString(6, card.getCvv());
            ps.setBoolean(7, card.isActiv());

            if (card instanceof CardDebit) {
                CardDebit cd = (CardDebit) card;
                ps.setString(4, "DEBIT");
                ps.setDouble(8, cd.getLimitaZilnica());
                ps.setBoolean(9, cd.isContactless());
                ps.setNull(10, Types.DOUBLE);
                ps.setNull(11, Types.DOUBLE);
            } else {
                CardCredit cc = (CardCredit) card;
                ps.setString(4, "CREDIT");
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.BOOLEAN);
                ps.setDouble(10, cc.getLimitaCredit());
                ps.setDouble(11, cc.getSoldUtilizat());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea cardului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Card> findById(String numarCard) {
        String sql =
            "SELECT numar_card, iban, tip_card, data_expirare, cvv, activ, " +
            "limita_zilnica, contactless, limita_credit, sold_utilizat " +
            "FROM carduri WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, numarCard);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea cardului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        String sql =
            "SELECT numar_card, iban, tip_card, data_expirare, cvv, activ, " +
            "limita_zilnica, contactless, limita_credit, sold_utilizat " +
            "FROM carduri";
        List<Card> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea cardurilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void update(Card card) {
        String sql = "UPDATE carduri SET activ = ?, sold_utilizat = ? WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setBoolean(1, card.isActiv());
            if (card instanceof CardCredit) {
                ps.setDouble(2, ((CardCredit) card).getSoldUtilizat());
            } else {
                ps.setNull(2, Types.DOUBLE);
            }
            ps.setString(3, card.getNumarCard());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea cardului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String numarCard) {
        String sql = "DELETE FROM carduri WHERE numar_card = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, numarCard);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea cardului: " + e.getMessage(), e);
        }
    }

    // JOIN 3: carduri active cu informatii despre cont si client
    public List<String> findCarduriActiveCuInfoClient() {
        String sql =
            "SELECT ca.numar_card, ca.tip_card, ca.data_expirare, " +
            "cl.nume AS nume_client, co.iban, co.tip_cont " +
            "FROM carduri ca " +
            "JOIN conturi co ON ca.iban = co.iban " +
            "JOIN clienti cl ON ca.cnp_client = cl.cnp " +
            "WHERE ca.activ = TRUE " +
            "ORDER BY cl.nume";
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("Card: %s | Tip: %s | Expira: %s | Client: %s | Cont: %s (%s)",
                        rs.getString("numar_card"),
                        rs.getString("tip_card"),
                        rs.getDate("data_expirare"),
                        rs.getString("nume_client"),
                        rs.getString("iban"),
                        rs.getString("tip_cont")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea cardurilor active: " + e.getMessage(), e);
        }
        return rezultate;
    }

    private String getCnpClientPentruIban(String iban) {
        String sql = "SELECT cnp_client FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("cnp_client");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la determinarea clientului pentru IBAN: " + e.getMessage(), e);
        }
        throw new RuntimeException("Nu exista cont cu IBAN: " + iban);
    }

    private Card mapRow(ResultSet rs) throws SQLException {
        String numarCard  = rs.getString("numar_card");
        String iban       = rs.getString("iban");
        String tipCard    = rs.getString("tip_card");
        LocalDate expirare = rs.getDate("data_expirare").toLocalDate();
        String cvv        = rs.getString("cvv");
        boolean activ     = rs.getBoolean("activ");

        Card card;
        if ("DEBIT".equals(tipCard)) {
            card = new CardDebit(numarCard, expirare, cvv, iban,
                    rs.getDouble("limita_zilnica"),
                    rs.getBoolean("contactless"));
        } else {
            card = new CardCredit(numarCard, expirare, cvv, iban,
                    rs.getDouble("limita_credit"));
        }
        card.setActiv(activ);
        return card;
    }
}
