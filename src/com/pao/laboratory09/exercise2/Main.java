package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data tip)
        // 2. Scrie toate înregistrările în OUTPUT_FILE cu DataOutputStream (format binar, RECORD_SIZE=32 bytes/înreg.)
        //    - bytes 0-3:   id (int, little-endian via ByteBuffer)
        //    - bytes 4-11:  suma (double, little-endian via ByteBuffer)
        //    - bytes 12-21: data (String, 10 chars ASCII, paddat cu spații la dreapta)
        //    - byte 22:     tip (0=CREDIT, 1=DEBIT)
        //    - byte 23:     status (0=PENDING, 1=PROCESSED, 2=REJECTED)
        //    - bytes 24-31: padding (zerouri)
        // 3. Procesează comenzile din stdin până la EOF cu RandomAccessFile:
        //    - READ idx       → seek(idx * RECORD_SIZE), citește și afișează înregistrarea
        //    - UPDATE idx ST  → seek(idx * RECORD_SIZE + 23), scrie noul status (0/1/2)
        //                       afișează "Updated [idx]: STATUS"
        //    - PRINT_ALL      → citește și afișează toate înregistrările
        //
        // Format linie output:
        //   [idx] id=<id> data=<data> tip=<CREDIT|DEBIT> suma=<suma:.2f> RON status=<STATUS>

        Scanner scanner = new Scanner(System.in);
        try {
            if (!scanner.hasNextInt()) return;
            int n = scanner.nextInt();

            try (FileOutputStream fos = new FileOutputStream(OUTPUT_FILE);
                 DataOutputStream dos = new DataOutputStream(fos)) {

                for (int i = 0; i < n; i++) {
                    int id = scanner.nextInt();
                    double suma = scanner.nextDouble();
                    String data = scanner.next();
                    TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                    dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(id).array());
                    dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(suma).array());

                    String dataPadded = String.format("%-10s", data);
                    dos.write(dataPadded.getBytes());

                    dos.write(tip == TipTranzactie.CREDIT ? 0 : 1);
                    dos.write(0);
                    dos.write(new byte[8]);
                }
            }

            try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
                while (scanner.hasNext()) {
                    String command = scanner.next();
                    if (command.equals("READ")) {
                        int idx = scanner.nextInt();
                        readAndPrint(raf, idx);
                    } else if (command.equals("UPDATE")) {
                        int idx = scanner.nextInt();
                        Status newStatus = Status.valueOf(scanner.next());
                        raf.seek((long) idx * RECORD_SIZE + 23);
                        raf.write(newStatus.value);
                        System.out.println("Updated [" + idx + "]: " + newStatus);
                    } else if (command.equals("PRINT_ALL")) {
                        long totalRecords = raf.length() / RECORD_SIZE;
                        for (int i = 0; i < totalRecords; i++) {
                            readAndPrint(raf, i);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readAndPrint(RandomAccessFile raf, int idx) throws IOException {
        byte[] buffer = new byte[RECORD_SIZE];
        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(buffer);

        ByteBuffer bb = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN);
        int id = bb.getInt(0);
        double suma = bb.getDouble(4);
        String data = new String(buffer, 12, 10).trim();
        TipTranzactie tip = (buffer[22] == 0) ? TipTranzactie.CREDIT : TipTranzactie.DEBIT;
        Status status = Status.fromInt(buffer[23]);

        System.out.printf("[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s\n",
                idx, id, data, tip, suma, status);
    }
}