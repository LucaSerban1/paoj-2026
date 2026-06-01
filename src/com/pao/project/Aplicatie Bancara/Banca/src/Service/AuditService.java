package Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

public class AuditService {

    private static volatile AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            synchronized (AuditService.class) {
                if (instance == null) {
                    instance = new AuditService();
                }
            }
        }
        return instance;
    }

    public void log(String numeActiune) {
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.write(numeActiune + "," + LocalDateTime.now());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Eroare la scrierea in audit: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
