package com.manymaidsinprovo.Model;

public class CartItem {

    private int cartId;
    private CleaningType cleaningType;
    private String date;
    private String time;
    private Address address;
    private String howOften;
    private int status;
    private String days;
    private String totalBill;
    private String instruction;
    private int quantity;
    private String updatedBill;
    private Task task;
    private int taskId;

    public CartItem() {
    }

    public CartItem(int cartId, CleaningType cleaningType, String date, String time, Address address, String howOften, int status, String days, String totalBill, String instruction, int quantity, String updatedBill, int taskId) {
        this.cartId = cartId;
        this.cleaningType = cleaningType;
        this.date = date;
        this.time = time;
        this.address = address;
        this.howOften = howOften;
        this.status = status;
        this.days = days;
        this.totalBill = totalBill;
        this.instruction = instruction;
        this.quantity = quantity;
        this.updatedBill = updatedBill;
        this.taskId = taskId;
    }

    public CartItem(int cartId, CleaningType cleaningType, String date, String time, Address address, String howOften, int status, String days, String totalBill, String instruction, int quantity, String updatedBill, Task task, int taskId) {
        this.cartId = cartId;
        this.cleaningType = cleaningType;
        this.date = date;
        this.time = time;
        this.address = address;
        this.howOften = howOften;
        this.status = status;
        this.days = days;
        this.totalBill = totalBill;
        this.instruction = instruction;
        this.quantity = quantity;
        this.updatedBill = updatedBill;
        this.task = task;
        this.taskId = taskId;
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getUpdatedBill() {
        return updatedBill;
    }

    public void setUpdatedBill(String updatedBill) {
        this.updatedBill = updatedBill;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public CleaningType getCleaningType() {
        return cleaningType;
    }

    public void setCleaningType(CleaningType cleaningType) {
        this.cleaningType = cleaningType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getHowOften() {
        return howOften;
    }

    public void setHowOften(String howOften) {
        this.howOften = howOften;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
