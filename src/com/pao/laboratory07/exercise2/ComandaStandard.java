package com.pao.laboratory07.exercise2;

public final class ComandaStandard extends Comanda{
    public ComandaStandard(String nume,double pret) {
        this.nume=nume;
        this.pret=pret;
    }

    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return "STANDARD: "+nume+", pret: "+String.format("%.2f",pret)+" lei [PLACED]";
    }
}