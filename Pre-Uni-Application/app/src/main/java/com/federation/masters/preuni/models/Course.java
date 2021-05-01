package com.federation.masters.preuni.models;

import java.util.ArrayList;

public class Course {
    private int courseID;
    private String courseName;
    private String courseDesc;
    private ArrayList<Assignment> assignmentList;

    public Course(String name, String courseDesc) {
        this.courseID = 1;
        this.courseName = name;
        this.courseDesc = courseDesc;

    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
