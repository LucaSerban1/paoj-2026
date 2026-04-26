package Model.Cont;

import java.time.LocalDate;
import Exceptii.FonduriInsuficienteException;

public class ContEconomii extends Cont {

    private double rataDobanda;
    private int perioadaMinima;

    public ContEconomii(String numarCont, double sold, LocalDate dataDeschidere, String idClient,
                        double rataDobanda, int perioadaMinima) {
        super(numarCont, sold, dataDeschidere, idClient);
        this.rataDobanda = rataDobanda;
        this.perioadaMinima = perioadaMinima;
    }

    @Override
    public double calculeazaDobanda() {
        return this.sold * rataDobanda;
    }

    public double getRataDobanda() {
        return rataDobanda;
    }

    public boolean esteEligibilRetragere() {
        LocalDate dataCurenta = LocalDate.now();

        LocalDate dataExpirareRestrictie = getDataDeschidere().plusMonths(perioadaMinima);

        return dataCurenta.isAfter(dataExpirareRestrictie) || dataCurenta.isEqual(dataExpirareRestrictie);
    }

    @Override
    public void retrage(double suma) throws FonduriInsuficienteException {
        if (!esteEligibilRetragere()) {
            throw new IllegalStateException("Retragere respinsă! Perioada minimă de "
                    + perioadaMinima + " luni nu a expirat.");
        }

        super.retrage(suma);
    }
}
