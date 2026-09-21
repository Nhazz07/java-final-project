package org.example.service;

import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.model.Account;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private final AccountDao accountDao;
    private final TransactionDao transactionDao;
    public AccountService(AccountDao accountDao, TransactionDao transactionDao ) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }


    // 1. Create a new account
    public boolean createAccount(Account account) {
        return accountDao.createAccount(account);
    }

    // 2. Get all accounts belonging to a user
    public List<Account> getUserAccount(long userId) {
        return accountDao.findByUserId(userId);
    }

    // 3. Get one account by account ID
    public Account getAccount(long accountId) {
        return accountDao.findById(accountId);
    }

    // 4. Get account balance
    public BigDecimal getBalance(long accountId) {
        Account account = accountDao.findById(accountId);
        if (account == null) {
            return null;
        }
        return account.getBalance();
    }

    // 5. Deposit money
    public boolean deposit(long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        Account account = accountDao.findById(accountId);
        if (account == null) {
            return false;
        }
        BigDecimal newBalance = account.getBalance().add(amount);

        boolean balanceUpdated = accountDao.updateBalance(accountId, newBalance);

        if(!balanceUpdated) {
            return false;
        }

        // create deposit transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setRelatedAccount(null);

        boolean transactionSaved = transactionDao.save(transaction);

        if(!transactionSaved){
            System.out.println("Warning: Deposit completed but transaction not recorded");
        }
        return true;
    }

    // 6. Withdraw money
    public boolean withdraw(long accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        Account account = accountDao.findById(accountId);
        if (account == null) {
            return false;
        }
        BigDecimal currentBalance = account.getBalance();

        // Not enough money
        if (currentBalance.compareTo(amount) < 0) {
            return false;
        }
        BigDecimal newBalance = currentBalance.subtract(amount);

        boolean balanceUpdated = accountDao.updateBalance(accountId,newBalance);

        if(!balanceUpdated){
            return false;
        }

        // create withdrawal transaction
        Transaction transaction = new Transaction();

        transaction.setAccountId(accountId);
        transaction.setType("WITHDRAW");
        transaction.setAmount(amount);
        transaction.setRelatedAccount(null);

        boolean transactionSaved = transactionDao.save(transaction);

        if(!transactionSaved){
            System.out.println("Warning: Withdrawal completed but transaction was not recorded");
        }
        return true;
    }
}
