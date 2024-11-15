package com.sp.mini_project_012;

public class messageModel {

    String message;
    String receiverId;
    long timeStamp;

    public messageModel() {
    }

    public messageModel(String MMessage, String receiverID, long timeStamp) {
        this.message = MMessage;
        this.receiverId = receiverID;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
