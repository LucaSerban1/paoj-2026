import Model.Client;
import Model.Card.Card;
import Model.Cont.Cont;
import Model.ServiciuBancar;
import Model.TipServiciu;
import Model.Tranzactie.Tranzactie;
import Repository.CardRepository;
import Repository.ClientRepository;
import Repository.ContRepository;
import Repository.TranzactieRepository;
import Service.AuditService;
import Service.BancaService;
import Service.CardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        BancaService bancaService = BancaService.getInstance();
        CardService cardService   = CardService.getInstance();

        // =========================================================
        //  Etapa I — demonstratie in-memory (cu audit automat)
        // =========================================================

        System.out.println("1: Adaugare clienti");
        Client ion   = new Client("1900101123456", "Ion Popescu",     "ion@email.ro");
        Client maria = new Client("2850202654321", "Maria Ionescu",   "maria@email.ro");
        Client alex  = new Client("1991231987654", "Alex Dumitrescu", "alex@email.ro");

        bancaService.adaugaClient(ion);
        bancaService.adaugaClient(maria);
        bancaService.adaugaClient(alex);
        System.out.println();

        System.out.println("2: Deschidere conturi");
        bancaService.deschideCont(ion.getCnp(),   "CURENT");
        bancaService.deschideCont(ion.getCnp(),   "ECONOMII");
        bancaService.deschideCont(maria.getCnp(), "CURENT");
        bancaService.deschideCont(alex.getCnp(),  "CURENT");
        System.out.println();

        String ibanCurentIon   = bancaService.listeazaConturi(ion.getCnp()).get(0).getNumarCont();
        String ibanEconomiiIon = bancaService.listeazaConturi(ion.getCnp()).get(1).getNumarCont();
        String ibanCurentMaria = bancaService.listeazaConturi(maria.getCnp()).get(0).getNumarCont();
        String ibanCurentAlex  = bancaService.listeazaConturi(alex.getCnp()).get(0).getNumarCont();

        System.out.println("3: Depuneri");
        bancaService.depune(ibanCurentIon,   5000.0);
        bancaService.depune(ibanEconomiiIon, 10000.0);
        bancaService.depune(ibanCurentMaria, 3000.0);
        bancaService.depune(ibanCurentAlex,  1500.0);
        System.out.println();

        System.out.println("4: Retrageri");
        bancaService.retrage(ibanCurentIon, 1200.0);
        bancaService.retrage(ibanCurentIon, 99999.0);
        System.out.println();

        System.out.println("5: Transfer intre conturi");
        bancaService.transfer(ibanCurentIon, ibanCurentMaria, 500.0);
        System.out.println("  Sold Ion Curent:   " + bancaService.getSoldDisponibil(ibanCurentIon)   + " RON");
        System.out.println("  Sold Maria Curent: " + bancaService.getSoldDisponibil(ibanCurentMaria) + " RON");
        System.out.println();

        System.out.println("6: Interogare solduri si conturi");
        List<Cont> conturiIon = bancaService.listeazaConturi(ion.getCnp());
        for (Cont c : conturiIon) {
            System.out.println("  " + c.getNumarCont()
                    + " | " + c.getClass().getSimpleName()
                    + " | Sold: " + bancaService.getSoldDisponibil(c.getNumarCont()) + " RON"
                    + " | Dobanda: " + c.calculeazaDobanda() + " RON");
        }
        System.out.println();

        System.out.println("7: Emitere carduri");
        Card cardDebitIon   = cardService.emiteCard(ion,   "DEBIT");
        Card cardCreditIon  = cardService.emiteCard(ion,   "CREDIT");
        Card cardDebitMaria = cardService.emiteCard(maria, "DEBIT");
        System.out.println();

        System.out.println("8: Operatii pe carduri");
        if (cardCreditIon != null) {
            cardService.utilizeazaCredit(cardCreditIon.getNumarCard(), 2500.0);
            cardService.utilizeazaCredit(cardCreditIon.getNumarCard(), 99999.0);
        }
        if (cardDebitMaria != null) {
            cardService.blocheazaCard(cardDebitMaria.getNumarCard());
        }
        System.out.println();

        System.out.println("9: Interogare si stergere carduri");
        System.out.println("  Carduri active Ion:");
        for (Card c : cardService.carduriActive(ion)) {
            System.out.println("    " + c);
        }

        System.out.println("  Toate cardurile Maria:");
        for (Card c : cardService.listeazaCarduriClient(maria)) {
            System.out.println("    " + c);
        }

        if (cardDebitMaria != null) {
            System.out.println("  Cautare card Maria dupa numar:");
            Card gasit = cardService.cautaCardDupaNr(cardDebitMaria.getNumarCard());
            if (gasit != null) System.out.println("    Gasit: " + gasit.getNumarCard());

            System.out.println("  Stergere card Maria:");
            cardService.stergeCard(cardDebitMaria.getNumarCard(), maria);
        }
        System.out.println();

        System.out.println("10: Abonare servicii bancare");
        ServiciuBancar internetBanking = new ServiciuBancar(
                TipServiciu.INTERNET_BANKING, 5.0, 12, ibanCurentIon, LocalDate.now());
        ServiciuBancar smsAlert = new ServiciuBancar(
                TipServiciu.SMS_ALERT, 2.5, 6, ibanCurentMaria, LocalDate.now());

        bancaService.aboneazaServiciu(ibanCurentIon,   internetBanking);
        bancaService.aboneazaServiciu(ibanCurentMaria, smsAlert);
        System.out.println("  Cost total Internet Banking: "
                + internetBanking.calculeazaCostTotal() + " RON");
        System.out.println();

        System.out.println("11: Extras de cont Ion Curent");
        bancaService.genereazaExtras(
                ibanCurentIon,
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        );
        System.out.println();

        System.out.println("12: Tranzactii sortate dupa data");
        List<Tranzactie> tranzactii = bancaService.sorteazaTranzactii(ibanCurentIon);
        if (tranzactii.isEmpty()) {
            System.out.println("  Nu exista tranzactii.");
        } else {
            for (Tranzactie t : tranzactii) {
                System.out.println("  " + t);
            }
        }
        System.out.println();

        System.out.println("13: Cautare client dupa CNP");
        Client cautat = bancaService.cautaClient("1900101123456");
        if (cautat != null) {
            System.out.println("  Gasit: " + cautat.getNume() + " | " + cautat.getEmail());
        }
        System.out.println();

        System.out.println("14: Listare toti clientii (sortati dupa CNP)");
        for (Client c : bancaService.listeazaTotiClientii()) {
            System.out.println("  " + c.getCnp() + " | " + c.getNume());
        }
        System.out.println();

        System.out.println("15: Stergere client");
        bancaService.stergeClient(alex.getCnp());
        System.out.println("  Clienti ramasi: " + bancaService.listeazaTotiClientii().size());

        // =========================================================
        //  Etapa II — Persistenta JDBC, Tranzactii si Audit
        // =========================================================

        System.out.println("\n=== ETAPA II: PERSISTENTA JDBC ===");
        System.out.println("(Asigurati-va ca MySQL ruleaza si baza de date 'paoj_banca' exista)");
        System.out.println("(Rulati schema.sql inainte de prima pornire)\n");

        try {
            ClientRepository     clientRepo     = new ClientRepository();
            ContRepository       contRepo       = new ContRepository();
            CardRepository       cardRepo       = new CardRepository();
            TranzactieRepository tranzactieRepo = new TranzactieRepository();

            // --- 16: Salvare clienti si conturi in DB ---
            System.out.println("16: Salvare clienti, conturi, carduri si tranzactii in baza de date");

            // Clienti ramasi dupa stergerea lui Alex
            for (Client c : bancaService.listeazaTotiClientii()) {
                clientRepo.save(c);
                System.out.println("  Client salvat: " + c.getNume());
            }

            // Conturi (toate, inclusiv ale lui alex — sterse din memory dar dorim demo complet)
            for (Cont cont : bancaService.getConturi().values()) {
                // Salvam doar conturile clientilor existenti in DB
                if (clientRepo.findById(cont.getIdClient()).isPresent()) {
                    contRepo.save(cont);
                    System.out.println("  Cont salvat: " + cont.getNumarCont() + " (" + cont.getClass().getSimpleName() + ")");
                }
            }

            // Carduri active
            for (Card card : cardService.getCarduri().values()) {
                cardRepo.save(card);
                System.out.println("  Card salvat: " + card.getNumarCard());
            }

            // Tranzactii (au iban setat prin setIban in inregistreazaTranzactie)
            for (Map.Entry<String, List<Tranzactie>> entry : bancaService.getIstoricTranzactii().entrySet()) {
                String iban = entry.getKey();
                // Salvam tranzactiile doar pentru conturile persistate
                if (contRepo.findById(iban).isPresent()) {
                    for (Tranzactie t : entry.getValue()) {
                        tranzactieRepo.save(t);
                    }
                }
            }
            System.out.println();

            // --- 17: Transfer cu tranzactie JDBC explicita ---
            System.out.println("17: Transfer persistent cu tranzactie JDBC (commit/rollback)");
            bancaService.executaTransferPersistent(ibanCurentIon, ibanCurentMaria, 200.0);
            System.out.println();

            // --- 18: Interogare JDBC simpla (findAll / findById) ---
            System.out.println("18: Clienti din baza de date:");
            for (Client c : clientRepo.findAll()) {
                System.out.println("  " + c.getCnp() + " | " + c.getNume() + " | " + c.getEmail());
            }
            System.out.println();

            System.out.println("18b: Conturi din baza de date:");
            for (Cont c : contRepo.findAll()) {
                System.out.println("  " + c.getNumarCont() + " | " + c.getClass().getSimpleName()
                        + " | Sold: " + c.getSold() + " RON");
            }
            System.out.println();

            // --- 19: Interogari avansate cu JOIN ---
            System.out.println("19a: JOIN — Clienti cu numarul de tranzactii:");
            for (String linie : clientRepo.findTotiClientiiCuNrTranzactii()) {
                System.out.println("  " + linie);
            }
            System.out.println();

            System.out.println("19b: JOIN — Conturi cu informatii despre client:");
            for (String linie : contRepo.findConturiCuInfoClient()) {
                System.out.println("  " + linie);
            }
            System.out.println();

            System.out.println("19c: JOIN — Carduri active cu informatii despre cont si client:");
            List<String> carduriActive = cardRepo.findCarduriActiveCuInfoClient();
            if (carduriActive.isEmpty()) {
                System.out.println("  Nu exista carduri active in baza de date.");
            } else {
                for (String linie : carduriActive) {
                    System.out.println("  " + linie);
                }
            }
            System.out.println();

            System.out.println("19d: JOIN — Tranzactii cu detalii despre client:");
            for (String linie : tranzactieRepo.findTranzactiiCuInfoClient()) {
                System.out.println("  " + linie);
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println("\n[JDBC SKIP] Baza de date nu este disponibila: " + e.getMessage());
            System.out.println("Configurati db.properties si rulati schema.sql pentru a activa persistenta.");
        }

        // --- 20: Verificare fisier audit ---
        System.out.println("\n20: Fisier audit.csv generat cu actiunile executate.");
        System.out.println("  Calea: audit.csv (in directorul de lucru al aplicatiei)");

        AuditService.getInstance().log("sfarsit_sesiune");
        System.out.println("  Intrare finala scrisa in audit.csv.");
    }
}
