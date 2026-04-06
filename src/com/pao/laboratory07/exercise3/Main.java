package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");
            if (tokens[0].equals("STANDARD")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                String client = tokens[3];
                Comanda c = new ComandaStandard(nume, pret,client);
                comenzi.add(c);
            } else if (tokens[0].equals("DISCOUNTED")) {
                String nume = tokens[1];
                double pret = Double.parseDouble(tokens[2]);
                int discount = Integer.parseInt(tokens[3]);
                String client = tokens[4];
                Comanda c = new ComandaRedusa(nume, pret, discount,client);
                comenzi.add(c);
            } else if (tokens[0].equals("GIFT")) {
                String nume = tokens[1];
                String client = tokens[2];
                Comanda c = new ComandaGratuita(nume,client);
                comenzi.add(c);
            }
        }
        System.out.println();
        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        String line = sc.nextLine().trim();
        String[] tokens = line.split(" ");
        while(!tokens[0].equals("QUIT")){
            if(tokens[0].equals("STATS")){
                System.out.println("--- STATS ---\n");
                Map<Class<? extends Comanda>, Double> mediiPreturi = comenzi.stream()
                        .collect(Collectors.groupingBy(
                                Comanda::getClass,
                                Collectors.averagingDouble(Comanda::pretFinal)
                        ));

                mediiPreturi.forEach((clasa, medie) ->
                        System.out.println(clasa.getSimpleName() + ": medie= " + medie+" lei")
                );
            } else if (tokens[0].equals("FILTER")) {
                double threshold=Double.parseDouble(tokens[1]);
                System.out.printf("--- FILTER (>= %.2f) ---\n",threshold);
                List<Comanda> filtered=comenzi.stream().filter(comanda -> comanda.pretFinal()>=threshold).toList();
                for(Comanda c : filtered){
                    System.out.println(c.descriere());
                }
            }else if(tokens[0].equals("SORT")){
                System.out.println("--- SORT(by client, then by pret) ---\n");
                List<Comanda> copy=new ArrayList<>(comenzi);
                Comparator<Comanda> comandaComparator=Comparator.comparing(Comanda::getClient).thenComparing(Comanda::pretFinal);
                copy.sort(comandaComparator);
                for(Comanda c:copy){
                    System.out.println(c.descriere());
                }
            } else if (tokens[0].equals("SPECIAL")) {
                System.out.println("--- SPECIAL (discount > 15%) ---\n");
                for(Comanda c: comenzi){
                    if(c instanceof ComandaRedusa && ((ComandaRedusa) c).getDiscountProcent()>15){
                        System.out.println(c.descriere());
                    }
                }
            }
            line=sc.nextLine().trim();
            tokens = line.split(" ");
        }
    }
}