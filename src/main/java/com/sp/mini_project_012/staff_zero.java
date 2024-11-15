package com.sp.mini_project_012;


public class staff_zero {
    String FirstName;
    String LastName;
    String Email;
    String PhoneNumber;
    String FinNumber;
    String SelectedClinic;
    String StaffType;
    String UserType;
    String SelectedClinicId;

    String StaffId;

    public staff_zero(String firstName, String lastName, String email, String phoneNumber, String finNumber, String selectedClinic,String userType,String selectedClinicId,String staffId) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.FinNumber = finNumber;
        this.SelectedClinic = selectedClinic;
        this.UserType = userType;
        this.SelectedClinicId = selectedClinicId;
        this.StaffId = staffId;
    }

    public String getStaffId() {
        return StaffId;
    }

    public staff_zero() {
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getFinNumber() {
        return FinNumber;
    }

    public String getSelectedClinic() {
        return SelectedClinic;
    }

    public String getSelectedClinicId() {
        return SelectedClinicId;
    }

    public String getStaffType() {
        return StaffType;
    }

    public String getUserType() {
        return UserType;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setFinNumber(String finNumber) {
        FinNumber = finNumber;
    }

    public void setSelectedClinic(String selectedClinic) {
        SelectedClinic = selectedClinic;
    }

    public void setStaffType(String staffType) {
        StaffType = staffType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public void setStaffId(String staffId) {
        StaffId = staffId;
    }

    public void setSelectedClinicId(String selectedClinicId) {
        SelectedClinicId = selectedClinicId;
    }
}
