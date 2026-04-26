package Model.Cont;

import java.time.LocalDate;
import Exceptii.FonduriInsuficienteException;

public class ContCurent extends Cont {

    private double limitaDescoperit;
    private double comisionLunar;

    public ContCurent(String numarCont, double sold, LocalDate dataDeschidere, String idClient,
                      double limitaDescoperit, double comisionLunar) {
        super(numarCont, sold, dataDeschidere, idClient);
        this.limitaDescoperit = limitaDescoperit;
        this.comisionLunar = comisionLunar;
    }

    @Override
    public void retrage(double suma) throws FonduriInsuficienteException {
        if (suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie să fie pozitivă!");
        }

        if (this.sold + limitaDescoperit < suma) {
            throw new FonduriInsuficienteException("Limita de credit depășită! Disponibil total: "
                    + (this.sold + limitaDescoperit));
        }

        this.sold -= suma;
    }

    @Override
    public double calculeazaDobanda() {
        return 0;
    }

    public void aplicaComision() {
        this.sold -= comisionLunar;
    }

    public double getLimitaDescoperit() {
        return limitaDescoperit;
    }
}