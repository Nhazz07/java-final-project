package org.example.service;

import org.example.dao.TransactionDao;
import org.example.dao.AccountDao;
import org.example.model.Account;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    // Constructor
    public TransactionService(AccountDao accountDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
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
        Account senderAcc = accountDao.findById(accountId);
        if (senderAcc == null) {
            System.out.println("Sender is invalid account");
            return false;
        }

        Account receiverAcc = accountDao.findAccountByNumber(relatedAcc);
        if (receiverAcc == null) {
            System.out.println("Receiver account not found");
            return false;
        }

        // Function to stop resend to same account
        if (senderAcc.getId() == receiverAcc.getId()) {
            System.out.println("Cannot send money to same Account");
            return false;
        }

        // check sender account
        if (senderAcc.getBalance().compareTo(amount) < 0) {
            System.out.println("Invalid amount");
            return false;
        }

        // Calculate new balance amount
        BigDecimal senderAccNewBalance =
                senderAcc.getBalance().subtract(amount);

        BigDecimal receiverAccNewBalance =
                receiverAcc.getBalance().add(amount);

        // Update Sender Balance
        boolean senderAccUpdated =
                accountDao.updateBalance(
                        senderAcc.getId(),
                        senderAccNewBalance
                );

        // check if senderAcc is eligible to be update balance
        if (!senderAccUpdated) {
            System.out.println("Error.");
            return false;
        }

        // update receiver balance
        boolean receiverAccUpdated =
                accountDao.updateBalance(
                        receiverAcc.getId(),
                        receiverAccNewBalance
                );

        // check if receiverAcc is eligible to be update balance
        if (!receiverAccUpdated) {
            System.out.println("Error");
            return false;
        }

        // Transaction Record
        Transaction transactionRecord = new Transaction();
        transactionRecord.setAccountId(senderAcc.getId());
        transactionRecord.setType("TRANSFER");
        transactionRecord.setAmount(amount);
        transactionRecord.setRelatedAccount(receiverAcc.getAccountNumber());

        // save those transaction record
        boolean savedTransactionRecord =
                transactionDao.save(transactionRecord);

        // if fail
        if (!savedTransactionRecord) {
            System.out.println("Transfer Unsuccessful");
            return false;
        }

        // if it went successful
        System.out.println("Transfer Successful");
        return true;
    }

    // GetTransaction History
    public List<Transaction> getTransactionHistory(long accountId) {
        return transactionDao.findByAccountId(accountId);
    }
}
