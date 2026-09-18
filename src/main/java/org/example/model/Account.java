package org.example.model;

import java.math.BigDecimal;

public class Account {

    private long id;
    private String accountNumber;
    private long userId;
    private BigDecimal balance;

    public Account(long id, String accountNumber,long userId, BigDecimal balance ){
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
    }

    public Account(String accountNumber, long userId, BigDecimal balance){
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
    }

    // setter
    public void setId(long id){
        this.id = id;
    }
    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }
    public void setUserId(){
        this.userId = userId;
    }
    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }

    // Getter
    public long getId(){
        return id;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public long getUserId(){
        return userId;
    }
    public BigDecimal getBalance(){
        return balance;
    }
}
