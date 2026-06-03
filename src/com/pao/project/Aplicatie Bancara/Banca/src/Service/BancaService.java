package Service;

import Model.Cont.Cont;
import Model.Cont.ContEconomii;
import Model.Cont.ContCurent;
import Exceptii.FonduriInsuficienteException;
import Model.Client;
import Model.Tranzactie.TipTranzactie;
import Model.Tranzactie.Tranzactie;
import Model.ExtrasDecont;
import Model.ServiciuBancar;
import Util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.UUID;

public class BancaService {
    private final TreeMap<String, Client> clienti;
    private final Map<String, Cont> conturi;
    private final Map<String, List<Tranzactie>> istoricTranzactii;

    private static BancaService instance;
    private final AuditService audit = AuditService.getInstance();

    private BancaService() {
        this.clienti = new TreeMap<>();
        this.conturi = new HashMap<>();
        this.istoricTranzactii = new HashMap<>();
    }

    public static BancaService getInstance() {
        if (instance == null) {
            instance = new BancaService();
        }
        return instance;
    }

    public void adaugaClient(Client client) {
        if (client != null && !clienti.containsKey(client.getCnp())) {
            clienti.put(client.getCnp(), client);
            System.out.println("Clientul " + client.getNume() + " a fost adaugat.");
        } else {
            System.out.println("Eroare: clientul exista deja sau este invalid.");
        }
        audit.log("adauga_client");
    }

    public void stergeClient(String cnp) {
        Client client = clienti.remove(cnp);
        if (client != null) {
            System.out.println("Clientul " + client.getNume() + " a fost sters.");
        } else {
            System.out.println("Clientul cu CNP " + cnp + " nu a fost gasit.");
        }
        audit.log("sterge_client");
    }

    public Client cautaClient(String cnp) {
        audit.log("cauta_client");
        return clienti.get(cnp);
    }

    public List<Client> listeazaTotiClientii() {
        return new ArrayList<>(clienti.values());
    }

    public void deschideCont(String cnpClient, String tipCont) {
        Client client = cautaClient(cnpClient);
        if (client == null) {
            System.out.println("Eroare: clientul cu CNP " + cnpClient + " nu a fost gasit.");
            return;
        }

        String iban = genereazaIban();
        Cont contNou;

        if (tipCont.equalsIgnoreCase("CURENT")) {
            contNou = new ContCurent(iban, 0.0, LocalDate.now(), cnpClient, 1000.0, 15.0);
        } else if (tipCont.equalsIgnoreCase("ECONOMII")) {
            contNou = new ContEconomii(iban, 0.0, LocalDate.now(), cnpClient, 0.05, 6);
        } else {
            System.out.println("Tip de cont necunoscut. Folositi 'CURENT' sau 'ECONOMII'.");
            return;
        }

        conturi.put(iban, contNou);
        client.adaugaCont(contNou);
        istoricTranzactii.put(iban, new ArrayList<>());
        System.out.println("Cont " + tipCont + " deschis. IBAN: " + iban);
        audit.log("deschide_cont");
    }

    public List<Cont> listeazaConturi(String cnpClient) {
        Client client = cautaClient(cnpClient);
        if (client != null) return client.getConturi();
        return Collections.emptyList();
    }

    public double getSoldDisponibil(String iban) {
        Cont cont = conturi.get(iban);
        if (cont != null) return cont.getSold();
        System.out.println("Contul " + iban + " nu a fost gasit.");
        return 0.0;
    }

    public void depune(String iban, double suma) {
        Cont cont = conturi.get(iban);
        if (cont != null && suma > 0) {
            cont.depune(suma);
            inregistreazaTranzactie(iban, suma, TipTranzactie.DEPUNERE, "Depunere numerar");
            System.out.println("Depunere reusita. Sold curent: " + cont.getSold() + " RON");
        } else {
            System.out.println("Cont invalid sau suma incorecta.");
        }
        audit.log("depune");
    }

    public void retrage(String iban, double suma) {
        Cont cont = conturi.get(iban);
        if (cont == null) {
            System.out.println("Contul " + iban + " nu a fost gasit.");
            audit.log("retrage");
            return;
        }
        try {
            cont.retrage(suma);
            inregistreazaTranzactie(iban, suma, TipTranzactie.RETRAGERE, "Retragere numerar");
            System.out.println("Retragere reusita. Sold curent: " + cont.getSold() + " RON");
        } catch (FonduriInsuficienteException e) {
            System.out.println("Eroare retragere: " + e.getMessage());
        }
        audit.log("retrage");
    }

