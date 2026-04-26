package Model;

import Model.Tranzactie.TipTranzactie;
import Model.Tranzactie.Tranzactie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExtrasDecont {
    private String numarCont;
    private LocalDate dataStart;
    private LocalDate dataFinal;
    private List<Tranzactie> tranzactii;

    public ExtrasDecont(String numarCont, LocalDate dataStart, LocalDate dataFinal) {
        this.numarCont = numarCont;
        this.dataStart = dataStart;
        this.dataFinal = dataFinal;
        this.tranzactii = new ArrayList<>();
    }

    public void adaugaTranzactie(Tranzactie tranzactie) {
        if (tranzactie != null) {
            this.tranzactii.add(tranzactie);
        }
    }

    public double getTotalDebitat() {
        return tranzactii.stream()
                .filter(this::esteInInterval)
                .filter(t -> t.getTip() == TipTranzactie.RETRAGERE ||
                        t.getTip() == TipTranzactie.TRANSFER ||
                        t.getTip() == TipTranzactie.PLATA_POS)
                .mapToDouble(Tranzactie::getSuma)
                .sum();
    }

    public void afiseazaExtras() {
        System.out.println("EXTRAS DE CONT - Cont: " + numarCont);
        System.out.println("Perioada: " + dataStart + " -> " + dataFinal);

        Collections.sort(tranzactii);

        List<Tranzactie> tranzactiiFiltrate = tranzactii.stream()
                .filter(this::esteInInterval)
                .collect(Collectors.toList());

        if (tranzactiiFiltrate.isEmpty()) {
            System.out.println("Nicio tranzacție găsită în această perioadă.");
        } else {
            for (Tranzactie t : tranzactiiFiltrate) {
                System.out.println(t.toString());
            }
        }
        System.out.println();
        System.out.printf("TOTAL DEBITAT (Cheltuieli): %.2f RON\n", getTotalDebitat());
    }

    private boolean esteInInterval(Tranzactie t) {
        LocalDate dataT = t.getData().toLocalDate();
        return !dataT.isBefore(dataStart) && !dataT.isAfter(dataFinal);
    }
}