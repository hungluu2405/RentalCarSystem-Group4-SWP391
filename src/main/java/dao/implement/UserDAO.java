package dao.implement;

import dao.GenericDAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.User;
import java.util.List;
import java.util.Map;
import model.Address;
import model.UserProfile;
import util.SecurityUtils;
import model.Role;

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

    public User findUserByEmail(String email) {
        String sql = """
        SELECT 
            u.USER_ID, u.USER_NAME, u.EMAIL, u.PASSWORD, 
            u.ROLE_ID, r.NAME AS ROLE_NAME,
            p.FULL_NAME
        FROM [USER] u
        LEFT JOIN ROLE r ON u.ROLE_ID = r.ROLE_ID
        LEFT JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID
        WHERE u.EMAIL = ?
    """;

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("USER_ID"));
                user.setUsername(rs.getString("USER_NAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setRoleId(rs.getInt("ROLE_ID"));
                user.setRoleName(rs.getString("ROLE_NAME"));

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

    public User checkLoginByEmailOrUsername(String loginKey, String password) {
        String sql = "SELECT u.*, p.FULL_NAME, r.NAME as ROLE_NAME "
                + "FROM [USER] u "
                + "JOIN USER_PROFILE p ON u.USER_ID = p.USER_ID "
                + "JOIN ROLE r ON u.ROLE_ID = r.ROLE_ID "
                + "WHERE u.EMAIL = ? OR u.USER_NAME = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, loginKey);
            st.setString(2, loginKey);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("PASSWORD");
                String inputHash = SecurityUtils.hashPassword(password);

                if (storedHash.equals(inputHash)) {
                    User user = new User();
                    user.setUserId(rs.getInt("USER_ID"));
                    user.setUsername(rs.getString("USER_NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setRoleId(rs.getInt("ROLE_ID"));

                    UserProfile profile = new UserProfile();
                    profile.setFullName(rs.getString("FULL_NAME"));
                    user.setUserProfile(profile);
                    user.setRoleName(rs.getString("ROLE_NAME"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- CÁC PHƯƠNG THỨC ĐĂNG KÝ VÀ CẬP NHẬT ---
    public boolean registerUser(User user, UserProfile profile, Address address) {
        if (findUserByEmail(user.getEmail()) != null || findUserByUsername(user.getUsername()) != null) {
            return false;
        }

        String insertUserSql = "INSERT INTO [USER] (ROLE_ID, USER_NAME, EMAIL, PASSWORD, IS_EMAIL_VERIFIED) VALUES (?, ?, ?, ?, 1)";
        String insertProfileSql = "INSERT INTO USER_PROFILE (USER_ID, FULL_NAME, PHONE, DOB, GENDER, DRIVER_LICENSE_NUMBER) VALUES (?, ?, ?, ?, ?, ?)";
        String insertAddressSql = "INSERT INTO ADDRESS (USER_ID, ADDRESS_LINE, CITY, PROVINCE, POSTAL_CODE, COUNTRY) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            String hashedPassword = SecurityUtils.hashPassword(user.getPassword());

            try (PreparedStatement userSt = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                userSt.setInt(1, user.getRoleId());
                userSt.setString(2, user.getUsername());
                userSt.setString(3, user.getEmail());
                userSt.setString(4, hashedPassword);
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
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean changePassword(String email, String oldPassword, String newPassword) {
        User user = findUserByEmail(email);
        if (user == null) {
            return false;
        }

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
        String sql = "UPDATE [USER] SET PASSWORD = ? WHERE USER_ID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, SecurityUtils.hashPassword(newPassword)); // Mã hóa mật khẩu mới
            st.setInt(2, userId);
            int rowsAffected = st.executeUpdate();
            System.out.println("Update password for User ID " + userId + ". Rows affected: " + rowsAffected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsersForAdmin() {
        List<User> users = new ArrayList<>();

        String sql = """
            SELECT 
                u.USER_ID,
                u.ROLE_ID,
                u.EMAIL,
                u.IS_EMAIL_VERIFIED,
                u.CREATED_AT,
                up.PROFILE_ID,
                up.FULL_NAME,
                up.PHONE,
                up.DOB,
                up.GENDER,
                up.DRIVER_LICENSE_NUMBER,
                up.IS_VERIFIED,
                r.NAME AS ROLE_NAME
            FROM [CarRentalDB].[dbo].[USER] u
            LEFT JOIN [CarRentalDB].[dbo].[USER_PROFILE] up 
                ON u.USER_ID = up.USER_ID
            LEFT JOIN [CarRentalDB].[dbo].[ROLE] r 
                ON u.ROLE_ID = r.ROLE_ID
            ORDER BY u.USER_ID ASC
        """;

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                // ----- Tạo đối tượng User -----
                User user = new User();
                user.setUserId(rs.getInt("USER_ID"));
                user.setRoleId(rs.getInt("ROLE_ID"));
                user.setEmail(rs.getString("EMAIL"));
                user.setIsEmailVerified(rs.getBoolean("IS_EMAIL_VERIFIED"));
                user.setCreatedAt(rs.getTimestamp("CREATED_AT"));

                // ----- Tạo đối tượng UserProfile -----
                UserProfile profile = new UserProfile();
                profile.setProfileId(rs.getInt("PROFILE_ID"));
                profile.setUserId(rs.getInt("USER_ID"));
                profile.setFullName(rs.getString("FULL_NAME"));
                profile.setPhone(rs.getString("PHONE"));
                profile.setDob(rs.getDate("DOB"));
                profile.setGender(rs.getString("GENDER"));
                profile.setDriverLicenseNumber(rs.getString("DRIVER_LICENSE_NUMBER"));
                profile.setIsVerified(rs.getBoolean("IS_VERIFIED"));

                // Gán profile cho user
                user.setUserProfile(profile);

                // ----- Gán tên role -----
                user.setRoleName(rs.getString("ROLE_NAME"));

                // Thêm user vào danh sách
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM [USER] WHERE USERNAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("USER_ID"));
                user.setUsername(rs.getString("USER_NAME"));
                user.setEmail(rs.getString("EMAIL"));
                user.setPassword(rs.getString("PASSWORD"));
                user.setRoleId(rs.getInt("ROLE_ID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countAllUsers(){
        String sql = "SELECT COUNT(*) AS total FROM [USER_PROFILE]";
        try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()){
            if (rs.next()){
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

}
