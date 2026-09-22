package org.example;

import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.dao.UserDao;
import org.example.model.Account;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.AuthService;
import org.example.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
     static Scanner input = new Scanner(System.in);
     static UserDao userDao = new UserDao();
     static AccountDao accountDao = new AccountDao();
     static TransactionDao transactionDao = new TransactionDao();
     static AuthService authService = new AuthService(userDao);
     static AccountService accountService = new AccountService(accountDao,transactionDao);
     static TransactionService transactionService = new TransactionService(accountDao, transactionDao);
    public static void main(String[] args) {



        while(true){
            System.out.println("===== ATM MACHINE =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice your option: ");
            int option = input.nextInt();
            input.nextLine();

            switch (option){
                case 1 -> {
                    register();
                }
                case 2 -> {
                    login();
                }
                case 3 -> {
                    System.out.println("Thank you for using our ATM machine");
                    input.close();
                    return;
                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }
    }

    private static void register() {
        System.out.println("===== REGISTER =====");
        System.out.print("Enter your full name: ");
        String fullName = input.nextLine();
        System.out.print("Enter your phone number: ");
        String phoneNumber = input.next();
        System.out.print("Enter your pin: ");
        int pin = input.nextInt();

        authService.registerService(fullName, phoneNumber,pin);

    }
    private static void login() {
        while (true) {

            System.out.println("===== LOGIN =====");

            System.out.print("Enter phone number: ");
            String phoneNumber = input.next();

            System.out.print("Enter pin: ");
            int pin = input.nextInt();
            input.nextLine();

            User loggedInUser = authService.userLogin(phoneNumber, pin);

            // Login successful
            if (loggedInUser != null) {
                atmMenu(loggedInUser);
                return;
            }else{
                // Login failed
                System.out.println("Login failed. Please try again.");
            }

        }
    }

    private static void atmMenu(User loggedInUser) {

        while (true) {

            System.out.println("\n===== WELCOME TO OUR ATM MENU =====");
            System.out.println("Welcome, " + loggedInUser.getFullName());
            System.out.println("1. Create Account");
            System.out.println("2. Check Balance");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Transaction History");
            System.out.println("7. Logout");

            System.out.print("Choose your option: ");

            int option = input.nextInt();
            input.nextLine();

            switch (option) {

                case 1 -> {
                    createAccount(loggedInUser);
                }

                case 2 -> {
                    checkBalance(loggedInUser);
                }

                case 3 -> {
                    deposit(loggedInUser);
                }

                case 4 -> {
                    withdraw(loggedInUser);
                }

                case 5 -> {
                    transfer(loggedInUser);
                }

                case 6 -> {
                    transactionHistory(loggedInUser);
                }
                case 7 -> {
                    System.out.println("Logged out");
                    return;
                }

                default -> {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static void createAccount(User loggedInUser) {
        System.out.println("===== CREATE ACCOUNT =====");
        System.out.print("Enter account number: ");
        String accountNumber = input.nextLine();
        System.out.print("Enter account type: ");
        String accountType = input.nextLine();
        System.out.print("Enter initial deposit: ");
        BigDecimal balance = input.nextBigDecimal();
        input.nextLine();

        Account account = new Account();

        account.setAccountNumber(accountNumber);
        account.setUserId(loggedInUser.getId());
        account.setAccountType(accountType);
        account.setBalance(balance);

        boolean created = accountService.createAccount(account);

        if(created){
            System.out.println("Account created successfully!");
            System.out.println("Your Account ID: " + account.getId());
            System.out.println("Your Account Number: " + account.getAccountNumber());
        }else{
            System.out.println("Failed to create account");
        }
    }

    private static void checkBalance(User loggedInUser) {
        List<Account> accounts = accountService.getUserAccount(loggedInUser.getId());

        if(accounts.isEmpty()){
            System.out.println("You don't have any bank account");
            return;
        }

        for(Account acc : accounts){
            System.out.println("Account Number: " + acc.getAccountNumber());
            System.out.println("Account type: " + acc.getAccountType());
            System.out.println("Balance: $" + acc.getBalance());
            System.out.println("-------------------------------");
        }
    }

    private static void deposit(User loggedInUser) {
        List<Account> accounts = accountService.getUserAccount(loggedInUser.getId());

        if(accounts.isEmpty()){
            System.out.println("You don't have an account");
            return;
        }
        System.out.println("===== DEPOSIT =====");
        for(int i = 0; i < accounts.size(); i++){
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountNumber() + " - Balance: $" + account.getBalance());
        }
        System.out.print("Choose account");
        int choice = input.nextInt();

        if(choice < 1 || choice > accounts.size()){
            System.out.println("Invalid account");
            input.nextLine();
            return;
        }
        Account selectedAccount = accounts.get(choice - 1);

        System.out.print("Enter deposit amount: ");
        BigDecimal amount = input.nextBigDecimal();
        input.nextLine();

        boolean success = accountService.deposit(
                selectedAccount.getId(), amount
        );
        if(success){
            System.out.println("deposit successful");
        }else{
            System.out.println("deposit failed");
        }
    }

    private static void withdraw(User loggedInUser) {
        List<Account> accounts = accountService.getUserAccount(loggedInUser.getId());

        if(accounts.isEmpty()){
            System.out.println("You don't have any account");
            return;
        }
        System.out.println("===== WITHDRAW =====");
        for(int i = 0; i < accounts.size(); i++){
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountNumber() + "- Balance: $" + account.getBalance());
        }
        System.out.print("Enter your choice");
        int choice = input.nextInt();

        if(choice < 1 || choice > accounts.size()){
            System.out.println("Invalid account");
            input.nextLine();
            return;
        }
        Account selectedAccount = accounts.get(choice - 1);
        System.out.print("Enter withdraw amount");
        BigDecimal amount = input.nextBigDecimal();
        input.nextLine();

        boolean success = accountService.withdraw(selectedAccount.getId(), amount);
        if(success){
            System.out.println("Withdraw successful");
        }else{
            System.out.println("Withdraw failed");
        }
    }

    private static void transfer(User loggedInUser) {
        List<Account> accounts = accountService.getUserAccount(loggedInUser.getId());
        if(accounts.isEmpty()){
            System.out.println("You don't have any bank account");
            return;
        }
        System.out.println("===== TRANSFER =====");
        for(int i = 0; i < accounts.size(); i++){
Account account = accounts.get(i);

            System.out.println((i + 1) + ". " + " - Balance: $" + account.getBalance());
        }

        System.out.print("Choose Sender account: ");
        int choice = input.nextInt();
        input.nextLine();

        if(choice < 1 || choice > accounts.size()){
            System.out.println("Invalid amount");
            return;
        }

        Account senderAccount = accounts.get(choice - 1);

        System.out.print("Enter receiver account number: ");
        String receiverAccount = input.nextLine();


        System.out.println("Enter amount: ");
        BigDecimal amount = input.nextBigDecimal();
        input.nextLine();

        boolean success = transactionService.transfer(senderAccount.getId(), amount,receiverAccount);
        if(success){
            System.out.println("Transfer completed.");
        }else{
            System.out.println("Transfer failed.");
        }
    }

    private static void transactionHistory(User loggedInUser) {

        List<Account> accounts =
                accountService.getUserAccount(loggedInUser.getId());

        if (accounts.isEmpty()) {
            System.out.println("You don't have any bank account.");
            return;
        }

        System.out.println("\n===== TRANSACTION HISTORY =====");

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);

            System.out.println(
                    (i + 1) + ". " +
                            account.getAccountNumber() +
                            " - " +
                            account.getAccountType()
            );
        }

        System.out.print("Choose account: ");
        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Invalid account.");
            return;
        }

        Account selectedAccount = accounts.get(choice - 1);

        List<Transaction> transactions =
                transactionService.getTransactionHistory(
                        selectedAccount.getId()
                );

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n===== TRANSACTIONS =====");
        System.out.println("Account: " + selectedAccount.getAccountNumber());
        System.out.println("--------------------------------");

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println("--------------------------------");
    }
}