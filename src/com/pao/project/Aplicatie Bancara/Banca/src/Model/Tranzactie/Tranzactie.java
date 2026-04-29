package Model.Tranzactie;

import Model.Tranzactie.TipTranzactie;

import java.time.LocalDateTime;
import java.util.Objects;

public class Tranzactie implements Comparable<Tranzactie> {
    private String id;
    private double suma;
    private TipTranzactie tip;
    private LocalDateTime data;
    private String descriere;

    public Tranzactie(String id, double suma, TipTranzactie tip, LocalDateTime data, String descriere) {
        this.id = id;
        this.suma = suma;
        this.tip = tip;
        this.data = data;
        this.descriere = descriere;
    }

    public double getSuma() { return suma;}

    public LocalDateTime getData() { return data;}

    public String getId() { return id; }
    public TipTranzactie getTip() { return tip; }
    public String getDescriere() { return descriere; }

    @Override
    public int compareTo(Tranzactie altaTranzactie) {
        return altaTranzactie.data.compareTo(this.data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tranzactie that = (Tranzactie) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Tranzacție [ID: %s] | Tip: %s | Sumă: %.2f | Data: %s | Descriere: %s",
                id, tip, suma, data, descriere);
    }
}