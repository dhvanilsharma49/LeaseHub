package model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private String firstName, lastName, contactNo, emailId, password, userType;

    public User() {
    }

    public User(String firstName, String lastName, String contactNo, String emailId, String password, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNo = contactNo;
        this.emailId = emailId;
        this.password = password;
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String usertype) {
        this.userType = usertype;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name : " + firstName + " " + lastName + " Email Id : " + emailId + " Account Type : " + userType;
    }
}
