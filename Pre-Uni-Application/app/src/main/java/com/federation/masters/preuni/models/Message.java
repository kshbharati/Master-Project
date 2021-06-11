package com.federation.masters.preuni.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {
    private int id;
    private String senderEmail;
    private String receiverEmail;
    private String messageSubject;
    private String messageBody;
    private String messageReadStatus;
    private String messageSentTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageReadStatus() {
        return messageReadStatus;
    }

    public void setMessageReadStatus(String messageReadStatus) {
        this.messageReadStatus = messageReadStatus;
    }

    public String getMessageSentTime() {
        return messageSentTime;
    }

    public void setMessageSentTime(String messageSentTime) {
        this.messageSentTime = messageSentTime;
    }

    public JSONObject getRequestData()
    {
        JSONObject request=new JSONObject();
        try {
            request.put("senderEmail",getSenderEmail());
            request.put("receiverEmail",getReceiverEmail());
            request.put("messageSubject",getMessageSubject());
            request.put("messageBody",getMessageBody());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    public boolean isMessageReadStatus() {
        return getMessageReadStatus().equals("READ");
    }
}
