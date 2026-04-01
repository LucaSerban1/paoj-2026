package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica{
    private double cheltuieliLunare;

    public PFAColaborator() { super(TipColaborator.PFA); }

    @Override
    public double calculeazaVenitNetAnual() {
        double Salariu_minim_brut = 4050.0;
        double venitNetInitial = (venitBrutLunar - cheltuieliLunare) * 12;

        double cass;
        if (venitNetInitial < 6 * Salariu_minim_brut) {
            cass = 0.10 * (6 * Salariu_minim_brut);
        } else if (venitNetInitial <= 72 * Salariu_minim_brut) {
            cass = 0.10 * venitNetInitial;
        } else {
            cass = 0.10 * (72 * Salariu_minim_brut);
        }

        double cas = 0;
        if (venitNetInitial >= 12 * Salariu_minim_brut && venitNetInitial <= 24 * Salariu_minim_brut) {
            cas = 0.25 * (12 * Salariu_minim_brut);
        } else if (venitNetInitial > 24 * Salariu_minim_brut) {
            cas = 0.25 * (24 * Salariu_minim_brut);
        }

        double impozit = 0.10 * venitNetInitial;

        return venitNetInitial - impozit - cass - cas;
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
