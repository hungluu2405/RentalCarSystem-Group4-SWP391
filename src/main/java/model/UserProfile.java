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
    private boolean isVerified;
    private String profileImage; // ✅ Ảnh đại diện (đường dẫn lưu trong DB)

    // === Constructors ===
    public UserProfile() {
    }

    // Constructor đầy đủ có userId
    public UserProfile(int userId, String fullName, String phone, Date dob, String gender) {
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
    }

    // ✅ Constructor dùng khi đăng ký tài khoản (RegisterServlet)
    // Không có userId vì người dùng mới tạo
    public UserProfile(String fullName, String phone, Date dob, String gender) {
        this.fullName = fullName;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
    }

    // ✅ Constructor rút gọn cho ChatList (chỉ cần id, tên, ảnh)
    public UserProfile(int userId, String fullName, String profileImage) {
        this.userId = userId;
        this.fullName = fullName;
        this.profileImage = profileImage;
    }

    // === Getters & Setters ===
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
