package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];

    private static class Holder{
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance(){
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a){
        Angajat[] copie = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, copie, 0 , angajati.length);
        copie[copie.length - 1] = a;
        angajati = copie;
    }

    public void printAll(){
        System.out.println(Arrays.toString(angajati));
    }

    public void listbySalary(){
        Angajat[] copie = angajati;
        Arrays.sort(copie);
        System.out.println(Arrays.toString(copie));
    }

    public void findByDepartament(String numeDept){
        boolean ok = false;
        for(Angajat angajat : angajati){
            if(angajat.getDepartament().nume().equalsIgnoreCase(numeDept)){
                ok = true;
                System.out.println(angajat);
            }
        }
        if(!ok)
            System.out.println("Niciun angajat în departamentul: " + numeDept);
    }
}
