package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        // Vezi Readme.md pentru cerințe
        CoadaTranzactii banda = new CoadaTranzactii(5);

        ATMThread atm1 = new ATMThread(1, banda);
        ATMThread atm2 = new ATMThread(2, banda);
        ATMThread atm3 = new ATMThread(3, banda);

        ProcessorThread procRunnable = new ProcessorThread(banda);
        Thread processorThread = new Thread(procRunnable);

        processorThread.start();
        atm1.start();
        atm2.start();
        atm3.start();

        atm1.join();
        atm2.join();
        atm3.join();

        procRunnable.activ = false;
        synchronized (banda) {
            banda.notifyAll();
        }

        processorThread.join();
    }
}