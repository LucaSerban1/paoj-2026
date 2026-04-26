package Model.Card;

import java.time.LocalDate;

import Exceptii.LimitaCreditDepasitaException;

public class CardCredit extends Card {

    private double limitaCredit;
    private double soldUtilizat;

    public CardCredit(String numarCard, LocalDate dataExpirare, String cvv,
                      String idCont, double limitaCredit) {
        super(numarCard, dataExpirare, cvv, idCont);
        this.limitaCredit = limitaCredit;
        this.soldUtilizat = 0.0;
    }

    @Override
    public double getLimitaZilnica() {
        return limitaCredit;
    }

    public double getSoldDisponibil() {
        return limitaCredit - soldUtilizat;
    }

    public double getLimitaCredit() { return limitaCredit; }
    public double getSoldUtilizat() { return soldUtilizat; }

    public void utilizeazaCredit(double suma) throws LimitaCreditDepasitaException {
        if (suma > getSoldDisponibil()) {
            throw new LimitaCreditDepasitaException(
                    "Limita de credit depasita. Disponibil: " + getSoldDisponibil() + " RON, solicitat: " + suma + " RON"
            );
        }
        this.soldUtilizat += suma;
    }

    public void ramburseaza(double suma) {
        this.soldUtilizat = Math.max(0, soldUtilizat - suma);
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Limita credit: " + limitaCredit + " RON" +
                " | Utilizat: " + soldUtilizat + " RON" +
                " | Disponibil: " + getSoldDisponibil() + " RON";
    }
}