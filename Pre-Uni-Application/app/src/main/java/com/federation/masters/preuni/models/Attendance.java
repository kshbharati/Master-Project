package com.federation.masters.preuni.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Attendance {
    private int id;
    private int classId;
    private int studentId;
    private int attendance;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JSONObject getRequestBody()
    {
        JSONObject json=new JSONObject();
        try {
            json.put("classId",getClassId());
            json.put("studentId",getStudentId());
            json.put("attendance",getAttendance());
            json.put("date",getDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
