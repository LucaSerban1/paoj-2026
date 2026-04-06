package com.pao.laboratory07.exercise3;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected String client;
    protected double pret;
    public abstract double pretFinal();
    public abstract String getClient();
    public abstract String descriere();
}