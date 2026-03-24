package com.pao.laboratory05.audit;

import java.time.LocalDateTime;
import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati = new Angajat[0];
    private AuditEntry[] auditLog = new AuditEntry[0];

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance(){
        return Holder.INSTANCE;
    }

    private void logAction(String action, String target){
        AuditEntry[] copie = new AuditEntry[auditLog.length + 1];
        System.arraycopy(auditLog, 0, copie, 0 , auditLog.length);
        AuditEntry a = new AuditEntry(action,target, LocalDateTime.now().toString());
        copie[copie.length - 1] = a;
        auditLog = copie;
    }

    public void addAngajat(Angajat a){
        Angajat[] copie = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, copie, 0 , angajati.length);
        copie[copie.length - 1] = a;
        angajati = copie;
        logAction("ADD", a.getNume());
    }

    public void printAll(){
        System.out.println(Arrays.toString(angajati));
    }

    public void listbySalary(){
        Angajat[] copie = angajati;
        Arrays.sort(copie);
        System.out.println(Arrays.toString(copie));
    }

    public void findByDepartament(String numeDept){
        boolean ok = false;
        for(Angajat angajat : angajati){
            if(angajat.getDepartament().nume().equalsIgnoreCase(numeDept)){
                ok = true;
                System.out.println(angajat);
            }
        }
        if(!ok)
            System.out.println("Niciun angajat în departamentul: " + numeDept);

        logAction("FIND_BY_DEPT", numeDept);
    }

    public void printAuditLog(){
        for(AuditEntry a: auditLog){
            System.out.println(a);
        }
    }
}
