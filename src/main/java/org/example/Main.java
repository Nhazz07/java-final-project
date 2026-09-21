package org.example;

import org.example.dao.AccountDao;
import org.example.dao.TransactionDao;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.AuthService;
import org.example.service.TransactionService;

import java.util.Scanner;

public class Main {
     static Scanner input = new Scanner(System.in);
     static UserDao userDao = new UserDao();
     static AccountDao accountDao = new AccountDao();
     static TransactionDao transactionDao = new TransactionDao();
     static AuthService authService = new AuthService(userDao);
     static AccountService accountService = new AccountService();
     TransactionService transactionService = new TransactionService(accountDao, transactionDao);
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

                default -> {
                    System.out.println("Invalid choice.");
                }
            }
        }
    }

    private static void createAccount(User loggedInUser) {

    }

    private static void checkBalance(User loggedInUser) {
    }

    private static void deposit(User loggedInUser) {
    }

    private static void withdraw(User loggedInUser) {
    }

    private static void transfer(User loggedInUser) {
    }

    private static void transactionHistory(User loggedInUser) {
    }
}