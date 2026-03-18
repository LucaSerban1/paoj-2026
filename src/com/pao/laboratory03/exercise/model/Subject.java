package com.pao.laboratory03.exercise.model;

public enum Subject {
    PAOJ("Programare avansata pe obiecte in Java", 6),
    BD("Baze de Date", 4),
    SO("Sisteme de Operare", 4),
    RC("Refacerea Compilatoarelor", 5);

    private String fullName;
    private int credits;

    Subject(String fullName, int credits){
        this.fullName = fullName;
        this.credits = credits;
    }

    public String getFullName() {
        return fullName;
    }

    public int getCredits() {
        return credits;
    }
    @Override
    public String toString(){
        return name() + "(" + fullName + ", " + credits + "credits)";
    }
}
