package com.pao.laboratory07.exercise2;

public final class ComandaRedusa extends Comanda{
    private int discountProcent;
    public ComandaRedusa(String nume, double pret,int discountProcent) {
        this.nume=nume;
        this.pret=pret;
        this.discountProcent = discountProcent;
    }

    @Override
    public double pretFinal() {
        return pret*(1-discountProcent/100.0);
    }

    @Override
    public String descriere() {
        return "DISCOUNTED: "+nume+", pret: "+String.format("%.2f",pretFinal())+" lei (-"+discountProcent+"%) [PLACED]";
    }
}