package com.federation.masters.preuni.models;

import java.util.ArrayList;

public class ParentUser extends User {
    public ArrayList<Student> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(ArrayList<Student> childrenList) {
        this.childrenList = childrenList;
    }

    private ArrayList<Student> childrenList;
}
