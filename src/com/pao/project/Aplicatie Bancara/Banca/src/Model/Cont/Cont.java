package Model.Cont;

import java.time.LocalDate;
import Exceptii.FonduriInsuficienteException;

public abstract class Cont {
    private String numarCont;
    protected double sold;
    private LocalDate dataDeschidere;
    private String idClient;

    public Cont(String numarCont, double sold, LocalDate dataDeschidere, String idClient) {
        this.numarCont = numarCont;
        this.sold = sold;
        this.dataDeschidere = dataDeschidere;
        this.idClient = idClient;
    }

    public double getSold() { return sold; }
    public void setSold(double sold) { this.sold = sold; }

    public void depune(double suma) {
        if (suma > 0) this.sold += suma;
    }

    public void retrage(double suma) throws FonduriInsuficienteException {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie să fie pozitivă!");
        }
        if (this.sold < suma) {
            throw new FonduriInsuficienteException("Sold insuficient: " + sold + " (Suma cerută: " + suma + ")");
        }
        this.sold -= suma;
    }

    public abstract double calculeazaDobanda();

    public LocalDate getDataDeschidere() {
        return dataDeschidere;
    }

    public String getNumarCont() { return numarCont;}

    public String getIdClient() { return idClient;}

    public void setNumarCont(String numarCont) { this.numarCont = numarCont;}
}