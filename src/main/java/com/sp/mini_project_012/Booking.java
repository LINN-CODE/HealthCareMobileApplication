package com.sp.mini_project_012;


public class Booking {
    private String bookingId;
    private String clinicId;
    private String userId;
    private String date;
    private String shift;
    private String assignmentId;
    private String doctorId;
    private boolean isCompleted;
    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Booking(String bookingId, String clinicId, String userId, String date, String shift, String assignmentId, String doctorId) {
        this.bookingId = bookingId;
        this.clinicId = clinicId;
        this.userId = userId;
        this.date = date;
        this.shift = shift;
        this.assignmentId = assignmentId;
        this.doctorId = doctorId;
        this.isCompleted = false;
    }


    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    // Getters and setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
