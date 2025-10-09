package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    /**
     * Mã hóa một chuỗi bằng thuật toán SHA-256.
     * @param password Mật khẩu gốc
     * @return Chuỗi đã được mã hóa
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Trong thực tế, SHA-256 luôn tồn tại, nên lỗi này gần như không xảy ra
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}