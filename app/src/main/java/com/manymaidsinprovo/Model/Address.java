package com.manymaidsinprovo.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Address implements Serializable {

    private int addressId;
    private String addressTag;
    private String addressCaption;
    private String buildingNo;
    private String streetAddress;
    private String city;
    private String zipcode;
    private String country;
    private int userId;
    private ArrayList<Contact> contact;
    private boolean isSelected;

    public Address() {
    }

    public Address(int addressId, String addressTag, String addressCaption, String buildingNo, String streetAddress, String city, String zipcode, String country, int userId, ArrayList<Contact> contactList, boolean isSelected) {
        this.addressId = addressId;
        this.addressTag = addressTag;
        this.addressCaption = addressCaption;
        this.buildingNo = buildingNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.userId = userId;
        this.contact = contactList;
        this.isSelected = isSelected;
    }

    public Address(int addressId, String addressTag, String addressCaption, String buildingNo, String streetAddress, String city, String zipcode, String country, ArrayList<Contact> contactList) {
        this.addressId = addressId;
        this.addressTag = addressTag;
        this.addressCaption = addressCaption;
        this.buildingNo = buildingNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.userId = userId;
        this.contact = contactList;
        this.isSelected = isSelected;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Address(int addressId, String addressTag, String addressCaption, String buildingNo, String streetAddress, String city, String zipcode, String country, int userId) {
        this.addressId = addressId;
        this.addressTag = addressTag;
        this.addressCaption = addressCaption;
        this.buildingNo = buildingNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
        this.userId = userId;
    }

    public Address(String addressTag, String addressCaption, String buildingNo, String streetAddress, String city, String zipcode, String country) {
        this.addressTag = addressTag;
        this.addressCaption = addressCaption;
        this.buildingNo = buildingNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipcode = zipcode;
        this.country = country;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressTag() {
        return addressTag;
    }

    public void setAddressTag(String addressTag) {
        this.addressTag = addressTag;
    }

    public String getAddressCaption() {
        return addressCaption;
    }

    public void setAddressCaption(String addressCaption) {
        this.addressCaption = addressCaption;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Contact> getContactList() {
        return contact;
    }

    public void setContactList(ArrayList<Contact> contactList) {
        this.contact = contactList;
    }
}
