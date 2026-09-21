package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;

public class AuthService {
    private final UserDao userDao;

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
        User loginAcc = userDao.findByPhoneNumber(phoneNumber);

        // Check if the account have a valid number or correct number
        if (loginAcc == null) {
            System.out.println("Phone number not found.");
            return null;
        }

        // check if pin is good
        if (loginAcc.getPin() != pin) {
            System.out.println("Incorrect Pin.");
            return null;
        }

        // if they fit the requirement
        System.out.println("Login Successfully");
        return loginAcc;
    }
}
