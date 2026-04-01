package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {

    private String utilizator;
    private String parolaHash;
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
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
        if (user.equals(this.utilizator) && parola.equals(this.parolaHash)) {
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
    public int compareTo(Inginer alt) {
        return this.nume.compareTo(alt.nume);
    }

    public void setCredentiale(String utilizator, String parola) {
        this.utilizator = utilizator;
        this.parolaHash = parola;
    }

    @Override
    public String toString() {
        return "Inginer{" + prenume + " " + nume + ", salariu=" + salariu + "}";
    }
}
