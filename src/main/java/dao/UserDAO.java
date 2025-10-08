package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.Address;
import model.User;
import model.UserProfile;

public class UserDAO extends DBContext {

    public UserDAO() {
        connection = getConnection(); // bắt buộc có dòng này
    }

    /**
     * Kiểm tra thông tin đăng nhập của người dùng bằng email và password.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b)); // chuyển byte -> hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User checkLogin(String email, String password) {
        String sql = "SELECT u.USER_ID, u.ROLE_ID, u.EMAIL, u.PASSWORD, p.FULL_NAME, r.NAME as ROLE_NAME "
                + "FROM [USER] u "
                + "JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID "
                + "JOIN ROLE r ON u.ROLE_ID = r.ROLE_ID "
                + "WHERE u.EMAIL = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("PASSWORD");
                String inputHash = hashPassword(password);

                // So sánh hash trong DB và hash nhập vào
                if (storedHash.equals(inputHash)) {
                    return new User(
                            rs.getInt("USER_ID"),
                            rs.getInt("ROLE_ID"),
                            rs.getString("EMAIL"),
                            storedHash,
                            rs.getString("FULL_NAME"),
                            rs.getString("ROLE_NAME")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi kiểm tra đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm kiếm người dùng trong database bằng địa chỉ email.
     */
    public User findUserByEmail(String email) {
        String sql = "SELECT u.USER_ID, u.ROLE_ID, u.EMAIL, u.PASSWORD, p.FULL_NAME, r.NAME as ROLE_NAME "
                + "FROM [USER] u "
                + "LEFT JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID "
                + "LEFT JOIN ROLE r ON u.ROLE_ID = r.ROLE_ID "
                + "WHERE u.EMAIL = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("USER_ID"),
                        rs.getInt("ROLE_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("PASSWORD"),
                        rs.getString("FULL_NAME"),
                        rs.getString("ROLE_NAME")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Đăng ký người dùng mới từ form.
     */
    public boolean registerUser(User user, UserProfile profile, Address address) {
        if (findUserByEmail(user.getEmail()) != null) {
            return false; // Email đã tồn tại
        }

        String insertUserSql = "INSERT INTO [USER] (ROLE_ID, EMAIL, PASSWORD, IS_EMAIL_VERIFIED) VALUES (?, ?, ?, 1)";

        String insertProfileSql = "INSERT INTO USER_PROFILE (USER_ID, FULL_NAME, PHONE, DOB, GENDER, DRIVER_LICENSE_NUMBER) VALUES (?, ?, ?, ?, ?, ?)";
        String insertAddressSql = "INSERT INTO ADDRESS (USER_ID, ADDRESS_LINE, CITY, PROVINCE, POSTAL_CODE, COUNTRY) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false); // Bắt đầu transaction

            // 1. Thêm vào bảng USER
            PreparedStatement userSt = connection.prepareStatement(insertUserSql, PreparedStatement.RETURN_GENERATED_KEYS);
            userSt.setInt(1, user.getRoleId()); // dùng role id được truyền từ servlet

            userSt.setString(2, user.getEmail());
            userSt.setString(3, user.getPassword());
            userSt.executeUpdate();

            ResultSet generatedKeys = userSt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int newUserId = generatedKeys.getInt(1);

                // 2. Thêm vào bảng USER_PROFILE
                PreparedStatement profileSt = connection.prepareStatement(insertProfileSql);
                profileSt.setInt(1, newUserId);
                profileSt.setString(2, profile.getFullName());
                profileSt.setString(3, profile.getPhone());
                profileSt.setDate(4, profile.getDob());
                profileSt.setString(5, profile.getGender());
                profileSt.setString(6, profile.getDriverLicenseNumber());
                profileSt.executeUpdate();

                // 3. Thêm vào bảng ADDRESS
                PreparedStatement addressSt = connection.prepareStatement(insertAddressSql);
                addressSt.setInt(1, newUserId);
                addressSt.setString(2, address.getAddressLine());
                addressSt.setString(3, address.getCity());
                addressSt.setString(4, address.getProvince());
                addressSt.setString(5, address.getPostalCode());
                addressSt.setString(6, address.getCountry());
                addressSt.executeUpdate();

                connection.commit(); // Hoàn tất transaction
                return true;
            }
        } catch (Exception e) {
            try {
                connection.rollback(); // Hoàn tác nếu có lỗi
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Trả lại trạng thái mặc định
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        try {
            String sqlSelect = "SELECT PASSWORD FROM [USER] WHERE EMAIL = ?";
            PreparedStatement psSelect = connection.prepareStatement(sqlSelect);
            psSelect.setString(1, email);
            ResultSet rs = psSelect.executeQuery();
            if (rs.next()) {
                String hashOld = rs.getString("PASSWORD");
                if (hashOld.equals(hashPassword(oldPassword))) {
                    String sqlUpdate = "UPDATE [USER] SET PASSWORD = ? WHERE EMAIL = ?";
                    PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate);
                    psUpdate.setString(1, hashPassword(newPassword));
                    psUpdate.setString(2, email);
                    psUpdate.executeUpdate();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
