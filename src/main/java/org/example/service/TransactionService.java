package org.example.service;

import org.example.dao.TransactionDao;
import org.example.model.Transaction;

import org.example.dao.AccountDao;
import org.example.model.Account;

import java.math.BigDecimal;
import java.util.List;
public class TransactionService {
    private final AccountDao account_dao;
    private final TransactionDao transaction_dao;
    // Constructor

    public TransactionService(AccountDao account_dao, TransactionDao transaction_dao) {
        this.account_dao = account_dao;
        this.transaction_dao = transaction_dao;
    }

    // Tranfer Function
    public boolean transfer(long account_id, BigDecimal amount, String related_Acc) {
        // Check amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Transfer amount must be greater than 0.");
            return false;
        }
        // check if reciever account is real or valid
        if (related_Acc == null || related_Acc.isBlank()) {
            System.out.println("Need a valid reciever account");
            return false;
        }
        // Find the desired Sender account
        Account sender_acc = account_dao.findById(account_id);
        if (sender_acc == null) {
            System.out.println("Sender is invalid account");
            return false;
        }
    }
}
