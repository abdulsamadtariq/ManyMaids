package com.manymaidsinprovo.Model;

import java.io.Serializable;

public class CleaningType  implements Serializable {

    private int CleaningTypeId;
    private int NumberOfCleaner;
    private String CategoryName;
    private double CategoryPricePerCleaning;
    private double discount;
    private String hours;
    private float rating;
    private int catPriority;
    private boolean isSelected;

    public CleaningType() {
    }

    public CleaningType(int cleaningTypeId, int numberOfCleaner, String categoryName, double categoryPricePerCleaning, double discount, String hours, float rating, int catPriority, boolean isSelected) {
        CleaningTypeId = cleaningTypeId;
        NumberOfCleaner = numberOfCleaner;
        CategoryName = categoryName;
        CategoryPricePerCleaning = categoryPricePerCleaning;
        this.discount = discount;
        this.hours = hours;
        this.rating = rating;
        this.catPriority = catPriority;
        this.isSelected = isSelected;
    }

    public CleaningType(int cleaningTypeId, int numberOfCleaner, String categoryName, double categoryPricePerCleaning, double discount, String hours, float rating) {
        CleaningTypeId = cleaningTypeId;
        NumberOfCleaner = numberOfCleaner;
        CategoryName = categoryName;
        CategoryPricePerCleaning = categoryPricePerCleaning;
        this.discount = discount;
        this.hours = hours;
        this.rating = rating;
    }

    public int getCatPriority() {
        return catPriority;
    }

    public void setCatPriority(int catPriority) {
        this.catPriority = catPriority;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getCleaningTypeId() {
        return CleaningTypeId;
    }

    public void setCleaningTypeId(int cleaningTypeId) {
        CleaningTypeId = cleaningTypeId;
    }

    public int getNumberOfCleaner() {
        return NumberOfCleaner;
    }

    public void setNumberOfCleaner(int numberOfCleaner) {
        NumberOfCleaner = numberOfCleaner;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public double getCategoryPricePerCleaning() {
        return CategoryPricePerCleaning;
    }

    public void setCategoryPricePerCleaning(double categoryPricePerCleaning) {
        CategoryPricePerCleaning = categoryPricePerCleaning;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
