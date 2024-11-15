package com.sp.mini_project_012;
public class NotificationClass {
    private String message;
    private String dateTime;

    public NotificationClass() {
        // Default constructor required for calls to DataSnapshot.getValue(Notification.class)
    }

    public NotificationClass(String message, String dateTime) {
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
