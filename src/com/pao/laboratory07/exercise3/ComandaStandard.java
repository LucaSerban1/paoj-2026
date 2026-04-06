package com.pao.laboratory07.exercise3;

public final class ComandaStandard extends Comanda {
    public ComandaStandard(String nume,double pret,String client) {
        this.nume=nume;
        this.pret=pret;
        this.client=client;
    }
    @Override
    public String getClient() {
        return client;
    }
    @Override
    public double pretFinal() {
        return pret;
    }

    @Override
    public String descriere() {
        return "STANDARD: "+nume+", pret: "+String.format("%.2f",pret)+" lei [PLACED] - client: "+client;
    }
}