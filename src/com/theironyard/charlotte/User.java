package com.theironyard.charlotte;

/**
 * Created by souporman on 3/9/17.
 */
public class User {
    int id;
    String userName;
    String password;
    String address;
    String state;
    int zipCode;
    String emailAddress;

    public User(String userName, String password, String address, String state, int zipCode, String emailAddress) {
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.state = state;
        this.zipCode = zipCode;
        this.emailAddress = emailAddress;
    }

    public User(int id, String userName, String password, String address, String state, int zipCode, String emailAddress) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.state = state;
        this.zipCode = zipCode;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
