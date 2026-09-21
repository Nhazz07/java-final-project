package org.example.service;
import org.example.dao.TransactionDao;
import org.example.dao.AccountDao;
import org.example.model.Account;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {
    private final AccountDao account_dao;
    private final TransactionDao transaction_dao;
    // Constructor

    public TransactionService(AccountDao accountDao, TransactionDao transactionDao) {
        this.account_dao = accountDao;
        this.transaction_dao = transactionDao;
    }

    // Transfer Function
    public boolean transfer(long accountId, BigDecimal amount, String relatedAcc) {
        // Check amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Transfer amount must be greater than 0.");
            return false;
        }
        // check if receiver account is real or valid
        if (relatedAcc == null || relatedAcc.isBlank()) {
            System.out.println("Need a valid receiver account");
            return false;
        }
        // Find the desired Sender account
        Account sender_acc = account_dao.findById(accountid);
        if (sender_acc == null) {
            System.out.println("Sender is invalid account");
            return false;
        }

        Account receiver_acc = account_dao.findAccountByNumber(related_Acc);
        if (receiver_acc == null) {
            System.out.println("Receiver account not found");
            return false;
        }
        // Function to stop resend to same account
        if (sender_acc.getId() == receiver_acc.getId()) {
            System.out.println("Cannot send money to same Account");
            return false;
        }
        // check sender account
        if (sender_acc.getBalance().compareTo(amount) < 0){
            System.out.println("Invalid amount");
            return false;
        }
        // Calculate new balance amount
        BigDecimal sender_acc_newBalance = sender_acc.getBalance().subtract(amount);
        BigDecimal receiver_acc_newBalance = receiver_acc.getBalance().add(amount);
        // Update Sender Balance
        boolean sender_acc_updated = account_dao.updateBalance(sender_acc.getId(), sender_acc_newBalance);
        // check if sender_acc  is eligible to be update balance
        if(!sender_acc_updated){
            System.out.println("Error.");
            return false;
        }
        //update receiver balance
        boolean receiver_acc_updated = account_dao.updateBalance(receiver_acc.getId(), receiver_acc_newBalance);
        // check if receiver_acc is eligible to be update balance
        if(!receiver_acc_updated){
            System.out.println("Error");
            return false;
        }

        // Transaction Record
        Transaction transactionRecord = new Transaction();
        transactionRecord.setAccountId(sender_acc.getId());
        transactionRecord.setType("TRANSFER");
        transactionRecord.setAmount(amount);
        transactionRecord.setRelatedAccount(receiver_acc.getAccountNumber());
        // save those transaction record
        boolean savedTransactionRecord = transaction_dao.save(transactionRecord);
        // if fail
        if(!savedTransactionRecord){
            System.out.println("Transfer Unsuccessful");
            return false;
        }
        // if it went successful
        System.out.println("Transfer Successful");
        return true;
    }

    // GetTransaction History
    public List<Transaction> getTransactionHistory(long accountId){
        return transaction_dao.findByAccountId(accountId);
    }
}
