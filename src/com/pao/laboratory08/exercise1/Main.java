package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește comanda din stdin: PRINT, SHALLOW <nume> sau DEEP <nume>
        // 3. Execută comanda:
        //    - PRINT → afișează toți studenții
        //    - SHALLOW <nume> → shallow clone + modifică orașul clonei la "MODIFICAT" + afișează
        //    - DEEP <nume> → deep clone + modifică orașul clonei la "MODIFICAT" + afișează


        BufferedReader br=new BufferedReader(new FileReader(FILE_PATH));
        String comanda;
        Scanner sc=new Scanner(System.in);
        comanda=sc.nextLine();
        String line=br.readLine();
        List<Student>lista=new ArrayList<>();
        while(!line.isEmpty()){
            String[] parts=line.split(",");
            String nume=parts[0].trim();
            Integer varsta=Integer.parseInt(parts[1].trim());
            String oras=parts[2].trim();
            String strada=parts[3].trim();
            Adresa adresa=new Adresa(oras,strada);
            Student student=new Student(nume,varsta,adresa);
            lista.add(student);
            line=br.readLine();
        }
        br.close();
        if(comanda.equals("PRINT")){
            for(Student s:lista){
                System.out.println(s);
            }
        } else{
            String[] tokens=comanda.split(" ");
            String copytype=tokens[0];
            String nume=tokens[1];
            for(Student s:lista){
                if(s.getNume().equals(nume)){
                    Student clona;
                    if(copytype.equals("SHALLOW")){
                        clona=(Student)s.shallowClone();

                    }else{
                        clona=(Student)s.deepClone();
                    }
                    System.out.println("Original: "+s.toString());
                    System.out.println("Clona: "+clona.toString());
                }
            }
        }

//        System.out.println("TODO: implementează exercițiul 1");
    }
}