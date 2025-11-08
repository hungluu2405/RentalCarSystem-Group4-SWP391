package model;

import java.util.Date;

public class Driver_License {
    private int license_id;
    private int user_id;
    private String license_number;
    private Date issue_date;
    private Date expiry_date;
    private String front_image_url;
    private String back_image_url;
    private String licenseClass;
    private String address;
    private String nationality;

    //join
    private String fullName;
    private String gender;
    private Date dob;
    public Driver_License(){
    }

    public Driver_License(int license_id, int user_id, String license_number, Date issue_date, Date expiry_date, String front_image_url, String back_image_url, String licenseClass, String address, String nationality, String fullName, String gender, Date dob) {
        this.license_id = license_id;
        this.user_id = user_id;
        this.license_number = license_number;
        this.issue_date = issue_date;
        this.expiry_date = expiry_date;
        this.front_image_url = front_image_url;
        this.back_image_url = back_image_url;
        this.licenseClass = licenseClass;
        this.address = address;
        this.nationality = nationality;
        this.fullName = fullName;
        this.gender = gender;
        this.dob = dob;
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

    public String getFront_image_url() {
        return front_image_url;
    }

    public void setFront_image_url(String front_image_url) {
        this.front_image_url = front_image_url;
    }

    public String getBack_image_url() {
        return back_image_url;
    }

    public void setBack_image_url(String back_image_url) {
        this.back_image_url = back_image_url;
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

    public String getLicenseClass() {
        return licenseClass;
    }

    public void setLicenseClass(String licenseClass) {
        this.licenseClass = licenseClass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
