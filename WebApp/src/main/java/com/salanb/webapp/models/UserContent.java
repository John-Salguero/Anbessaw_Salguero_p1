package com.salanb.webapp.models;

public class UserContent {
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    String emailAddress;
    String address;
    String city;
    String state;
    String zipCode;
    String SSN_Hash;
    String Pass_Hash;

    @Override
    public String toString() {
        return "UserContent{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", SSN_Hash='" + SSN_Hash + '\'' +
                ", Pass_Hash='" + Pass_Hash + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSSN_Hash() {
        return SSN_Hash;
    }

    public void setSSN_Hash(String SSN_Hash) {
        this.SSN_Hash = SSN_Hash;
    }

    public String getPass_Hash() {
        return Pass_Hash;
    }

    public void setPass_Hash(String pass_Hash) {
        Pass_Hash = pass_Hash;
    }
}
