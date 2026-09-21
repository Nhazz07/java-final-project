package org.example;

import org.example.dao.UserDao;
import org.example.service.AuthService;

import java.util.Scanner;

public class Main {
     static Scanner input = new Scanner(System.in);
     static UserDao userDao = new UserDao();
     static AuthService authService = new AuthService(userDao);
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
        System.out.println("===== LOGIN =====");
        System.out.print("Enter phone number: ");
        String phoneNumber = input.next();
        System.out.print("Enter pin: ");
        int pin = input.nextInt();

        authService.userLogin(phoneNumber, pin);
    }
}