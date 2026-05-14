package com.pao.laboratory10.exercise3;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Tranzactie> tranzactii = Arrays.asList(
                new Tranzactie(1, 1500.0, "2024-01-10", TipTranzactie.CREDIT, "RO01"),
                new Tranzactie(2, 200.0,  "2024-01-15", TipTranzactie.DEBIT,  "RO01"),
                new Tranzactie(3, 3000.0, "2024-02-05", TipTranzactie.CREDIT, "RO02"),
                new Tranzactie(4, 50.0,   "2024-02-10", TipTranzactie.DEBIT,  "RO03"),
                new Tranzactie(5, 120.0,  "2024-02-20", TipTranzactie.DEBIT,  "RO01"),
                new Tranzactie(6, 450.0,  "2024-03-01", TipTranzactie.CREDIT, "RO04"),
                new Tranzactie(7, 800.0,  "2024-03-12", TipTranzactie.DEBIT,  "RO02"),
                new Tranzactie(8, 100.0,  "2024-03-15", TipTranzactie.DEBIT,  "RO03"),
                new Tranzactie(9, 2100.0, "2024-03-25", TipTranzactie.CREDIT, "RO01"),
                new Tranzactie(10, 55.0,  "2024-03-28", TipTranzactie.DEBIT,  "RO04")
        );

        System.out.println("\n# 1. Toate tranzactiile CREDIT:");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n# 2. Total procesat:");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total: %.2f RON\n", total);

        System.out.println("\n# 3. Suma per luna (yyyy-MM):");
        Map<String, Double> sumaPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        Tranzactie::getLuna,
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        sumaPerLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON\n", luna, suma));

        System.out.println("\n# 4. Top 3 tranzactii dupa suma:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n# 5. Conturi sursa unice:");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(conturiUnice);

        System.out.println("\n# 6. Suma medie a tranzactiilor:");
        tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .ifPresent(avg -> System.out.printf("Media: %.2f RON\n", avg));

        System.out.println("\n# 7. EXTRAS DE CONT GENERAT:");
        tranzactii.stream()
                .collect(Collectors.groupingBy(Tranzactie::getLuna, TreeMap::new, Collectors.toList()))
                .forEach((luna, lista) -> {
                    double totalLuna = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
                    System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON\n",
                            luna, lista.size(), totalLuna);
                });
    }
}