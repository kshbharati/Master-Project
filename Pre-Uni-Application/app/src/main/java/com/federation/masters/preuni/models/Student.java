package com.federation.masters.preuni.models;

import java.util.ArrayList;

public class Student {
    private int stuID;

    private String studentName;

    private ArrayList<Course> enrolledCourseList;

    public Student() {
        this.enrolledCourseList = new ArrayList<Course>();
    }

    public Student(int id, String name) {
        Course course = new Course("Test Course 1", "Random Dewsc");
        Course course1 = new Course("Test Course 2", "Random Dewsc");
        this.stuID = id;
        this.studentName = name;

        this.enrolledCourseList = new ArrayList<Course>();
        this.enrolledCourseList.add(course);
        this.enrolledCourseList.add(course1);
    }

    public int getStuID() {
        return stuID;
    }

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public ArrayList<Course> getEnrolledCourseList() {
        return enrolledCourseList;
    }

    public void setEnrolledCourseList(ArrayList<Course> enrolledCourseList) {
        this.enrolledCourseList = enrolledCourseList;
    }

}
