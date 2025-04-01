package services;


import exeptions.AccountNotFound;
import exeptions.AnAccountWithThisNameExists;
import exeptions.UserInvalidName;
import lombok.Getter;
import lombok.Setter;
import models.UserAccount;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class BankService {

    private List<UserAccount> userAccountList;

    public BankService() {
        this.userAccountList = new ArrayList<>();
    }

    public UserAccount loginAccount(String userName, String password) throws AccountNotFound {
        for (UserAccount userAccount : userAccountList) {
            if (userAccount.getUserName().equals(userName) && userAccount.CheckPassword(password)) {
                return userAccount;
            }
        }
        return null;
    }

    public UserAccount createAccount(String userName, String password) throws AnAccountWithThisNameExists {
        String regex = "^[a-zA-Z]{4,31}$"; // регулярка как для ника
        if (!userName.matches(regex)) {
            throw new UserInvalidName("Invalid user name");
        }
        for (UserAccount userAccount : userAccountList) {
            if (userAccount.getUserName().equals(userName)) {
                throw new AnAccountWithThisNameExists("User name already exists");
            }
        }

        UserAccount tmpAccount = new UserAccount(userName, password);
        userAccountList.add(tmpAccount);
        return tmpAccount;
    }
}