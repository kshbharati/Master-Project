package com.federation.masters.preuni.models;

public class TeachingClass {
    private int id;
    private String classTitle;
    private int courseTaught;
    private int staffTeaching;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public int getCourseTaught() {
        return courseTaught;
    }

    public void setCourseTaught(int courseTaught) {
        this.courseTaught = courseTaught;
    }

    public int getStaffTeaching() {
        return staffTeaching;
    }

    public void setStaffTeaching(int staffTeaching) {
        this.staffTeaching = staffTeaching;
    }
}
