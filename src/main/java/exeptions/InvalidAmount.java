package exeptions;

public class InvalidAmount extends Exception {
    public InvalidAmount() {
        super();
    }

    public InvalidAmount(String message) {
        super(message);
    }
}
