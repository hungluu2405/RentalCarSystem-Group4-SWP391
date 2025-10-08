package model;

import java.util.Date;

public class User {
    private int userId;
    private int roleId;
    private String email;
    private String password;
    boolean is_email_verified;
    Date created_at;

    public User() {
    }

    public User(int userId, int roleId, String email, String password, boolean is_email_verified, Date created_at) {
        this.userId = userId;
        this.roleId = roleId;
        this.email = email;
        this.password = password;
        this.is_email_verified = is_email_verified;
        this.created_at = created_at;
    }

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

    public boolean isIs_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(boolean is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
