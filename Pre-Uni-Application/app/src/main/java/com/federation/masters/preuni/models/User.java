package com.federation.masters.preuni.models;

public class User {
    private int id;
    private String email;
    private UserDetail userdetail;

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
}
