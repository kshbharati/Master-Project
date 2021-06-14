package com.federation.masters.preuni.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Grading {
    private int id;
    private int submissionId;
    private String markGiven;
    private String feedback;
    private String gradedDate;

    public JSONObject requestData()
    {
        JSONObject request=new JSONObject();
        try {
            request.put("submissionId",getSubmissionId());
            request.put("markGiven",getMarkGiven());
            request.put("feedback",getFeedback());

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

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getMarkGiven() {
        return markGiven;
    }

    public void setMarkGiven(String markGiven) {
        this.markGiven = markGiven;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGradedDate() {
        return gradedDate;
    }

    public void setGradedDate(String gradedDate) {
        this.gradedDate = gradedDate;
    }
}
