package Exceptii;

public class LimitaCreditDepasitaException extends RuntimeException {
    public LimitaCreditDepasitaException(String message) {
        super(message);
    }
}
