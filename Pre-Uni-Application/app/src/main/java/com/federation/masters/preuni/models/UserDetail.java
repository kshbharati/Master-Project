package com.federation.masters.preuni.models;

public class UserDetail {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private int categoryId;
    private int userId;

    public UserDetail(int id,String fName, String lName, String pNumber, int catID, int uID)
    {
        this.id=id;
        this.firstName=fName;
        this.lastName=lName;
        this.phoneNumber=pNumber;
        this.categoryId=catID;
        this.userId=uID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName()
    {
        return this.firstName+" "+this.lastName;
    }
}
