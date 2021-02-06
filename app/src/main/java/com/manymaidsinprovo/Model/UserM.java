package com.manymaidsinprovo.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class UserM {
    private int userId;
    private String userName;
    private int status;

    public UserM() {
    }

    public UserM(int userId, String userName, int status) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return userName;
    }
}
