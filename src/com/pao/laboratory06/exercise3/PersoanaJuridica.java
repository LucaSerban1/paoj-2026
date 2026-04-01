package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {

    private List<String> smsTrimise;
    private String utilizator;
    private String parola;
    private double sold;
    private boolean autentificat;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.smsTrimise = new ArrayList<>();
        this.sold = sold;
        this.autentificat = false;
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("Utilizatorul nu poate fi null sau gol.");
        }
        if (parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("Parola nu poate fi null sau goală.");
        }
        if (user.equals(this.utilizator) && parola.equals(this.parola)) {
            this.autentificat = true;
            System.out.println("Autentificare reușită pentru " + this);
        } else {
            this.autentificat = false;
            System.out.println("Credențiale incorecte pentru " + this);
        }
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie să fie pozitivă, primită: " + suma);
        }
        if (!autentificat) {
            System.out.println("Plată refuzată — nu ești autentificat.");
            return false;
        }
        if (suma > sold) {
            return false;
        }
        sold -= suma;
        return true;
    }

    @Override
    public boolean trimiteSMS(String mesaj) {
        if (telefon == null || telefon.isBlank()) {
            return false;
        }
        if (mesaj == null || mesaj.isBlank()) {
            return false;
        }
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }

    public void setCredentiale(String utilizator, String parola) {
        this.utilizator = utilizator;
        this.parola = parola;
    }

    @Override
    public String toString() {
        return "PersoanaJuridica{" + prenume + " " + nume + "}";
    }
}