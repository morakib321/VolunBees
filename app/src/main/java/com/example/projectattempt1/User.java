package com.example.projectattempt1;

public class User {
    protected int userID;
    protected String name;
    protected int age;
    protected String gender;
    protected int phoneNumber;
    protected String username;
    protected String password;

    public User(int userID, String name, int age, String gender, int phoneNumber, String username, String password) {
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
