package exeptions;

public class InsufficientFunds extends Exception {
    public InsufficientFunds() {
        super();
    }

    public InsufficientFunds(String message) {
        super(message);
    }
}
