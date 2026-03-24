package com.pao.laboratory05.audit;

import java.util.Scanner;
/**
 * Exercise 4 (Bonus) — Audit Log
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 4 (Bonus) — Audit"
 *
 * Extinde soluția de la Exercise 3 cu un sistem de audit bazat pe record.
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("Cerințele se află în Readme.md — secțiunea Exercise 4 (Bonus).");

        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("4. Afișează audit log");
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
                case 4:
                    service.printAuditLog();
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
