# Aplicație Bancară — Java OOP

## 1.1 — Acțiuni / interogări posibile în sistem

1. **Adaugă un client nou** — înregistrează un client în sistem cu CNP, nume și email
2. **Deschide un cont bancar** — creează un cont curent sau de economii pentru un client
3. **Depune numerar într-un cont** — actualizează soldul contului cu suma depusă
4. **Retrage numerar dintr-un cont** — scade suma din sold, cu validare fonduri insuficiente
5. **Transferă bani între două conturi** — debitează contul sursă și creditează contul destinație
6. **Emite un card bancar** — generează un card debit sau credit asociat unui cont
7. **Utilizează creditul unui card** — scade din limita disponibilă, cu validare limită depășită
8. **Generează un extras de cont** — afișează toate tranzacțiile dintr-o perioadă pentru un cont
9. **Abonează un cont la un serviciu bancar** — asociază un produs (Internet Banking, SMS Alert) unui cont
10. **Caută un client după CNP** — returnează datele clientului din sistem
11. **Listează toate conturile unui client** — afișează conturile cu soldurile și dobânzile aferente
12. **Sortează tranzacțiile unui cont după dată** — ordonează istoricul de tranzacții cronologic
13. **Blochează un card** — dezactivează un card din sistem
14. **Listează toți clienții din sistem** — afișează clienții sortați după CNP
15. **Șterge un client din sistem** — elimină clientul și datele asociate

---

## 1.2 — Tipuri de obiecte din domeniu

| Clasă | Descriere |
|---|---|
| `Cont` | Clasă abstractă — baza ierarhiei de conturi |
| `ContCurent` | Cont cu limită de descoperit și comision lunar |
| `ContEconomii` | Cont cu rată de dobândă și perioadă minimă |
| `Card` | Clasă abstractă — baza ierarhiei de carduri |
| `CardDebit` | Card legat direct de soldul contului |
| `CardCredit` | Card cu limită de credit și sold utilizat |
| `Client` | Persoana titulară de conturi și carduri |
| `Tranzactie` | Înregistrare imutabilă a unei operațiuni financiare |
| `ExtrasDecont` | Colecție de tranzacții pentru o perioadă dată |
| `ServiciuBancar` | Produs bancar abonat pe un cont (Internet Banking, SMS Alert etc.) |

---

## Structura proiectului

```
src/
├── Main.java
├── Model/
│   ├── Client.java
│   ├── ExtrasDecont.java
│   ├── ServiciuBancar.java
│   ├── IbanRecord.java
│   ├── Tranzactie/
│       ├── Tranzactie.java            
│       ├── TipTranzactie.java    
│   ├── TipServiciu.java       
│   ├── Cont/
│   │   ├── Cont.java            
│   │   ├── ContCurent.java
│   │   └── ContEconomii.java
│   └── Card/
│       ├── Card.java            
│       ├── CardDebit.java
│       └── CardCredit.java
├── Service/
│   ├── BancaService.java         
│   └── CardService.java          
└── Exceptii/
    ├── FonduriInsuficienteException.java
    └── LimitaCreditDepasitaException.java
