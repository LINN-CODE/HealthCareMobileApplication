package com.sp.mini_project_012;

public class clinics {
    String name,phoneNo,address,email,UserType,ClinicID;

    public clinics(String name, String phoneNo, String address, String email, String userType,String clinicID) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.address = address;
        this.email = email;
        this.UserType = userType;
        this.ClinicID = clinicID;
    }

    public clinics() {
        // Default constructor required for calls to DataSnapshot.getValue(Clinic.class)
    }

    public String getUserType() {
        return UserType;
    }

    public String getClinicID() {
        return ClinicID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return UserType;
    }

    public void setClinicID(String clinicID) {
        ClinicID = clinicID;
    }
}
