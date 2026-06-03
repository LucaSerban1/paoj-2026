package Repository;

import Model.Cont.Cont;
import Model.Cont.ContCurent;
import Model.Cont.ContEconomii;
import Util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContRepository implements Repository<Cont, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Cont cont) {
        String sql =
            "INSERT INTO conturi (iban, cnp_client, tip_cont, sold, data_deschidere, " +
            "limita_descoperit, comision_lunar, rata_dobanda, perioada_minima) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (iban) DO NOTHING";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cont.getNumarCont());
            ps.setString(2, cont.getIdClient());
            ps.setDouble(4, cont.getSold());
            ps.setDate(5, Date.valueOf(cont.getDataDeschidere()));

            if (cont instanceof ContCurent) {
                ContCurent cc = (ContCurent) cont;
                ps.setString(3, "CURENT");
                ps.setDouble(6, cc.getLimitaDescoperit());
                ps.setDouble(7, cc.getComisionLunar());
                ps.setNull(8, Types.DOUBLE);
                ps.setNull(9, Types.INTEGER);
            } else {
                ContEconomii ce = (ContEconomii) cont;
                ps.setString(3, "ECONOMII");
                ps.setNull(6, Types.DOUBLE);
                ps.setNull(7, Types.DOUBLE);
                ps.setDouble(8, ce.getRataDobanda());
                ps.setInt(9, ce.getPerioadaMinima());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea contului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cont> findById(String iban) {
        String sql =
            "SELECT iban, cnp_client, tip_cont, sold, data_deschidere, " +
            "limita_descoperit, comision_lunar, rata_dobanda, perioada_minima " +
            "FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea contului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Cont> findAll() {
        String sql =
            "SELECT iban, cnp_client, tip_cont, sold, data_deschidere, " +
            "limita_descoperit, comision_lunar, rata_dobanda, perioada_minima " +
            "FROM conturi";
        List<Cont> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea conturilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void update(Cont cont) {
        String sql = "UPDATE conturi SET sold = ?, limita_descoperit = ?, rata_dobanda = ? WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDouble(1, cont.getSold());
            if (cont instanceof ContCurent) {
                ps.setDouble(2, ((ContCurent) cont).getLimitaDescoperit());
                ps.setNull(3, Types.DOUBLE);
            } else {
                ps.setNull(2, Types.DOUBLE);
                ps.setDouble(3, ((ContEconomii) cont).getRataDobanda());
            }
            ps.setString(4, cont.getNumarCont());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea contului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM conturi WHERE iban = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea contului: " + e.getMessage(), e);
        }
    }

    public List<String> findConturiCuInfoClient() {
        String sql =
            "SELECT co.iban, co.tip_cont, co.sold, cl.nume, COUNT(t.id) AS nr_tranzactii " +
            "FROM conturi co " +
            "JOIN clienti cl ON co.cnp_client = cl.cnp " +
            "LEFT JOIN tranzactii t ON co.iban = t.iban " +
            "GROUP BY co.iban, co.tip_cont, co.sold, cl.nume " +
            "ORDER BY co.sold DESC";
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("IBAN: %s | Tip: %s | Sold: %.2f RON | Client: %s | Tranzactii: %d",
                        rs.getString("iban"),
                        rs.getString("tip_cont"),
                        rs.getDouble("sold"),
                        rs.getString("nume"),
                        rs.getInt("nr_tranzactii")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea conturilor cu info client: " + e.getMessage(), e);
        }
        return rezultate;
    }

    private Cont mapRow(ResultSet rs) throws SQLException {
        String iban       = rs.getString("iban");
        String cnpClient  = rs.getString("cnp_client");
        String tipCont    = rs.getString("tip_cont");
        double sold       = rs.getDouble("sold");
        LocalDate dataDeschidere = rs.getDate("data_deschidere").toLocalDate();

        if ("CURENT".equals(tipCont)) {
            return new ContCurent(iban, sold, dataDeschidere, cnpClient,
                    rs.getDouble("limita_descoperit"),
                    rs.getDouble("comision_lunar"));
        } else {
            return new ContEconomii(iban, sold, dataDeschidere, cnpClient,
                    rs.getDouble("rata_dobanda"),
                    rs.getInt("perioada_minima"));
        }
    }
}
