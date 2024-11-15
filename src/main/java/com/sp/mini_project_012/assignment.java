package com.sp.mini_project_012;

public class assignment {
    private String assignmentId;
    private String doctorId;
    private String clinicId;
    private String date;
    private String shift;
    private String isBooked;

    // Default constructor required for calls to DataSnapshot.getValue(assignment.class)
    public assignment() {
    }

    public assignment(String assignmentId, String doctorId, String clinicId, String date, String shift, String isBooked) {
        this.assignmentId = assignmentId;
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.date = date;
        this.shift = shift;
        this.isBooked = isBooked;

    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String isBooked() {
        return isBooked;
    }

    public void setBooked(String booked) {
        isBooked = booked;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
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

}
