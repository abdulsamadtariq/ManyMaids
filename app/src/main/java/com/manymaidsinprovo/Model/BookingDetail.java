package com.manymaidsinprovo.Model;

import java.util.ArrayList;

public class BookingDetail {

    private int order_detail_id;
    private int orderId;
    private int cleaningId;
    private int quantity;
    private int cartId;
    private String startDate;
    private String startTime;
    private String howOften;
    private double updatedPrice;
    private int status;
    private String instruction;
    private String days;
    private CleaningType cleaningType;
    private Task task;
    private ArrayList<Address> address;

    public BookingDetail() {
    }

    public BookingDetail(int order_detail_id, int orderId, int cleaningId, int quantity, int cartId, String startDate, String startTime, String howOften, double updatedPrice, int status, String instruction, String days, CleaningType cleaningType, Task task, ArrayList<Address> address) {
        this.order_detail_id = order_detail_id;
        this.orderId = orderId;
        this.cleaningId = cleaningId;
        this.quantity = quantity;
        this.cartId = cartId;
        this.startDate = startDate;
        this.startTime = startTime;
        this.howOften = howOften;
        this.updatedPrice = updatedPrice;
        this.status = status;
        this.instruction = instruction;
        this.days = days;
        this.cleaningType = cleaningType;
        this.task = task;
        this.address = address;
    }

    public int getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(int order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCleaningId() {
        return cleaningId;
    }

    public void setCleaningId(int cleaningId) {
        this.cleaningId = cleaningId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getHowOften() {
        return howOften;
    }

    public void setHowOften(String howOften) {
        this.howOften = howOften;
    }

    public double getUpdatedPrice() {
        return updatedPrice;
    }

    public void setUpdatedPrice(double updatedPrice) {
        this.updatedPrice = updatedPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public CleaningType getCleaningType() {
        return cleaningType;
    }

    public void setCleaningType(CleaningType cleaningType) {
        this.cleaningType = cleaningType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }
}
