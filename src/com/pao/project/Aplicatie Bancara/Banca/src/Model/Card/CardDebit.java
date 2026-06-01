package Model.Card;

import Model.Card.Card;

import java.time.LocalDate;

public class CardDebit extends Card {

    private double limitaZilnica;
    private boolean contactless;

    public CardDebit(String numarCard, LocalDate dataExpirare, String cvv,
                     String idCont, double limitaZilnica, boolean contactless) {
        super(numarCard, dataExpirare, cvv, idCont);
        this.limitaZilnica = limitaZilnica;
        this.contactless = contactless;
    }

    @Override
    public double getLimitaZilnica() {
        return limitaZilnica;
    }

    public void setLimitaZilnica(double limitaZilnica) {
        this.limitaZilnica = limitaZilnica;
    }

    public boolean isContactless() { return contactless; }
    public void setContactless(boolean contactless) { this.contactless = contactless; }

    @Override
    public String toString() {
        return super.toString() +
                " | Limita zilnica: " + limitaZilnica + " RON" +
                " | Contactless: " + contactless;
    }
}