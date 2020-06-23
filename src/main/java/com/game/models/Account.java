package com.game.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that represents a player account with 4 main attributes:
 * name, password, balance(represents in-game credits), and isAdmin.
 * Included getters and setters as well as additional behaviour
 * in which the balance is modified
 */
public class Account {
    private final String username;
    private String password;
    private final String email;
    private final List<String> friends;
    private String cardNumber;
    private	int balance;

    public Account(String username, String password, String email, String friends, int balance, String cardNumber) {
        this.friends = new ArrayList<>();
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.email = email;
        if (friends.equals("")){
            return;
        }
        String[] temp = friends.split(",");
        this.friends.addAll(Arrays.asList(temp));
    }

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        friends = new ArrayList<>();
    }

    public String getName() {
        return username;
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int deposit){
        balance+=deposit;
    }

    public void subtractBalance(int request){
        balance -= request;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public List<String> getFriends() {
        return friends;
    }
}
