package com.federation.masters.preuni.models;

import java.util.ArrayList;

public class StaffUser extends User {
    private ArrayList<TeachingClass> assignedClasses;

    public ArrayList<TeachingClass> getAssignedClasses() {
        return assignedClasses;
    }

    public void setAssignedClasses(ArrayList<TeachingClass> assignedClasses) {
        this.assignedClasses = assignedClasses;
    }
}
