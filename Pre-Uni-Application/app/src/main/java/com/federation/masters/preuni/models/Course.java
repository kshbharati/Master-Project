package com.federation.masters.preuni.models;

import java.util.ArrayList;

public class Course {
    private int id;
    private String courseTitle;
    private String courseDesc;
    private ArrayList<Assignment> assignmentList;

    public Course() {}

    public Course(String name, String courseDesc) {
        this.id = 1;
        this.courseTitle = name;
        this.courseDesc = courseDesc;

    }

    public int getId() {
        return id;
    }

    public void setId(int courseID) {
        this.id = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseName) {
        this.courseTitle = courseName;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public void setAssignmentList(ArrayList<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }
}
