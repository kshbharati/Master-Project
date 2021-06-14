package com.federation.masters.preuni.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Submission {
    private int id;
    private int studentId;
    private String submittedDate;
    private int classId;
    private int assignmentId;
    private String submissionFile="";
    private int submissionType;

    public enum submissionMethod{
        HANDIN,
        ONLINE,
        EXAM
    }

    ArrayList<Grading> gradings=new ArrayList<Grading>();

    public ArrayList<Grading> getGradings() {
        return gradings;
    }

    public void setGradings(ArrayList<Grading> gradings) {
        this.gradings = gradings;
    }

    public JSONObject getRequestData()
    {
        JSONObject request=new JSONObject();
        try {
            request.put("studentId",getStudentId());
            request.put("classId",getClassId());
            request.put("assignmentId",getAssignmentId());
            request.put("submissionFile",getSubmissionFile());
            request.put("submissionType",getSubmissionType());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
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

    public String getSubmissionFile() {
        return submissionFile;
    }

    public void setSubmissionFile(String submissionFile) {
        this.submissionFile = submissionFile;
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
