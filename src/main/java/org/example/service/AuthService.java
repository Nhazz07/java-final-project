package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;

public class AuthService {
    private final UserDao user_dao;

    // Constructor
    public AuthService(UserDao user_dao) {
        this.user_dao = user_dao;
    }

    // Register user
    public boolean register_service(String fullName,String phone_number,int pin){
        if(user_dao.existByPhoneNumber(phone_number)){
            System.out.println("Phone Number is already been in used");
            return false;
        }

        // Create user Function
        User user = new User();
        user.setFullName(fullName);
        user.setPhoneNumber(phone_number);
        user.setPin(pin);
        // save to user regi info to database
        boolean saved_info = user_dao.save(user);
        if(saved_info){
            System.out.println("Successfully Registered Account");
            return true;
        }
        System.out.println("Unable to Registered Account, please try again.");
        return false;
    }

    // for login by using phone number and pin
    public User user_login(String phone_number,int pin) {
        User login_acc = user_dao.findByPhoneNumber(phone_number);
        // Check if the account have a valid number or correct number
        if(login_acc == null){
            System.out.println("Phone number not found.");
            return null;
        }
        // check if pin is good
        if(login_acc.getPin() != pin){
            System.out.println("Incorrect Pin.");
            return null;
        }
        //if they fit the requirement
        System.out.println("Login Successfully");
        return login_acc;
    }
}
