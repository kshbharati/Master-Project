package com.federation.masters.preuni.models;

import java.text.DateFormat;
import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private UserDetail userdetail;
    private int category;
    private String userCreatedDate;

    public User() {
    }

    public User(int userid, String userEmail, UserDetail detail) {
        this.id = userid;
        this.email = userEmail;
        this.userdetail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDetail getUserdetail() {
        return userdetail;
    }

    public void setUserdetail(UserDetail userdetail) {
        this.userdetail = userdetail;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getUserCreatedDate() {
        return userCreatedDate;
    }

    public void setUserCreatedDate(String userCreatedDate) {
        this.userCreatedDate = userCreatedDate;
    }
}
