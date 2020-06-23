package com.game.service.accountservices;
import com.game.data.MessageSQLRepo;
import com.game.data.Repository;
import com.game.models.Account;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * main service of the application that deals with manipulating account objects
 */
public class AccountDetailServiceImp implements AccountDetailService {
    static final Logger logger = Logger.getLogger(MessageSQLRepo.class);
    List<String> accountList;
    Map<String, Account> accountMap;
    Repository<Account,String> arepo;

    /**
     * Initializes the account repository
     * creates a new hashmap to store instantiated accounts
     * Also creates a list of IDs that exist in the account
     * @param accountSQLRepo account repository
     */
    public AccountDetailServiceImp(Repository<Account, String> accountSQLRepo){
        BasicConfigurator.configure();
        arepo = accountSQLRepo;
        accountList = arepo.findAllID();
        accountMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Checks if passed in user exists in the accountList
     * @param username session user
     * @return true if username exists in repo
     */
    @Override
    public boolean checkExist(String username) {
        return accountList.contains(username);
    }

    /**
     * Finds account of logged in user within the hashmap
     * @param username session user and acts as key
     * @return session user account
     */
    @Override
    public Account findByID(String username) {
        return arepo.findById(username);
    }

    /**
     * instantiates user account on successful login and
     * puts it in the map
     * @param username session user
     * @param password session user password
     * @return true if user exists and password matches
     */
    @Override
    public boolean checkCredentials(String username, String password) {
        Account temp = findByID(username);
        if (temp.getPassword().equals(password)){
            accountMap.put(username,temp);
            logger.debug("credentials not valid");
            return true;
        }
        return false;
    }

    /**
     * Gets an account by ID from users currently logged in
     * by retrieving account from map
     * @param username session user
     * @return session user account
     */
    @Override
    public Account getAccount(String username) {
        if (accountMap.containsKey(username)) {
            return accountMap.get(username);
        }
        logger.debug("account not found");
        return null;
    }

    /**
     * Mainly used for testing/debugging purposes
     * Retrieves list of usernames
     * @return ArrayList of usernames
     */
    @Override
    public List<String> getAccountList() {
        return accountList;
    }

    /**
     * does account validations and creates a new account object
     * to push into map
     * called in CreationService
     * @param username desired username
     * @param password desired password
     * @param email chosen email
     * @return true if account passes validations and was created
     */
    @Override
    public boolean addAccount(String username, String password, String email) {
        if (usernameValidations(username)&&passwordValidations(password)){
            Account temp = new Account(username, password, email);
            accountList.add(username);
            arepo.save(temp);
            return true;
        }
        logger.debug("account not added");
        return false;
    }

    /**
     * Admin level method will allow removal of other users'
     * accounts while standard users could use it to close
     * theirs. Called in creation service
     * @param username username
     * @return true if removal succeeds and is valid
     */
    @Override
    public boolean removeAccount(String username) {
        if (username.equals("admin")){
            logger.debug("cannot delete admin account");
            return false;
        }
        accountList.remove(username);
        accountMap.remove(username);
        arepo.delete(username);
        return true;
    }

    /**
     * updates given account
     * In the future, would pass in id
     * and get the Account from map reducing
     * how convoluted this is
     * @param obj session account
     */
    @Override
    public void update(Account obj) {
        arepo.update(obj,obj.getName());
    }

    /**
     * removes instantiated account from map
     * @param username session user
     */
    @Override
    public void logOff(String username) {
        accountMap.remove(username);
    }

    /**
     * Checks if username is valid
     * @param username desired username
     * @return true if valid
     */
    @Override
    public boolean usernameValidations(String username) {
        return !username.equals("") || username.matches("^[a-zA-Z0-9]*$");
    }

    /**
     * checks if password is valid
     * @param password desired password
     * @return true if valid
     */
    @Override
    public boolean passwordValidations(String password) {
        if (password.length()<8){
            logger.debug("password length too short");
            return false;
        }
        char [] newPass = password.toCharArray();
        boolean numCond = false;
        boolean capCond = false;
        boolean undCond = false;
        for (char c:newPass) {
            if (Character.isDigit(c)){
                numCond = true;
            }
            if (Character.isUpperCase(c)){
                capCond = true;
            }
            if (Character.isLowerCase(c)){
                undCond = true;
            }
        }
        return numCond&&capCond&&undCond;
    }

}
