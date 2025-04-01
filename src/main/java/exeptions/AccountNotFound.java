package exeptions;

public class AccountNotFound extends Exception {
    public AccountNotFound() {
        super();
    }

    public AccountNotFound(String message) {
        super(message);
    }


}
