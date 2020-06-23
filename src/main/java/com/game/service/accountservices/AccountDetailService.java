package com.game.service.accountservices;

import com.game.models.Account;
import java.util.List;

public interface AccountDetailService {
    boolean checkExist(String username);
    Account findByID(String username);
    boolean checkCredentials(String username, String password);
    Account getAccount(String username);
    List<String> getAccountList();
    boolean addAccount(String username, String password, String email);
    boolean removeAccount(String username);
    void update(Account obj);
    void logOff(String username);
    boolean usernameValidations(String username);
    boolean passwordValidations(String password);
}
