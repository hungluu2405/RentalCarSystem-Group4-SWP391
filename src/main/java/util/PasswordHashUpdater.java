package util;

import dao.DBContext;
import dao.implement.UserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PasswordHashUpdater {
    public static void main(String[] args) {
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();

            String selectSQL = "SELECT USER_ID, PASSWORD FROM [USER]";
            String updateSQL = "UPDATE [USER] SET PASSWORD = ? WHERE USER_ID = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            ResultSet rs = selectStmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                int userId = rs.getInt("USER_ID");
                String plainPassword = rs.getString("PASSWORD");

                // Chỉ hash nếu mật khẩu chưa được mã hóa (độ dài ngắn)
                if (plainPassword != null && plainPassword.length() < 64) {
                    String hashed = SecurityUtils.hashPassword(plainPassword);
                    PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
                    updateStmt.setString(1, hashed);
                    updateStmt.setInt(2, userId);
                    updateStmt.executeUpdate();
                    count++;
                }
            }

            System.out.println("Đã cập nhật " + count + " mật khẩu sang dạng hash SHA-256.");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
