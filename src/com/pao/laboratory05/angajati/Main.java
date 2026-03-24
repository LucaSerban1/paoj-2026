package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Cerințele se află în Readme.md — secțiunea Exercise 3.");

        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            switch (optiune){
                case 1:
                    System.out.print("Nume: ");
                    String nume = scanner.next();
                    System.out.print("Nume Departament: ");
                    String nume_dep = scanner.next();
                    System.out.print("Locatie Departament: ");
                    String loc_dep = scanner.next();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();

                    Departament dep = new Departament(nume_dep, loc_dep);
                    Angajat angajat = new Angajat(nume, dep, salariu);

                    service.addAngajat(angajat);
                    break;
                case 2:
                    service.listbySalary();
                    break;
                case 3:
                    System.out.print("Nume Departament: ");
                    String nume_departament = scanner.next();
                    service.findByDepartament(nume_departament);
                    break;
                case 0:
                    System.out.println("La revedere!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opțiune invalidă. Încearcă din nou.");
            }
        }
    }
}
