package model;

public class User {
    private int userId;
    private int roleId;
    private String email;
    private String password; // Lưu ý: Trong thực tế, mật khẩu nên được băm (hashed)
    private String fullName; // Lấy từ bảng USER_PROFILE
    private String roleName; // Lấy từ bảng ROLE

    // Constructors
    public User() {
    }

    public User(int userId, int roleId, String email, String password, String fullName, String roleName) {
        this.userId = userId;
        this.roleId = roleId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.roleName = roleName;
    }

    // Getters and Setters
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
