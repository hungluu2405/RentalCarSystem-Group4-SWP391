package service.account;

import dao.implement.UserDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import model.Address;
import model.User;
import model.UserProfile;
import util.EmailUtil;
import util.ResetCodeStore;

public class RegisterService {

    private final UserDAO userDAO = new UserDAO();

    /** ✅ Kiểm tra dữ liệu nhập */
    public String validateInput(String username, String email, String password, String rePassword,
                                String fullName, String phone, String dobString, String gender,
                                String licenseNumber, String addressLine, String city,
                                String country, String roleParam) {
        if (username == null || username.isEmpty()
                || email == null || email.isEmpty()
                || password == null || password.isEmpty()
                || rePassword == null || rePassword.isEmpty()
                || fullName == null || fullName.isEmpty()
                || phone == null || phone.isEmpty()
                || dobString == null || dobString.isEmpty()
                || gender == null || gender.isEmpty()
                || licenseNumber == null || licenseNumber.isEmpty()
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

        if (!licenseNumber.matches("^\\d{8,12}$"))
            return "Driver’s license number must contain 8 to 12 digits!";

        try {
            Integer.parseInt(roleParam);
        } catch (NumberFormatException e) {
            return "Invalid role selected!";
        }

        return null;
    }

    /** ✅ Kiểm tra username/email trùng */
    public String checkDuplicate(String username, String email) {
        if (userDAO.findUserByUsername(username) != null)
            return "This username is already taken!";
        if (userDAO.findUserByEmail(email) != null)
            return "This email is already registered!";
        return null;
    }

    /** ✅ Tạo User, Profile, Address và gửi OTP xác thực */
    public void registerTempUser(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        ResetCodeStore.saveCode(email, otp);

        EmailUtil.sendEmail(email, "Account Verification - Rentaly",
                "Your verification code is: <h2><b>" + otp + "</b></h2>");
    }
}
