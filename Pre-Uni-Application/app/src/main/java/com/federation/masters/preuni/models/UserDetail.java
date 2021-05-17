package com.federation.masters.preuni.models;

public class UserDetail {
    private int id;
    private String userName;
    private String phoneNumber;
    private int userId;

    public UserDetail(int id, String name, String pNumber, int uID) {
        this.id = id;
        this.userName=name;
        this.phoneNumber = pNumber;
        this.userId = uID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
