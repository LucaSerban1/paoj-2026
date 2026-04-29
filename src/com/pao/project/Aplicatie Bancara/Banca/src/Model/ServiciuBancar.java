package Model;
import Model.TipServiciu;

import java.time.LocalDate;

public class ServiciuBancar {

    private TipServiciu tip;
    private double costLunar;
    private int durata;
    private String idCont;

    private LocalDate dataActivare;

    public ServiciuBancar(TipServiciu tip, double costLunar, int durata, String idCont, LocalDate dataActivare) {
        this.tip = tip;
        this.costLunar = costLunar;
        this.durata = durata;
        this.idCont = idCont;
        this.dataActivare = dataActivare;
    }

    public TipServiciu getTip() { return tip;}

    public double getCostLunar() { return costLunar;}

    public double calculeazaCostTotal() {
        return costLunar * durata;
    }

    public boolean esteActiv() {
        if (dataActivare == null) {
            return false;
        }

        LocalDate dataCurenta = LocalDate.now();
        LocalDate dataExpirare = dataActivare.plusMonths(durata);

        return !dataCurenta.isAfter(dataExpirare);
    }

    public int getDurata() { return durata;}

    public void setDurata(int durata) { this.durata = durata;}

    public String getIdCont() { return idCont;}

    public LocalDate getDataActivare() { return dataActivare;}
}