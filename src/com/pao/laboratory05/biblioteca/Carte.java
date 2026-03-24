package com.pao.laboratory05.biblioteca;

public class Carte implements Comparable<Carte> {
    String titlu;
    String autor;
    int an;
    double rating;

    public Carte(String titlu, String autor, int an, double rating) {
        this.rating = rating;
        this.an = an;
        this.titlu = titlu;
        this.autor = autor;
    }

    public String getTitlu() { return titlu; }

    public String getAutor() { return autor; }

    public double getRating() { return rating; }

    public int getAn() { return an;}

    @Override
    public String toString() {
        return "Carte{" +
                "titlu='" + titlu + '\'' +
                ", autor='" + autor + '\'' +
                ", an=" + an +
                ", rating=" + rating +
                '}' + "\n";
    }

    @Override
    public int compareTo(Carte o) {
        if (this.getRating() == o.getRating()) return 0;

        return (o.getRating() > this.getRating()) ? 1 : -1;
    }
}
