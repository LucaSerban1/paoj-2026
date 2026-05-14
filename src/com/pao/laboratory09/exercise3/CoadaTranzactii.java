package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

class CoadaTranzactii {
    private final Queue<Tranzactie> coada = new LinkedList<>();
    private final int capacitateMax;

    public CoadaTranzactii(int capacitate) {
        this.capacitateMax = capacitate;
    }

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (coada.size() == capacitateMax) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
        }
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized int getDimensiune() {
        return coada.size();
    }
}