package com.sp.mini_project_012;

public class users {
    String FirstName;
    String LastName;
    String Email;
    String PhoneNumber;
    String FinNumber;
    String UserType;
    String UserID;
    public users() {
    }
    public users(String firstName, String lastName, String email, String phoneNumber, String finNumber, String userType,String userID) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.FinNumber = finNumber;
        this.UserType = userType;
        this.UserID = userID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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

    public void setUserType(String userType) {
        UserType = userType;
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

    public String getUserType() {
        return UserType;
    }
}

