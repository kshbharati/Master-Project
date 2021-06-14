package com.federation.masters.preuni.models;

import java.util.ArrayList;

public class ParentHomeDetailViewPassingModel {
    Student student;
    ArrayList<TeachingClass> teachingClasses;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ArrayList<TeachingClass> getTeachingClasses() {
        return teachingClasses;
    }

    public void setTeachingClasses(ArrayList<TeachingClass> teachingClasses) {
        this.teachingClasses = teachingClasses;
    }
}
