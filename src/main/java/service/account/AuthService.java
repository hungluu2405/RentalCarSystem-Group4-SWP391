package service.account;

import dao.implement.UserDAO;
import jakarta.servlet.http.HttpSession;
import model.User;
import util.EmailUtil;
import util.VerificationCodeStore;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

public class AuthService {

    public static class LoginService {

        private final UserDAO userDAO;

        public LoginService() {
            this.userDAO = new UserDAO();
        }

        /**
         * Kiểm tra thông tin đăng nhập theo email hoặc tên đăng nhập.
         */
        public User authenticate(String loginKey, String password) {
            if (loginKey == null || loginKey.isEmpty() || password == null || password.isEmpty()) {
                return null;
            }
            return userDAO.checkLoginByEmailOrUsername(loginKey, password);
        }

        /**
         * Trả về thông báo lỗi nếu có lỗi trong input.
         */
        public String validateInput(String loginKey, String password) {
            if (loginKey == null || loginKey.isEmpty()) {
                return "Email hoặc tên đăng nhập không được để trống!";
            }
            if (password == null || password.isEmpty()) {
                return "Mật khẩu không được để trống!";
            }
            return null;
        }
    }

    /**
     * Xử lý đăng xuất người dùng.
     */
    public static class LogoutService {
        public void logout(HttpSession session) {
            if (session != null) {
                session.invalidate();
            }
        }
    }

    public static class RegisterService {

        private final UserDAO userDAO = new UserDAO();

        /**
         * Kiểm tra dữ liệu nhập khi đăng ký.
         */
        public String validateInput(String username, String email, String password, String rePassword,
                                    String fullName, String phone, String dobString, String gender,
                                    String addressLine, String city,
                                    String country, String roleParam) {

            if (username == null || username.isEmpty()
                    || email == null || email.isEmpty()
                    || password == null || password.isEmpty()
                    || rePassword == null || rePassword.isEmpty()
                    || fullName == null || fullName.isEmpty()
                    || phone == null || phone.isEmpty()
                    || dobString == null || dobString.isEmpty()
                    || gender == null || gender.isEmpty()
                    || addressLine == null || addressLine.isEmpty()
                    || city == null || city.isEmpty()
                    || country == null || country.isEmpty()
                    || roleParam == null || roleParam.isEmpty()) {
                return "Vui lòng điền đầy đủ tất cả các trường bắt buộc!";
            }

            if (!password.equals(rePassword)) {
                return "Mật khẩu nhập lại không khớp!";
            }

            if (!username.matches("^[a-zA-Z0-9_]{4,20}$")) {
                return "Tên đăng nhập phải từ 4–20 ký tự và chỉ chứa chữ, số hoặc dấu gạch dưới!";
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return "Định dạng email không hợp lệ!";
            }

            if (password.length() < 6) {
                return "Mật khẩu phải có ít nhất 6 ký tự!";
            }

            if (!phone.matches("^\\d{9,11}$")) {
                return "Số điện thoại phải gồm từ 9 đến 11 chữ số!";
            }

            Date dob;
            try {
                dob = Date.valueOf(dobString);
            } catch (IllegalArgumentException e) {
                return "Ngày sinh không hợp lệ!";
            }

            int age = Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
            if (age < 18 || age > 70) {
                return "Độ tuổi hợp lệ để đăng ký là từ 18 đến 70 tuổi!";
            }

            if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female") && !gender.equalsIgnoreCase("other")) {
                return "Giới tính không hợp lệ!";
            }

            try {
                Integer.parseInt(roleParam);
            } catch (NumberFormatException e) {
                return "Vai trò được chọn không hợp lệ!";
            }

            return null;
        }

        /**
         * Kiểm tra username hoặc email trùng lặp.
         */
        public String checkDuplicate(String username, String email) {
            if (userDAO.findUserByUsername(username) != null) {
                return "Tên đăng nhập này đã được sử dụng!";
            }
            if (userDAO.findUserByEmail(email) != null) {
                return "Email này đã được đăng ký!";
            }
            return null;
        }

        /**
         * Gửi mã OTP xác thực đến email.
         */
        public void registerTempUser(String email) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            VerificationCodeStore.saveCode(email, otp);

            EmailUtil.sendEmail(email, "Mã xác minh tài khoản Rentaly",
                    "Mã xác minh của bạn là: <h2><b>" + otp + "</b></h2>");
        }
    }
}
