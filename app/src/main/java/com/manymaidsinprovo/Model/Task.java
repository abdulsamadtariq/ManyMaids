package com.manymaidsinprovo.Model;

import java.io.Serializable;

public class Task implements Serializable {


    private int taskId;
    private int taskHistoryId;
    private String taskName;
    private String note;
    private String startTime;
    private String endTime;
    private int areaId;
    private int taskStatus;
    private int taskIndicator;
    private int howOftenQty;
    private String howOftenDays;
    private String completedDate;
    private String userName;
    private String AreaName;

    public Task() {
    }

    public Task(int taskId, int taskHistoryId, String taskName, String note, String startTime, String endTime, int areaId, int taskStatus, int taskIndicator, int howOftenQty, String howOftenDays, String completedDate, String userName, String areaName) {
        this.taskId = taskId;
        this.taskHistoryId = taskHistoryId;
        this.taskName = taskName;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.areaId = areaId;
        this.taskStatus = taskStatus;
        this.taskIndicator = taskIndicator;
        this.howOftenQty = howOftenQty;
        this.howOftenDays = howOftenDays;
        this.completedDate = completedDate;
        this.userName = userName;
        AreaName = areaName;
    }

    public Task(int taskId, String taskName, String note, String startTime, String endTime, int areaId, int taskStatus, int taskIndicator, int howOftenQty, String howOftenDays, String completedDate, String userName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.areaId = areaId;
        this.taskStatus = taskStatus;
        this.taskIndicator = taskIndicator;
        this.howOftenQty = howOftenQty;
        this.howOftenDays = howOftenDays;
        this.completedDate = completedDate;
        this.userName = userName;
    }

    public Task(int taskId, String taskName, String note, String startTime, String endTime, int areaId, int taskStatus, int taskIndicator, int howOftenQty, String howOftenDays, String completedDate, String userName, String areaName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.areaId = areaId;
        this.taskStatus = taskStatus;
        this.taskIndicator = taskIndicator;
        this.howOftenQty = howOftenQty;
        this.howOftenDays = howOftenDays;
        this.completedDate = completedDate;
        this.userName = userName;
        AreaName = areaName;
    }


    public int getTaskHistoryId() {
        return taskHistoryId;
    }

    public void setTaskHistoryId(int taskHistoryId) {
        this.taskHistoryId = taskHistoryId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskIndicator() {
        return taskIndicator;
    }

    public void setTaskIndicator(int taskIndicator) {
        this.taskIndicator = taskIndicator;
    }

    public int getHowOftenQty() {
        return howOftenQty;
    }

    public void setHowOftenQty(int howOftenQty) {
        this.howOftenQty = howOftenQty;
    }

    public String getHowOftenDays() {
        return howOftenDays;
    }

    public void setHowOftenDays(String howOftenDays) {
        this.howOftenDays = howOftenDays;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }
}
