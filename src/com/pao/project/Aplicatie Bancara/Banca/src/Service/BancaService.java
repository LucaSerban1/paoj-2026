package Service;

import Model.*;
import Model.Cont.*;
import Exceptii.FonduriInsuficienteException;
import Model.Card.*;
import Model.Tranzactie.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class BancaService {
    private final TreeMap<String, Client> clienti;
    private final Map<String, Cont> conturi;
    private final Map<String, List<Tranzactie>> istoricTranzactii;

    private static BancaService instance;

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
    }

    public void stergeClient(String cnp) {
        Client client = clienti.remove(cnp);
        if (client != null) {
            System.out.println("Clientul " + client.getNume() + " a fost sters.");
        } else {
            System.out.println("Clientul cu CNP " + cnp + " nu a fost gasit.");
        }
    }

    public Client cautaClient(String cnp) {
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
    }

    public void retrage(String iban, double suma) {
        Cont cont = conturi.get(iban);
        if (cont == null) {
            System.out.println("Contul " + iban + " nu a fost gasit.");
            return;
        }
        try {
            cont.retrage(suma);
            inregistreazaTranzactie(iban, suma, TipTranzactie.RETRAGERE, "Retragere numerar");
            System.out.println("Retragere reusita. Sold curent: " + cont.getSold() + " RON");
        } catch (FonduriInsuficienteException e) {
            System.out.println("Eroare retragere: " + e.getMessage());
        }
    }

    public void transfer(String ibanSursa, String ibanDest, double suma) {
        Cont sursa = conturi.get(ibanSursa);
        Cont dest  = conturi.get(ibanDest);

        if (sursa == null || dest == null) {
            System.out.println("Unul dintre conturi este invalid.");
            return;
        }
        if (!valideazaSold(sursa, suma)) {
            System.out.println("Fonduri insuficiente pentru transfer.");
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
    }

    public void genereazaExtras(String iban, LocalDate start, LocalDate end) {
        ExtrasDecont extras = new ExtrasDecont(iban, start, end);
        List<Tranzactie> lista = istoricTranzactii.getOrDefault(iban, new ArrayList<>());
        for (Tranzactie t : lista) {
            extras.adaugaTranzactie(t);
        }
        extras.afiseazaExtras();
    }

    public void aboneazaServiciu(String iban, ServiciuBancar serviciu) {
        if (conturi.containsKey(iban)) {
            System.out.println("Contul " + iban + " abonat la " + serviciu.getTip()
                    + " | Cost lunar: " + serviciu.getCostLunar() + " RON"
                    + " | Activ: " + serviciu.esteActiv());
        } else {
            System.out.println("Contul " + iban + " nu exista.");
        }
    }

    public List<Tranzactie> sorteazaTranzactii(String iban) {
        List<Tranzactie> lista = new ArrayList<>(
                istoricTranzactii.getOrDefault(iban, new ArrayList<>())
        );
        Collections.sort(lista);
        return lista;
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
        String id = "TRX" + System.currentTimeMillis();
        Tranzactie t = new Tranzactie(id, suma, tip, LocalDateTime.now(), descriere);
        istoricTranzactii.get(iban).add(t);
    }
}