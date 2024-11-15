package com.sp.mini_project_012;

public class DoctorAssignment {
    private String patientName;
    private String date;
    private String shift;

    public DoctorAssignment(String patientName, String date, String shift) {
        this.patientName = patientName;
        this.date = date;
        this.shift = shift;
    }

    // Getters
    public String getPatientName() { return patientName; }
    public String getDate() { return date; }
    public String getShift() { return shift; }
}
