package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private long accountId;
    private String type;
    private BigDecimal amount;
    private String relatedAccount;
    private LocalDateTime createdAt;

    public Transaction(){

    }

    public Transaction(long id, long accountId, String type,
                       BigDecimal amount, String relatedAccount,
                       LocalDateTime createdAt){
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.relatedAccount = relatedAccount;
        this.createdAt = createdAt;
    }

    // setter
    public void setId(long id){
        this.id = id;
    }
    public void setAccountId(long accountId){
        this.accountId = accountId;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public void setRelatedAccount(String relatedAccount){
        this.relatedAccount = relatedAccount;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

    // getter
    public long getId(){
        return id;
    }
    public long getAccountId(){
        return accountId;
    }
    public String getType(){
        return type;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public String getRelatedAccount(){
        return relatedAccount;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", relatedAccount='" + relatedAccount + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
