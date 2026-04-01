package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends Colaborator implements PersoanaJuridica  {
    private double cheltuieliLunare;

    public SRLColaborator() { super(TipColaborator.SRL); }

    @Override
    public double calculeazaVenitNetAnual() {
        return (venitBrutLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public void afiseaza(){
        System.out.println(tipContract() + ": " + this.nume + " " + this.prenume + ", venit net anual: " + calculeazaVenitNetAnual() );
    }
}