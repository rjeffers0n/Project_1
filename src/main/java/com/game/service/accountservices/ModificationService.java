package com.game.service.accountservices;

public interface ModificationService {
    boolean deposit(int amount, String user);
    boolean withdraw(int amount, String user);
    boolean changePassword(String password, String user);
    boolean changeBankAccount(String bankAccount, String user);

    boolean validCard(String bankAccount);
}
