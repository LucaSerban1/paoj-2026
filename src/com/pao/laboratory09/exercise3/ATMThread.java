package com.pao.laboratory09.exercise3;

class ATMThread extends Thread {
    private final int idATM;
    private final CoadaTranzactii banda;

    public ATMThread(int id, CoadaTranzactii banda) {
        this.idATM = id;
        this.banda = banda;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 4; i++) {
                int transId = idATM * 100 + i;
                Tranzactie t = new Tranzactie(transId, 100.0 + i, "2024-05-10");
                System.out.println("[ATM-" + idATM + "] trimite: Tranzactie #" + transId + " " + t.suma + " RON");
                banda.adauga(t, idATM);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}