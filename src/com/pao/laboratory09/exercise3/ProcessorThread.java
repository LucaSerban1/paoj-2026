package com.pao.laboratory09.exercise3;

class ProcessorThread implements Runnable {
    private final CoadaTranzactii banda;
    public volatile boolean activ = true;
    private int totalProcesate = 0;

    public ProcessorThread(CoadaTranzactii banda) {
        this.banda = banda;
    }

    @Override
    public void run() {
        try {
            while (activ || banda.getDimensiune() > 0) {
                if (!activ && banda.getDimensiune() == 0) break;

                Tranzactie t = banda.extrage();
                System.out.println("[Processor] Factura #" + t.id + " - " + t.suma + " RON | " + t.data);
                totalProcesate++;
                Thread.sleep(80);
            }
        } catch (InterruptedException e) {
        }
        System.out.println("Toate tranzactiile procesate. Total: " + totalProcesate);
    }
}