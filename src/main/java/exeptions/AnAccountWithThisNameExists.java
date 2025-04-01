package exeptions;

public class AnAccountWithThisNameExists extends Exception {
    public AnAccountWithThisNameExists(String message) {
        super(message);
    }
}
