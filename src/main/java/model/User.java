package model;

import java.sql.Timestamp; // Sử dụng Timestamp cho CREATED_AT để có cả ngày và giờ

public class User {
    private int userId;
    private int roleId;
    private String email;
    private String password;
    private boolean isEmailVerified; // Thuộc tính này cần setter
    private Timestamp createdAt;     // Thuộc tính này cần setter

    // Mối quan hệ: Một User CÓ MỘT UserProfile
    private UserProfile userProfile; // Thuộc tính này cần setter
    private String roleName;// cần để lấy ra tên role trong dashboard
    // Constructors
    public User() {
    }

    // Getters and Setters (Đầy đủ)

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // PHƯƠNG THỨC BỊ THIẾU DẪN ĐẾN LỖI
    public boolean isIsEmailVerified() { // Getter
        return isEmailVerified;
    }

    public void setIsEmailVerified(boolean isEmailVerified) { // Setter
        this.isEmailVerified = isEmailVerified;
    }

    // PHƯƠNG THỨC BỊ THIẾU DẪN ĐẾN LỖI
    public Timestamp getCreatedAt() { // Getter
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) { // Setter
        this.createdAt = createdAt;
    }

    // PHƯƠNG THỨC BỊ THIẾU DẪN ĐẾN LỖI
    public UserProfile getUserProfile() { // Getter
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) { // Setter
        this.userProfile = userProfile;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    
}