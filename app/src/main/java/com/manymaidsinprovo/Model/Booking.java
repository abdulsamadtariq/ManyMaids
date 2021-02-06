package com.manymaidsinprovo.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Booking {

    private int orderId;
    private int userId;
    private String orderTime;
    private double totalBill;
    private int paymentStatus;
    private String paymentMethod;
    private String orderStatus;
    @SerializedName("orderDetails")
    private ArrayList<BookingDetail> bookingDetailArrayList;

    public Booking() {

    }

    public Booking(int orderId, int userId, String orderTime, double totalBill, int paymentStatus, String paymentMethod, String orderStatus, ArrayList<BookingDetail> bookingDetailArrayList) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderTime = orderTime;
        this.totalBill = totalBill;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.bookingDetailArrayList = bookingDetailArrayList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ArrayList<BookingDetail> getBookingDetailArrayList() {
        return bookingDetailArrayList;
    }

    public void setBookingDetailArrayList(ArrayList<BookingDetail> bookingDetailArrayList) {
        this.bookingDetailArrayList = bookingDetailArrayList;
    }
}
