package exeptions;

public class AccountNotCreated extends Exception {
    public AccountNotCreated() {
        super();
    }

    public AccountNotCreated(String message) {
        super(message);
    }
}
