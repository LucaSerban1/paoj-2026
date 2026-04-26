package Service;

import Exceptii.LimitaCreditDepasitaException;
import Model.Card.Card;
import Model.Card.CardCredit;
import Model.Card.CardDebit;
import Model.Client;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CardService {

    private static CardService instance;

    private CardService() {}

    public static CardService getInstance() {
        if (instance == null) {
            instance = new CardService();
        }
        return instance;
    }

    private final Map<String, Card> carduri = new HashMap<>();

    public Card emiteCard(Client client, String tipCard) {
        if (client == null || client.getConturi().isEmpty()) {
            System.out.println("Eroare: client invalid sau fara cont asociat.");
            return null;
        }

        String idCont      = client.getConturi().get(0).getNumarCont();
        String numarCard   = genereazaNumarCard();
        String cvv         = String.format("%03d", (int)(Math.random() * 1000));
        LocalDate expirare = LocalDate.now().plusYears(tipCard.equalsIgnoreCase("CREDIT") ? 3 : 4);

        Card cardNou;
        if (tipCard.equalsIgnoreCase("DEBIT")) {
            cardNou = new CardDebit(numarCard, expirare, cvv, idCont, 5000.0, true);
        } else if (tipCard.equalsIgnoreCase("CREDIT")) {
            cardNou = new CardCredit(numarCard, expirare, cvv, idCont, 10000.0);
        } else {
            System.out.println("Tip card invalid. Folositi 'DEBIT' sau 'CREDIT'.");
            return null;
        }

        carduri.put(numarCard, cardNou);
        client.adaugaCard(cardNou);
        System.out.println("Card " + tipCard + " emis: " + numarCard);
        return cardNou;
    }

    public void stergeCard(String numarCard, Client client) {
        Card card = carduri.remove(numarCard);
        if (card == null) {
            System.out.println("Cardul " + numarCard + " nu a fost gasit.");
            return;
        }
        if (client != null) {
            client.getCarduri().remove(card);
        }
        System.out.println("Cardul " + numarCard + " a fost sters.");
    }

    public Card cautaCardDupaNr(String numarCard) {
        Card card = carduri.get(numarCard);
        if (card == null) {
            System.out.println("Cardul " + numarCard + " nu exista.");
        }
        return card;
    }

    public List<Card> listeazaCarduriClient(Client client) {
        if (client == null) return Collections.emptyList();
        return new ArrayList<>(client.getCarduri());
    }

    public List<Card> listeazaToate() {
        return new ArrayList<>(carduri.values());
    }

    public void blocheazaCard(String numarCard) {
        Card card = carduri.get(numarCard);
        if (card != null) {
            card.setActiv(false);
            System.out.println("Cardul " + numarCard + " a fost blocat.");
        } else {
            System.out.println("Cardul " + numarCard + " nu a fost gasit.");
        }
    }

    public void utilizeazaCredit(String numarCard, double suma) {
        Card card = carduri.get(numarCard);
        if (!(card instanceof CardCredit)) {
            System.out.println("Cardul " + numarCard + " nu este un card de credit.");
            return;
        }
        try {
            ((CardCredit) card).utilizeazaCredit(suma);
            System.out.println("Credit utilizat: " + suma + " RON. Disponibil ramas: "
                    + ((CardCredit) card).getSoldDisponibil() + " RON");
        } catch (LimitaCreditDepasitaException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public List<Card> carduriActive(Client client) {
        return listeazaCarduriClient(client).stream()
                .filter(Card::esteActiv)
                .collect(Collectors.toList());
    }

    private String genereazaNumarCard() {
        return "4" + String.format("%015d", (long)(Math.random() * 1_000_000_000_000_000L));
    }
}