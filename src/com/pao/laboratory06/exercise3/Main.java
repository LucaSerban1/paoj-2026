package com.pao.laboratory06.exercise3;

public class Main {
    public static void main(String[] args) {
        System.out.println("TVA: " + ConstanteFinanciare.TVA.getValoare());
        System.out.println("Salariu Minim: " + ConstanteFinanciare.SALARIU_MINIM.getValoare());
        System.out.println();

        Inginer i1 = new Inginer("Popescu", "Ion", "0722111222", 8000, 140000);
        Inginer i2 = new Inginer("Ionescu", "Ana", "0733444555", 9500, 100000);
        Inginer i3 = new Inginer("Georgescu", "Dan", null, 7000, 120000);

        Inginer[] ingineri = {i1, i2, i3};

        java.util.Arrays.sort(ingineri);
        System.out.println("Sortare Alfabetică:");
        for(Inginer ing : ingineri) System.out.println(ing.nume + " - " + ing.salariu);

        java.util.Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("\nSortare după Salariu (Descrescător):");
        for(Inginer ing : ingineri) System.out.println(ing.nume + " - " + ing.salariu);

        System.out.println();
        PlataOnline plata = i1;
        plata.autentificare("user123", "parolaSafe");
        System.out.println("Sold consultat prin interfață: " + plata.consultareSold() +"\n");

        PersoanaJuridica firma = new PersoanaJuridica("TechCorp", "SRL", "0214445555", 30000);
        PlataOnlineSMS servSms = firma;

        System.out.println("Trimitere SMS valid: " + servSms.trimiteSMS("Salutare client!"));

        PersoanaJuridica firmaFaraTel = new PersoanaJuridica("NoPhone", "SA", null, 300000);
        System.out.println("Trimitere SMS (fără telefon): " + firmaFaraTel.trimiteSMS("Mesaj"));

        System.out.println();
        try {
            i1.autentificare(null, "1234");
        } catch (IllegalArgumentException e) {
            System.out.println("Capturat eroare autentificare: " + e.getMessage());
        }

        try {
            System.out.println("Inginerul nu suportă SMS (corect conform design)");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}