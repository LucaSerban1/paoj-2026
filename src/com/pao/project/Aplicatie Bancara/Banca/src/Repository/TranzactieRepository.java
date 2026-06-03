package Repository;

import Model.Tranzactie.Tranzactie;
import Model.Tranzactie.TipTranzactie;
import Util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TranzactieRepository implements Repository<Tranzactie, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Tranzactie t) {
        if (t.getIban() == null) {
            throw new IllegalArgumentException("Tranzactia nu are IBAN setat. Folositi setIban() inainte de save.");
        }
        String sql =
            "INSERT INTO tranzactii (id, iban, suma, tip_tranzactie, data_tranzactie, descriere) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, t.getId());
            ps.setString(2, t.getIban());
            ps.setDouble(3, t.getSuma());
            ps.setString(4, t.getTip().name());
            ps.setTimestamp(5, Timestamp.valueOf(t.getData()));
            ps.setString(6, t.getDescriere());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea tranzactiei: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Tranzactie> findById(String id) {
        String sql =
            "SELECT id, iban, suma, tip_tranzactie, data_tranzactie, descriere " +
            "FROM tranzactii WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea tranzactiei: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Tranzactie> findAll() {
        String sql =
            "SELECT id, iban, suma, tip_tranzactie, data_tranzactie, descriere " +
            "FROM tranzactii ORDER BY data_tranzactie DESC";
        List<Tranzactie> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea tranzactiilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void update(Tranzactie t) {
        String sql = "UPDATE tranzactii SET descriere = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, t.getDescriere());
            ps.setString(2, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea tranzactiei: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tranzactii WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea tranzactiei: " + e.getMessage(), e);
        }
    }

    public List<Tranzactie> findByIban(String iban) {
        String sql =
            "SELECT id, iban, suma, tip_tranzactie, data_tranzactie, descriere " +
            "FROM tranzactii WHERE iban = ? ORDER BY data_tranzactie DESC";
        List<Tranzactie> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea tranzactiilor dupa IBAN: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<String> findTranzactiiCuInfoClient() {
        String sql =
            "SELECT t.id, t.suma, t.tip_tranzactie, t.data_tranzactie, t.descriere, " +
            "cl.nume AS nume_client, t.iban " +
            "FROM tranzactii t " +
            "JOIN conturi co ON t.iban = co.iban " +
            "JOIN clienti cl ON co.cnp_client = cl.cnp " +
            "ORDER BY t.data_tranzactie DESC";
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("[%s] %s | %.2f RON | %s | Client: %s | IBAN: %s",
                        rs.getString("id"),
                        rs.getString("tip_tranzactie"),
                        rs.getDouble("suma"),
                        rs.getTimestamp("data_tranzactie").toLocalDateTime(),
                        rs.getString("nume_client"),
                        rs.getString("iban")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea tranzactiilor cu info client: " + e.getMessage(), e);
        }
        return rezultate;
    }

    private Tranzactie mapRow(ResultSet rs) throws SQLException {
        Tranzactie t = new Tranzactie(
                rs.getString("id"),
                rs.getDouble("suma"),
                TipTranzactie.valueOf(rs.getString("tip_tranzactie")),
                rs.getTimestamp("data_tranzactie").toLocalDateTime(),
                rs.getString("descriere")
        );
        t.setIban(rs.getString("iban"));
        return t;
    }
}
