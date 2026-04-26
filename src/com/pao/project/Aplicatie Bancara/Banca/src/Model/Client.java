package Model;

import Model.Cont.Cont;
import Model.Card.Card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Client {
    private String cnp;
    private String nume;
    private String email;
    private List<Cont> conturi;
    private Set<Card> carduri;

    public Client(String cnp, String nume, String email) {
        this.cnp = cnp;
        this.nume = nume;
        this.email = email;

        this.conturi = new ArrayList<>();
        this.carduri = new HashSet<>();
    }

    public void adaugaCont(Cont cont) {
        if (cont != null) {
            this.conturi.add(cont);
        } else {
            System.out.println("Nu se poate adăuga un cont invalid (null).");
        }
    }

    public void adaugaCard(Card card) {
        if (card != null) {
            this.carduri.add(card);
        } else {
            System.out.println("Nu se poate adăuga un card invalid (null).");
        }
    }

    public List<Cont> getConturi() { return conturi;}

    public Set<Card> getCarduri() { return carduri;}

    public String getCnp() { return cnp;}

    public void setCnp(String cnp) { this.cnp = cnp;}

    public String getNume() { return nume;}

    public void setNume(String nume) {this.nume = nume;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}
}