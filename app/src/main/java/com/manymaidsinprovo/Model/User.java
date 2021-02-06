package com.manymaidsinprovo.Model;

import java.util.ArrayList;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String imageLink;
    private String lastUpdated;
    private int userRole;
    private int userStatus;
    private String authToken;
    private ArrayList<Address> addressList;


    public User() {
    }

    public User(int userId, String firstName, String lastName, String email, String imageLink, String lastUpdated, int userRole, int userStatus, String authToken, ArrayList<Address> addressList) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageLink = imageLink;
        this.lastUpdated = lastUpdated;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.authToken = authToken;
        this.addressList = addressList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ArrayList<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(ArrayList<Address> addressList) {
        this.addressList = addressList;
    }
}
