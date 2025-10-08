package model;

import java.sql.Date;

public class UserProfile {
    private int profileId;
    private int userId;
    private String fullName;
    private String phone;
    private Date dob;
    private String gender;
    private String driverLicenseNumber;

    // Constructors, Getters and Setters
    public UserProfile() {}

    public UserProfile(String fullName, String phone, Date dob, String gender, String driverLicenseNumber) {
        this.fullName = fullName;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public int getProfileId() {
        return profileId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public Date getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }
    
    }