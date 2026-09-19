package org.example.model;

public class User {

    private long id;
    private String fullName;
    private String phoneNumber;
    private int pin;

    // default constructor
    public User(){
    }
    public User(long id, String fullName, String phoneNumber, int pin){
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
    }

    // setter
    public void setId(long id){
        this.id = id;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setPin(int pin){
        this.pin = pin;
    }
    // getter
    public long getId(){
        return id;
    }
    public String getFullName(){
        return fullName;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public int getPin(){
        return pin;
    }

}