    public void transfer(String ibanSursa, String ibanDest, double suma) {
        Cont sursa = conturi.get(ibanSursa);
        Cont dest  = conturi.get(ibanDest);

        if (sursa == null || dest == null) {
            System.out.println("Unul dintre conturi este invalid.");
            audit.log("transfer");
            return;
        }
        if (!valideazaSold(sursa, suma)) {
            System.out.println("Fonduri insuficiente pentru transfer.");
            audit.log("transfer");
            return;
        }
        try {
            sursa.retrage(suma);
            dest.depune(suma);
            inregistreazaTranzactie(ibanSursa, suma, TipTranzactie.TRANSFER, "Transfer catre " + ibanDest);
            inregistreazaTranzactie(ibanDest,  suma, TipTranzactie.TRANSFER, "Incasare de la " + ibanSursa);
            System.out.println("Transfer de " + suma + " RON realizat cu succes.");
        } catch (FonduriInsuficienteException e) {
            System.out.println("Transfer esuat: " + e.getMessage());
        }
        audit.log("transfer");
    }

    public void executaTransferPersistent(String ibanSursa, String ibanDest, double suma) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE conturi SET sold = sold - ? WHERE iban = ?")) {
                    ps.setDouble(1, suma);
                    ps.setString(2, ibanSursa);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE conturi SET sold = sold + ? WHERE iban = ?")) {
                    ps.setDouble(1, suma);
                    ps.setString(2, ibanDest);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO tranzactii (id, iban, suma, tip_tranzactie, data_tranzactie, descriere) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")) {
                    ps.setString(1, "TRX" + UUID.randomUUID());
                    ps.setString(2, ibanSursa);
                    ps.setDouble(3, suma);
                    ps.setString(4, TipTranzactie.TRANSFER.name());
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(6, "Transfer catre " + ibanDest);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO tranzactii (id, iban, suma, tip_tranzactie, data_tranzactie, descriere) " +
                        "VALUES (?, ?, ?, ?, ?, ?)")) {
                    ps.setString(1, "TRX" + UUID.randomUUID());
                    ps.setString(2, ibanDest);
                    ps.setDouble(3, suma);
                    ps.setString(4, TipTranzactie.TRANSFER.name());
                    ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(6, "Incasare de la " + ibanSursa);
                    ps.executeUpdate();
                }

                conn.commit();
                System.out.println("Transfer persistent de " + suma + " RON executat si comis cu succes.");
                audit.log("transfer_persistent");
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Eroare la transfer persistent, rollback efectuat: " + e.getMessage());
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la transfer persistent: " + e.getMessage(), e);
        }
    }

    public void genereazaExtras(String iban, LocalDate start, LocalDate end) {
        ExtrasDecont extras = new ExtrasDecont(iban, start, end);
        List<Tranzactie> lista = istoricTranzactii.getOrDefault(iban, new ArrayList<>());
        for (Tranzactie t : lista) {
            extras.adaugaTranzactie(t);
        }
        extras.afiseazaExtras();
        audit.log("genereaza_extras");
    }

    public void aboneazaServiciu(String iban, ServiciuBancar serviciu) {
        if (conturi.containsKey(iban)) {
            System.out.println("Contul " + iban + " abonat la " + serviciu.getTip()
                    + " | Cost lunar: " + serviciu.getCostLunar() + " RON"
                    + " | Activ: " + serviciu.esteActiv());
        } else {
            System.out.println("Contul " + iban + " nu exista.");
        }
        audit.log("aboneaza_serviciu");
    }

    public List<Tranzactie> sorteazaTranzactii(String iban) {
        List<Tranzactie> lista = new ArrayList<>(
                istoricTranzactii.getOrDefault(iban, new ArrayList<>())
        );
        Collections.sort(lista);
        audit.log("sorteaza_tranzactii");
        return lista;
    }

    public Map<String, Cont> getConturi() {
        return conturi;
    }

    public Map<String, List<Tranzactie>> getIstoricTranzactii() {
        return istoricTranzactii;
    }

    private boolean valideazaSold(Cont cont, double suma) {
        if (cont instanceof ContCurent) {
            ContCurent cc = (ContCurent) cont;
            return (cc.getSold() + cc.getLimitaDescoperit()) >= suma;
        }
        return cont.getSold() >= suma;
    }

    private String genereazaIban() {
        long nr = (long)(Math.random() * 10_000_000_000L);
        return "RO" + String.format("%02d", (int)(Math.random() * 99))
                + "BANC" + String.format("%010d", nr);
    }

    private void inregistreazaTranzactie(String iban, double suma,
                                         TipTranzactie tip, String descriere) {
        String id = "TRX" + UUID.randomUUID();
        Tranzactie t = new Tranzactie(id, suma, tip, LocalDateTime.now(), descriere);
        t.setIban(iban);
        istoricTranzactii.get(iban).add(t);
    }
}
