package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;

public class AuthService {
    private final UserDao userDao;

    // Login attempt variables
    private int failedAttempts = 0;
    private long blockedUntil = 0;

    // Constructor
    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    // Register user
    public boolean registerService(String fullName, String phoneNumber, int pin) {
        if (userDao.existByPhoneNumber(phoneNumber)) {
            System.out.println("Phone Number is already been in used");
            return false;
        }

        // Create user Function
        User user = new User();
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setPin(pin);

        // save to user regi info to database
        boolean savedInfo = userDao.save(user);

        if (savedInfo) {
            System.out.println("Successfully Registered Account");
            return true;
        }

        System.out.println("Unable to Registered Account, please try again.");
        return false;
    }

    // for login by using phone number and pin
    public User userLogin(String phoneNumber, int pin) {

        // Check if login is currently blocked
        if (System.currentTimeMillis() < blockedUntil) {

            long remaining =
                    (blockedUntil - System.currentTimeMillis()) / 1000;

            System.out.println(
                    "Login is blocked. Please wait "
                            + (remaining + 1)
                            + " seconds."
            );

            return null;
        }

        User loginAcc = userDao.findByPhoneNumber(phoneNumber);

        // Phone number not found
        if (loginAcc == null) {

            failedAttempts++;

            System.out.println("Phone number not found.");
            checkLoginAttempts();

            return null;
        }

        // Incorrect PIN
        if (loginAcc.getPin() != pin) {

            failedAttempts++;

            System.out.println("Incorrect Pin.");
            checkLoginAttempts();

            return null;
        }

        // Login successful
        failedAttempts = 0;

        System.out.println("Login Successfully");

        return loginAcc;
    }

    private void checkLoginAttempts() {

        System.out.println(
                "Failed attempts: " + failedAttempts + "/3"
        );

        if (failedAttempts >= 3) {

            blockedUntil =
                    System.currentTimeMillis() + 10_000;

            System.out.println(
                    "Too many failed login attempts."
            );

            System.out.println(
                    "Login blocked for 10 seconds."
            );
        }
    }
    }

