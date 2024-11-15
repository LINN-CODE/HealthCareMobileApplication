package com.sp.mini_project_012;

public class AppNotification {
    private String message;
    private String dateTime;

    public AppNotification() {
        // Default constructor required for calls to DataSnapshot.getValue(AppNotification.class)
    }

    public AppNotification(String message, String dateTime) {
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
