package com.federation.masters.preuni.models;

public class Submission {
    private int id;
    private int studentId;
    private String submittedDate;
    private int classId;
    private int assignmentId;
    private String submissionFileLink;
    private int submissionType;

    public enum submissionMethod{
        HANDIN,
        ONLINE_SUBMISSION
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getSubmissionFileLink() {
        return submissionFileLink;
    }

    public void setSubmissionFileLink(String submissionFileLink) {
        this.submissionFileLink = submissionFileLink;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(int submissionType) {
        this.submissionType = submissionType;
    }
}
