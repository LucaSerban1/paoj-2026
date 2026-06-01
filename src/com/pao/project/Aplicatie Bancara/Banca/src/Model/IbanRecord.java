package Model;

import java.time.LocalDate;


public record IbanRecord(String iban, String idClient, LocalDate dataDeschidere) {
}