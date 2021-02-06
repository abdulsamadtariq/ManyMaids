package com.manymaidsinprovo.Model;

public class Month {
    private int seasonId;
    private String monthName;
    private int taskId;

    public Month() {
    }

    public Month(int seasonId, String monthName, int taskId) {
        this.seasonId = seasonId;
        this.monthName = monthName;
        this.taskId = taskId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
