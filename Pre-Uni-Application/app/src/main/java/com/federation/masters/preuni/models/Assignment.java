package com.federation.masters.preuni.models;

public class Assignment {
    private int assignmentID;
    private String assignmentName;
    private String assignmentDesc;
    private String assignmentDocFile;

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDesc() {
        return assignmentDesc;
    }

    public void setAssignmentDesc(String assignmentDesc) {
        this.assignmentDesc = assignmentDesc;
    }

    public String getAssignmentDocFile() {
        return assignmentDocFile;
    }

    public void setAssignmentDocFile(String assignmentDocFile) {
        this.assignmentDocFile = assignmentDocFile;
    }
}
