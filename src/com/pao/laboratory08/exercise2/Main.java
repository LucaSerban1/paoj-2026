package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește studenții din FILE_PATH cu BufferedReader
        // 2. Citește pragul de vârstă din stdin cu Scanner
        // 3. Filtrează studenții cu varsta >= prag
        // 4. Scrie filtrații în "rezultate.txt" cu BufferedWriter
        // 5. Afișează sumarul la consolă
        BufferedReader br=new BufferedReader(new FileReader(FILE_PATH));
        Integer prag;
        Scanner sc=new Scanner(System.in);
        prag=Integer.parseInt(sc.nextLine().trim());
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
        BufferedWriter bw=new BufferedWriter(new FileWriter("src/com/pao/laboratory08/exercise2/rezultate.txt"));
        List<Student>listafiltrata=lista.stream().filter(s->s.getVarsta()>=prag).toList();
        System.out.println("Filtru : varsta >= "+prag);
        System.out.println("Rezultate: "+listafiltrata.size()+" studenti");
        for(Student s:listafiltrata){
            System.out.println(s);
            bw.write(s.toString());
            bw.newLine();
        }
        System.out.println("Scris in: rezultate.txt");
        bw.close();


    }
}