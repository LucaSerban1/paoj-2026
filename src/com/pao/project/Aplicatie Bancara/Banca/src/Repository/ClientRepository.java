package Repository;

import Model.Client;
import Util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements Repository<Client, String> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Client client) {
        String sql = "INSERT INTO clienti (cnp, nume, email) VALUES (?, ?, ?) ON CONFLICT (cnp) DO NOTHING";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getCnp());
            ps.setString(2, client.getNume());
            ps.setString(3, client.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la salvarea clientului: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> findById(String cnp) {
        String sql = "SELECT cnp, nume, email FROM clienti WHERE cnp = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la cautarea clientului: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String sql = "SELECT cnp, nume, email FROM clienti ORDER BY cnp";
        List<Client> lista = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la listarea clientilor: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE clienti SET nume = ?, email = ? WHERE cnp = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, client.getNume());
            ps.setString(2, client.getEmail());
            ps.setString(3, client.getCnp());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la actualizarea clientului: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(String cnp) {
        String sql = "DELETE FROM clienti WHERE cnp = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, cnp);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la stergerea clientului: " + e.getMessage(), e);
        }
    }

    public List<String> findTotiClientiiCuNrTranzactii() {
        String sql =
            "SELECT cl.cnp, cl.nume, COUNT(t.id) AS nr_tranzactii " +
            "FROM clienti cl " +
            "LEFT JOIN conturi co ON cl.cnp = co.cnp_client " +
            "LEFT JOIN tranzactii t ON co.iban = t.iban " +
            "GROUP BY cl.cnp, cl.nume " +
            "ORDER BY nr_tranzactii DESC";
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(String.format("CNP: %s | Nume: %s | Nr. tranzactii: %d",
                        rs.getString("cnp"),
                        rs.getString("nume"),
                        rs.getInt("nr_tranzactii")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la interogarea clientilor cu tranzactii: " + e.getMessage(), e);
        }
        return rezultate;
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(rs.getString("cnp"), rs.getString("nume"), rs.getString("email"));
    }
}
