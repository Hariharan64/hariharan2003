package com.example.qrstaff;





public class User {
    private String name;
    private String id;
    private String designation;
    private String phoneNumber;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String id, String designation, String phoneNumber) {
        this.name = name;
        this.id = id;
        this.designation = designation;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}