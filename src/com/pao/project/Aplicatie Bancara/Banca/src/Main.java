import Model.Client;
import Model.Card.*;
import Model.Cont.*;
import Model.ServiciuBancar;
import Model.TipServiciu;
import Model.Tranzactie.*;
import Service.BancaService;
import Service.CardService;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        BancaService bancaService = BancaService.getInstance();
        CardService cardService   = CardService.getInstance();

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

        System.out.println("12: Tranzactii sortate dupa data (Ion Curent)");
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

    }
}