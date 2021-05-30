package com.federation.masters.preuni.models;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Assignment {
    private int id;
    private String assignmentTitle;
    private String assignmentDesc;
    private String assignmentSubmissionDate;
    private int assignmentAddedBy;
    private int courseId;

    public int getId() {
        return id;
    }

    public void setId(int assignmentID) {
        this.id = assignmentID;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentName) {
        this.assignmentTitle = assignmentName;
    }

    public String getAssignmentDesc() {
        return assignmentDesc;
    }

    public void setAssignmentDesc(String assignmentDesc) {
        this.assignmentDesc = assignmentDesc;
    }

    public String getAssignmentSubmissionDate() {
        return assignmentSubmissionDate;
    }

    public void setAssignmentSubmissionDate(String assignmentSubmissionDate) {
        this.assignmentSubmissionDate = assignmentSubmissionDate;
    }

    public int getAssignmentAddedBy() {
        return assignmentAddedBy;
    }

    public void setAssignmentAddedBy(int assignmentAddedBy) {
        this.assignmentAddedBy = assignmentAddedBy;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public JSONObject getAssignmentPutObject()
    {
        JSONObject request=new JSONObject();
        try {
            request.put("assignmentTitle",getAssignmentTitle());
            request.put("assignmentDesc",getAssignmentDesc());
            request.put("assignmentSubmissionDate",getAssignmentSubmissionDate());
            request.put("assignmentAddedBy",getAssignmentAddedBy());
            request.put("courseId",getCourseId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }
}
