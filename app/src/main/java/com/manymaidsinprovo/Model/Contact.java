package com.manymaidsinprovo.Model;

import java.io.Serializable;

public class Contact implements Serializable {

    private int contactId;
    private String phone;
    private String countryCode;
    private int addressId;
    private int userId;

    public Contact() {
    }

    public Contact(int contactId, String phone, String countryCode, int addressId, int userId) {
        this.contactId = contactId;
        this.phone = phone;
        this.countryCode = countryCode;
        this.addressId = addressId;
        this.userId = userId;
    }

    public Contact(int contactId, String phone, String countryCode) {
        this.contactId = contactId;
        this.phone = phone;
        this.countryCode = countryCode;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
