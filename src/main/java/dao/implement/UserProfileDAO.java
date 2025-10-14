package dao.implement;

import dao.DBContext;
import model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDAO extends DBContext {

    /**  Lấy thông tin profile theo userId */
    public UserProfile findByUserId(int userId) {
        String sql = "SELECT * FROM [dbo].[USER_PROFILE] WHERE USER_ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserProfile profile = new UserProfile();
                profile.setProfileId(rs.getInt("PROFILE_ID"));
                profile.setUserId(rs.getInt("USER_ID"));
                profile.setFullName(rs.getString("FULL_NAME"));
                profile.setPhone(rs.getString("PHONE"));
                profile.setDob(rs.getDate("DOB"));
                profile.setGender(rs.getString("GENDER"));
                profile.setDriverLicenseNumber(rs.getString("DRIVER_LICENSE_NUMBER"));
                profile.setIsVerified(rs.getBoolean("IS_VERIFIED"));
                profile.setProfileImage(rs.getString("profileImage"));
                return profile;
            }
        } catch (SQLException e) {
            System.out.println("❌ [UserProfileDAO.findByUserId] Error: " + e.getMessage());
        }
        return null;
    }

    /** ✅ Cập nhật profile */
    public boolean updateProfile(UserProfile profile) {
        String sql = """
            UPDATE [dbo].[USER_PROFILE]
            SET FULL_NAME = ?, PHONE = ?, DOB = ?, GENDER = ?, 
                DRIVER_LICENSE_NUMBER = ?, profileImage = ?
            WHERE USER_ID = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, profile.getFullName());
            ps.setString(2, profile.getPhone());
            ps.setDate(3, profile.getDob());
            ps.setString(4, profile.getGender());
            ps.setString(5, profile.getDriverLicenseNumber());
            ps.setString(6, profile.getProfileImage());
            ps.setInt(7, profile.getUserId());

            int rows = ps.executeUpdate();
            System.out.println("✅ [UserProfileDAO.updateProfile] Rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ [UserProfileDAO.updateProfile] Error: " + e.getMessage());
            return false;
        }
    }

    /** ✅ Thêm mới profile */
    public boolean insertProfile(UserProfile profile) {
        String sql = """
            INSERT INTO [dbo].[USER_PROFILE]
                (USER_ID, FULL_NAME, PHONE, DOB, GENDER, DRIVER_LICENSE_NUMBER, profileImage, IS_VERIFIED)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFullName());
            ps.setString(3, profile.getPhone());
            ps.setDate(4, profile.getDob());
            ps.setString(5, profile.getGender());
            ps.setString(6, profile.getDriverLicenseNumber());
            ps.setString(7, profile.getProfileImage());
            ps.setBoolean(8, profile.isIsVerified());

            int rows = ps.executeUpdate();
            System.out.println("✅ [UserProfileDAO.insertProfile] Rows inserted: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ [UserProfileDAO.insertProfile] Error: " + e.getMessage());
            return false;
        }
    }
}
