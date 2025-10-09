package dao.implement;

import dao.GenericDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.User;
import java.util.List;
import java.util.Map;
import model.Address;
import model.UserProfile;
import util.SecurityUtils;

public class UserDAO extends GenericDAO<User> {

    public UserDAO() {
        connection = getConnection(); // bắt buộc có dòng này
    }

    @Override
    public List<User> findAll() {
        return queryGenericDAO(User.class);
    }

    @Override
    public int insert(User user) {
        return insertGenericDAO(user);
    }

    // Lấy user theo ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM [USER] WHERE userId = ?";
        List<User> list = queryGenericDAO(User.class, sql, Map.of("userId", id));
        return list.isEmpty() ? null : list.get(0);
    }

    // Lấy user theo email (ví dụ cho login)
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM [USER] WHERE email = ?";
        List<User> list = queryGenericDAO(User.class, sql, Map.of("email", email));
        return list.isEmpty() ? null : list.get(0);
    }

    // --- CÁC PHƯƠNG THỨC XÁC THỰC VÀ TÌM KIẾM ---

    public User checkLogin(String email, String password) {
        String sql = "SELECT u.*, p.FULL_NAME, r.NAME as ROLE_NAME "
                + "FROM [USER] u "
                + "JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID "
                + "JOIN ROLE r ON u.ROLE_ID = r.ROLE_ID "
                + "WHERE u.EMAIL = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("PASSWORD");
                String inputHash = SecurityUtils.hashPassword(password);

                if (storedHash.equals(inputHash)) {
                    User user = new User();
                    user.setUserId(rs.getInt("USER_ID"));
                    user.setRoleId(rs.getInt("ROLE_ID"));
                    user.setEmail(rs.getString("EMAIL"));

                    UserProfile profile = new UserProfile();
                    profile.setFullName(rs.getString("FULL_NAME"));
                    user.setUserProfile(profile);

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT u.*, p.FULL_NAME FROM [USER] u LEFT JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID WHERE u.EMAIL = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("USER_ID"));
                user.setEmail(rs.getString("EMAIL"));

                UserProfile profile = new UserProfile();
                profile.setFullName(rs.getString("FULL_NAME"));
                user.setUserProfile(profile);

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- CÁC PHƯƠNG THỨC ĐĂNG KÝ VÀ CẬP NHẬT ---

    public boolean registerUser(User user, UserProfile profile, Address address) {
        if (findUserByEmail(user.getEmail()) != null) return false;

        String insertUserSql = "INSERT INTO [USER] (ROLE_ID, EMAIL, PASSWORD, IS_EMAIL_VERIFIED) VALUES (?, ?, ?, 1)";
        String insertProfileSql = "INSERT INTO USER_PROFILE (USER_ID, FULL_NAME, PHONE, DOB, GENDER, DRIVER_LICENSE_NUMBER) VALUES (?, ?, ?, ?, ?, ?)";
        String insertAddressSql = "INSERT INTO ADDRESS (USER_ID, ADDRESS_LINE, CITY, PROVINCE, POSTAL_CODE, COUNTRY) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            int customerRoleId = 2;

            // Mã hóa mật khẩu trước khi lưu
            String hashedPassword = SecurityUtils.hashPassword(user.getPassword());

            try (PreparedStatement userSt = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userSt.setInt(1, customerRoleId);
                userSt.setString(2, user.getEmail());
                userSt.setString(3, hashedPassword);
                userSt.executeUpdate();

                try (ResultSet generatedKeys = userSt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newUserId = generatedKeys.getInt(1);

                        try (PreparedStatement profileSt = connection.prepareStatement(insertProfileSql)) {
                            profileSt.setInt(1, newUserId);
                            profileSt.setString(2, profile.getFullName());
                            profileSt.setString(3, profile.getPhone());
                            profileSt.setDate(4, profile.getDob());
                            profileSt.setString(5, profile.getGender());
                            profileSt.setString(6, profile.getDriverLicenseNumber());
                            profileSt.executeUpdate();
                        }

                        try (PreparedStatement addressSt = connection.prepareStatement(insertAddressSql)) {
                            addressSt.setInt(1, newUserId);
                            addressSt.setString(2, address.getAddressLine());
                            addressSt.setString(3, address.getCity());
                            addressSt.setString(4, address.getProvince());
                            addressSt.setString(5, address.getPostalCode());
                            addressSt.setString(6, address.getCountry());
                            addressSt.executeUpdate();
                        }

                        connection.commit();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = findUserByEmail(email);
        if (user == null) return false;

        String sql = "UPDATE [USER] SET PASSWORD = ? WHERE EMAIL = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, SecurityUtils.hashPassword(newPassword));
            st.setString(2, email);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE [USER] SET PASSWORD = ?, RESET_TOKEN = NULL, TOKEN_EXPIRY = NULL WHERE USER_ID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, SecurityUtils.hashPassword(newPassword)); // Mã hóa mật khẩu mới
            st.setInt(2, userId);
            int rowsAffected = st.executeUpdate();
            System.out.println("Update password for User ID " + userId + ". Rows affected: " + rowsAffected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}