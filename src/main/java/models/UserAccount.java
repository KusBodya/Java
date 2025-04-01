package models;

import StaticCounter.CounterID;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class UserAccount implements Serializable {
    @Getter @Setter
    public String userName;

    private final String password;

    private final @Getter long accountId;

    private final @Getter Wallet wallet;


    public UserAccount(String userName, String password) {
        this.userName = userName;
        this.wallet = new Wallet();
        this.password = password;
        this.accountId = CounterID.SetId();
    }

    public boolean CheckPassword(String password) {
        return this.password.equals(password);
    }
}
