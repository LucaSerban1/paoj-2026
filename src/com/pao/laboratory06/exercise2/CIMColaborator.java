package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica{
    private String Bonus;

    public CIMColaborator() { super(TipColaborator.CIM); }

    @Override
    public double calculeazaVenitNetAnual() {
        double netBaza = venitBrutLunar * 12 * 0.55;
        if (areBonus()) {
            return netBaza * 1.10;
        }
        return netBaza;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.Bonus = in.next();
    }

    @Override
    public void afiseaza(){
        System.out.println(tipContract() + ": " + this.nume + " " + this.prenume + ", venit net anual: " + calculeazaVenitNetAnual() );
    }

    @Override
    public boolean areBonus() {
        return this.Bonus.equalsIgnoreCase("DA");
    }
}
