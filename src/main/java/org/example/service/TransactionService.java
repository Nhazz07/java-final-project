package org.example.service;
import org.example.dao.TransactionDao;
import org.example.dao.AccountDao;
import org.example.model.Account;
import java.math.BigDecimal;

public class TransactionService {
    private final AccountDao account_dao;
    private final TransactionDao transaction_dao;
    // Constructor

    public TransactionService(AccountDao account_dao, TransactionDao transaction_dao) {
        this.account_dao = account_dao;
        this.transaction_dao = transaction_dao;
    }

    // Transfer Function
    public boolean transfer(long account_id, BigDecimal amount, String related_Acc) {
        // Check amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Transfer amount must be greater than 0.");
            return false;
        }
        // check if receiver account is real or valid
        if (related_Acc == null || related_Acc.isBlank()) {
            System.out.println("Need a valid receiver account");
            return false;
        }
        // Find the desired Sender account
        Account sender_acc = account_dao.findById(account_id);
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

    }
}
