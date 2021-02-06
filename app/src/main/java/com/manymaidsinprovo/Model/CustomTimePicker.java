package com.manymaidsinprovo.Model;

public class CustomTimePicker {
    private String time;
    private boolean isSelected;

    public CustomTimePicker() {

    }

    public CustomTimePicker(String time, boolean isSelected) {
        this.time = time;
        this.isSelected = isSelected;
    }

    public CustomTimePicker(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
