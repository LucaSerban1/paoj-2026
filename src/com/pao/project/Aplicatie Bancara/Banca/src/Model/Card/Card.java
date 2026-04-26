package Model.Card;

import java.time.LocalDate;

public abstract class Card {

    private String numarCard;
    private LocalDate dataExpirare;
    protected String cvv;
    private String idCont;
    private boolean activ;

    public Card(String numarCard, LocalDate dataExpirare, String cvv, String idCont) {
        this.numarCard = numarCard;
        this.dataExpirare = dataExpirare;
        this.cvv = cvv;
        this.idCont = idCont;
        this.activ = true;
    }

    public abstract double getLimitaZilnica();

    public String getNumarCard() { return numarCard; }
    public LocalDate getDataExpirare() { return dataExpirare; }
    public String getIdCont() { return idCont; }
    public boolean isActiv() { return activ; }
    public void setActiv(boolean activ) { this.activ = activ; }

    public boolean esteActiv() {
        return activ && LocalDate.now().isBefore(dataExpirare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return numarCard.equals(card.numarCard);
    }

    @Override
    public int hashCode() {
        return numarCard.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" + numarCard + "]" +
                " | Cont: " + idCont +
                " | Expira: " + dataExpirare +
                " | Activ: " + esteActiv();
    }
}