package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.Address;
import model.User;
import model.UserProfile;

public class UserDAO extends DBContext {

    // Góp ý: Nên chuyển phương thức này sang một lớp tiện ích riêng, ví dụ: SecurityUtils.java
    // để tách biệt rõ ràng chức năng (DAO chỉ nên thao tác với CSDL).
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Kiểm tra thông tin đăng nhập.
     * Cập nhật để trả về đối tượng User đã được liên kết với UserProfile.
     */
    public User checkLogin(String email, String password) {
        // Lấy tất cả thông tin cần thiết từ cả 3 bảng
        String sql = "SELECT u.*, p.*, r.NAME as ROLE_NAME "
                   + "FROM [USER] u "
                   + "JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID "
                   + "JOIN ROLE r ON u.ROLE_ID = r.ROLE_ID "
                   + "WHERE u.EMAIL = ?";
        try (Connection conn = getConnection(); // Sử dụng try-with-resources để tự động đóng kết nối
             PreparedStatement st = conn.prepareStatement(sql)) {
            
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("PASSWORD");
                String inputHash = hashPassword(password);

                if (storedHash.equals(inputHash)) {
                    // Tạo đối tượng User và UserProfile từ ResultSet
                    UserProfile profile = new UserProfile();
                    profile.setProfileId(rs.getInt("PROFILE_ID"));
                    profile.setUserId(rs.getInt("USER_ID"));
                    profile.setFullName(rs.getString("FULL_NAME"));
                    profile.setPhone(rs.getString("PHONE"));
                    profile.setDob(rs.getDate("DOB"));
                    profile.setGender(rs.getString("GENDER"));
                    profile.setDriverLicenseNumber(rs.getString("DRIVER_LICENSE_NUMBER"));
                    profile.setIsVerified(rs.getBoolean("IS_VERIFIED"));

                    User user = new User();
                    user.setUserId(rs.getInt("USER_ID"));
                    user.setRoleId(rs.getInt("ROLE_ID"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword(storedHash); // Lưu lại mật khẩu đã hash
                    user.setIsEmailVerified(rs.getBoolean("IS_EMAIL_VERIFIED"));
                    user.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                    
                    // Liên kết profile vào user
                    user.setUserProfile(profile);
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm kiếm người dùng bằng email.
     * Cũng được cập nhật để trả về đối tượng User hoàn chỉnh.
     */
    public User findUserByEmail(String email) {
        String sql = "SELECT u.*, p.* FROM [USER] u LEFT JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID WHERE u.EMAIL = ?";
        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // Tái sử dụng logic tương tự như checkLogin
                UserProfile profile = new UserProfile();
                profile.setProfileId(rs.getInt("PROFILE_ID"));
                profile.setUserId(rs.getInt("USER_ID"));
                profile.setFullName(rs.getString("FULL_NAME"));
                // (Bạn có thể thêm các trường khác của profile nếu cần)

                User user = new User();
                user.setUserId(rs.getInt("USER_ID"));
                user.setRoleId(rs.getInt("ROLE_ID"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setIsEmailVerified(rs.getBoolean("IS_EMAIL_VERIFIED"));
                user.setCreatedAt(rs.getTimestamp("CREATED_AT"));
                
                user.setUserProfile(profile);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Đăng ký người dùng mới.
     * Cập nhật signature và logic để phù hợp với model mới.
     */
    public boolean registerUser(User user, Address address) {
        // Lấy profile từ đối tượng user
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            // Profile là bắt buộc, không thể đăng ký nếu thiếu
            return false; 
        }

        String insertUserSql = "INSERT INTO [USER] (ROLE_ID, EMAIL, PASSWORD, IS_EMAIL_VERIFIED, CREATED_AT) VALUES (?, ?, ?, ?, ?)";
        String insertProfileSql = "INSERT INTO USER_PROFILE (USER_ID, FULL_NAME, PHONE, DOB, GENDER, DRIVER_LICENSE_NUMBER, IS_VERIFIED) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertAddressSql = "INSERT INTO ADDRESS (USER_ID, ADDRESS_LINE, CITY, PROVINCE, POSTAL_CODE, COUNTRY) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Thêm vào bảng USER
            try (PreparedStatement userSt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userSt.setInt(1, user.getRoleId());
                userSt.setString(2, user.getEmail());
                userSt.setString(3, user.getPassword());
                userSt.setBoolean(4, user.isIsEmailVerified()); // Lấy từ object
                userSt.setTimestamp(5, user.getCreatedAt());   // Lấy từ object
                userSt.executeUpdate();

                try (ResultSet generatedKeys = userSt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newUserId = generatedKeys.getInt(1);

                        // 2. Thêm vào bảng USER_PROFILE
                        try (PreparedStatement profileSt = conn.prepareStatement(insertProfileSql)) {
                            profileSt.setInt(1, newUserId);
                            profileSt.setString(2, profile.getFullName());
                            profileSt.setString(3, profile.getPhone());
                            profileSt.setDate(4, profile.getDob());
                            profileSt.setString(5, profile.getGender());
                            profileSt.setString(6, profile.getDriverLicenseNumber());
                            profileSt.setBoolean(7, profile.isIsVerified()); // Thêm trường isVerified
                            profileSt.executeUpdate();
                        }

                        // 3. Thêm vào bảng ADDRESS (Nếu có)
                        if (address != null) {
                            try (PreparedStatement addressSt = conn.prepareStatement(insertAddressSql)) {
                                addressSt.setInt(1, newUserId);
                                addressSt.setString(2, address.getAddressLine());
                                addressSt.setString(3, address.getCity());
                                addressSt.setString(4, address.getProvince()); // Giả sử lớp Address có getProvince()
                                addressSt.setString(5, address.getPostalCode());
                                addressSt.setString(6, address.getCountry());
                                addressSt.executeUpdate();
                            }
                        }

                        conn.commit(); // Hoàn tất transaction
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    /**
     * Thay đổi mật khẩu cho người dùng.
     * @param email Email của người dùng
     * @param oldPassword Mật khẩu cũ (chưa hash)
     * @param newPassword Mật khẩu mới (chưa hash)
     * @return true nếu đổi thành công, false nếu mật khẩu cũ không đúng.
     */
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        String sqlSelect = "SELECT PASSWORD FROM [USER] WHERE EMAIL = ?";
        String sqlUpdate = "UPDATE [USER] SET PASSWORD = ? WHERE EMAIL = ?";

        try (Connection conn = getConnection();
             PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {

            psSelect.setString(1, email);
            ResultSet rs = psSelect.executeQuery();

            // Kiểm tra xem người dùng có tồn tại không và lấy mật khẩu đã hash
            if (rs.next()) {
                String storedHashedPassword = rs.getString("PASSWORD");
                String hashedOldPassword = hashPassword(oldPassword); // Hash mật khẩu cũ người dùng nhập vào

                // So sánh mật khẩu cũ đã hash với mật khẩu trong DB
                if (storedHashedPassword.equals(hashedOldPassword)) {
                    // Nếu khớp, cập nhật mật khẩu mới đã được hash
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                        psUpdate.setString(1, hashPassword(newPassword)); // Hash mật khẩu mới
                        psUpdate.setString(2, email);
                        
                        int rowsAffected = psUpdate.executeUpdate();
                        return rowsAffected > 0; // Trả về true nếu có dòng được cập nhật
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Trả về false nếu có lỗi hoặc mật khẩu cũ không đúng
        return false;
    }
}