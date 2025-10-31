package dao.implement;

import dao.DBContext;
import model.Driver_License;
import java.sql.*;

public class Driver_LicenseDAO extends DBContext {

    // 🟩 Lấy thông tin bằng lái + join thông tin user
    public Driver_License getLicenseByUserId(int userId) {
        String sql = """
            SELECT d.LICENSE_ID, d.USER_ID, d.LICENSE_NUMBER, d.ISSUE_DATE, d.EXPIRY_DATE,
                                                  d.FRONT_IMAGE_URL, d.BACK_IMAGE_URL,
                                                  u.FULL_NAME, u.GENDER, u.DOB
                                           FROM DRIVER_LICENSE d
                                           JOIN USER_PROFILE u ON d.USER_ID = u.USER_ID
                                           WHERE d.USER_ID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Driver_License dl = new Driver_License();
                    dl.setLicense_id(rs.getInt("LICENSE_ID"));
                    dl.setUser_id(rs.getInt("USER_ID"));
                    dl.setLicense_number(rs.getString("LICENSE_NUMBER"));
                    dl.setIssue_date(rs.getDate("ISSUE_DATE"));
                    dl.setExpiry_date(rs.getDate("EXPIRY_DATE"));
                    dl.setFront_image_url(rs.getString("FRONT_IMAGE_URL"));
                    dl.setBack_image_url(rs.getString("BACK_IMAGE_URL"));

                    // Join thêm thông tin user
                    dl.setFullName(rs.getString("FULL_NAME"));
                    dl.setGender(rs.getString("GENDER"));
                    dl.setDob(rs.getDate("DOB"));

                    return dl;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error in getLicenseByUserId: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 🟨 Cập nhật thông tin bằng lái (update từ form JSP)
    public boolean updateLicense(Driver_License dl) {
        String sql = """
            UPDATE DRIVER_LICENSE
                        SET LICENSE_NUMBER = ?, ISSUE_DATE = ?, EXPIRY_DATE = ?,\s
                            FRONT_IMAGE_URL = ?, BACK_IMAGE_URL = ?
                        WHERE USER_ID = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, dl.getLicense_number());
            ps.setDate(2, dl.getIssue_date() != null ? new java.sql.Date(dl.getIssue_date().getTime()) : null);
            ps.setDate(3, dl.getExpiry_date() != null ? new java.sql.Date(dl.getExpiry_date().getTime()) : null);
            ps.setString(4, dl.getFront_image_url());
            ps.setString(5, dl.getBack_image_url());
            ps.setInt(6, dl.getUser_id());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error in updateLicense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertLicense(Driver_License dl) {
        String sql = """
            INSERT INTO DRIVER_LICENSE (USER_ID, LICENSE_NUMBER, ISSUE_DATE, EXPIRY_DATE, FRONT_IMAGE_URL, BACK_IMAGE_URL)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dl.getUser_id());
            ps.setString(2, dl.getLicense_number());
            ps.setDate(3, dl.getIssue_date() != null ? new java.sql.Date(dl.getIssue_date().getTime()) : null);
            ps.setDate(4, dl.getExpiry_date() != null ? new java.sql.Date(dl.getExpiry_date().getTime()) : null);
            ps.setString(5, dl.getFront_image_url());
            ps.setString(6, dl.getBack_image_url());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error in insertLicense: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
