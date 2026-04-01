package com.pao.laboratory06.exercise2;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tipStr = sc.next();

            Colaborator c = null;
            TipColaborator tip = TipColaborator.valueOf(tipStr.toUpperCase());
            switch (tip) {
                case CIM -> c = new CIMColaborator();
                case PFA -> c = new PFAColaborator();
                case SRL -> c = new SRLColaborator();
            }

            if (c != null) {
                c.citeste(sc);
                colaboratori.add(c);
            }
        }

        for (var colaborator : colaboratori) {
            colaborator.afiseaza();
        }

        System.out.println();
        Collections.sort(colaboratori);

        double max = 0.0;
        for (var colaborator : colaboratori) {
            colaborator.afiseaza();
        }

        System.out.println();
        System.out.print("Colaborator cu venit net maxim: " );
        colaboratori.get(colaboratori.size()-1).afiseaza();


        System.out.println();
        for (var colaborator : colaboratori) {
            if (colaborator instanceof PersoanaJuridica) {
                colaborator.afiseaza();
            }
        }
        System.out.println();

        System.out.println("Sume și număr colaboratori pe tip:");
        for (TipColaborator tipEnum : TipColaborator.values()) {
            double sumaVenituri = 0;
            int numarColaboratori = 0;

            for (Colaborator c : colaboratori) {
                if (c.tipContract().equalsIgnoreCase(tipEnum.name())) {
                    sumaVenituri += c.calculeazaVenitNetAnual();
                    numarColaboratori++;
                }
            }

            System.out.printf("%s: suma = %.2f lei, număr = %d\n",
                    tipEnum.name(), sumaVenituri, numarColaboratori);
        }
    }
}