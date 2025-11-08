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
         * Kiểm tra thông tin đăng nhập theo email hoặc username.
         *
         * @param loginKey email hoặc username
         * @param password mật khẩu người dùng nhập
         * @return User nếu đăng nhập thành công, null nếu thất bại
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
                return "Email or username cannot be empty!";
            }
            if (password == null || password.isEmpty()) {
                return "Password cannot be empty!";
            }
            return null;
        }
    }

    /**
     * LogoutService - Xử lý logic đăng xuất người dùng.
     */
    public static class LogoutService {

        /**
         * ✅ Xóa session hiện tại và đăng xuất người dùng.
         *
         * @param session phiên làm việc hiện tại
         */
        public void logout(HttpSession session) {
            if (session != null) {
                // Có thể mở rộng thêm: ghi log, cập nhật DB, v.v.
                session.invalidate();
            }
        }
    }

    public static class RegisterService {

        private final UserDAO userDAO = new UserDAO();

        /**
         * ✅ Kiểm tra dữ liệu nhập
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
                return "Please fill in all required fields!";
            }

            if (!password.equals(rePassword))
                return "Passwords do not match!";

            if (!username.matches("^[a-zA-Z0-9_]{4,20}$"))
                return "Username must be 4–20 characters and contain only letters, numbers or underscore!";

            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
                return "Invalid email format!";

            if (password.length() < 6)
                return "Password must be at least 6 characters long!";

            if (!phone.matches("^\\d{9,11}$"))
                return "Phone number must contain 9 to 11 digits!";

            Date dob;
            try {
                dob = Date.valueOf(dobString);
            } catch (IllegalArgumentException e) {
                return "Invalid date of birth format!";
            }

            int age = Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
            if (age < 18)
                return "You must be at least 18 years old to register!";

            if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female") && !gender.equalsIgnoreCase("other"))
                return "Invalid gender value!";


            try {
                Integer.parseInt(roleParam);
            } catch (NumberFormatException e) {
                return "Invalid role selected!";
            }

            return null;
        }

        /**
         * ✅ Kiểm tra username/email trùng
         */
        public String checkDuplicate(String username, String email) {
            if (userDAO.findUserByUsername(username) != null)
                return "This username is already taken!";
            if (userDAO.findUserByEmail(email) != null)
                return "This email is already registered!";
            return null;
        }

        /**
         * ✅ Tạo User, Profile, Address và gửi OTP xác thực
         */
        public void registerTempUser(String email) {
            String otp = String.format("%06d", new Random().nextInt(999999));
            VerificationCodeStore.saveCode(email, otp);

            EmailUtil.sendEmail(email, "Your Rentaly Verification Code",
                    "Your verification code is: <h2><b>" + otp + "</b></h2>");
        }

    }
}
