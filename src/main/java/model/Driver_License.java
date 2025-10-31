package model;

import java.util.Date;

public class Driver_License {
    private int license_id;
    private int user_id;
    private String license_number;
    private Date issue_date;
    private Date expiry_date;
    private String image_url;

    //join
    private String fullName;
    private String gender;
    private Date dob;
    public Driver_License(){
    }
    public Driver_License(int license_id, int user_id, String license_number, Date issue_date, Date expiry_date, String image_url) {
        this.license_id = license_id;
        this.user_id = user_id;
        this.license_number = license_number;
        this.issue_date = issue_date;
        this.expiry_date = expiry_date;
        this.image_url = image_url;
    }

    public int getLicense_id() {
        return license_id;
    }

    public void setLicense_id(int license_id) {
        this.license_id = license_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
