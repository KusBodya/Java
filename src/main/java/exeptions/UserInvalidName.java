package exeptions;

public class UserInvalidName extends RuntimeException {
    public UserInvalidName(String message) {
        super(message);
    }
}
