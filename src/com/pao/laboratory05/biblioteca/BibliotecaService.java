package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {
    private Carte[] carti = new Carte[0];
    private static BibliotecaService instance;

   private BibliotecaService(){}

   public static BibliotecaService getInstance(){
       if(instance == null){
           instance = new BibliotecaService();
       }
       return instance;
   }

   public void addCarte(Carte carte){
       Carte[] copie = new Carte[carti.length + 1];
       System.arraycopy(carti, 0 , copie , 0 , carti.length);
       copie[copie.length - 1] = carte;
       carti = copie;
   }

   public void listSortedByRating(){
       Carte[] copie = carti;
       Arrays.sort(copie);
       System.out.println(Arrays.toString(copie));
   }

    public void listSortedBy(Comparator<Carte> comparator){
        Carte[] copie = carti;
        Arrays.sort(copie, comparator);
        System.out.println(Arrays.toString(copie));
    }
}
