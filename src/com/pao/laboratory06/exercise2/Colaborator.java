package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere, Comparable<Colaborator>{
    protected String nume;
    protected String prenume;
    protected TipColaborator tip;
    protected double venitBrutLunar;

    public Colaborator(TipColaborator tip) {
        this.tip = tip;
    }

    public Colaborator(String nume, String prenume, double venitBrutLunar) {
        this.nume = nume;
        this.prenume = prenume;
        this.venitBrutLunar = venitBrutLunar;
    }

    @Override
    public String tipContract() {
        return tip.toString();
    }

    abstract double calculeazaVenitNetAnual();

    @Override
    public int compareTo(Colaborator o) {
        return Double.compare(o.calculeazaVenitNetAnual(), this.calculeazaVenitNetAnual());
    }
}
