package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.User;
import model.UserProfile;
import util.EmailUtil;
import util.ResetCodeStore;

@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 🟩 Lấy thêm username
        String username = request.getParameter("username");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("driver_license_number");
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");
        String roleParam = request.getParameter("role_id");

        // 🟩 Kiểm tra trống
        if (username == null || username.isEmpty()
                || email == null || email.isEmpty()
                || password == null || password.isEmpty()
                || fullName == null || fullName.isEmpty()
                || phone == null || phone.isEmpty()
                || dobString == null || dobString.isEmpty()
                || gender == null || gender.isEmpty()
                || licenseNumber == null || licenseNumber.isEmpty()
                || addressLine == null || addressLine.isEmpty()
                || city == null || city.isEmpty()
                || country == null || country.isEmpty()
                || roleParam == null || roleParam.isEmpty()) {

            request.setAttribute("error", "Please fill in all required fields!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // 🟩 Validate username (chỉ cho chữ, số, _ , từ 4–20 ký tự)
        if (!username.matches("^[a-zA-Z0-9_]{4,20}$")) {
            request.setAttribute("error", "Username must be 4–20 characters and contain only letters, numbers or underscore!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("error", "Invalid email format!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Validate password
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Validate phone number
        if (!phone.matches("^\\d{9,11}$")) {
            request.setAttribute("error", "Phone number must contain 9 to 11 digits!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Validate and parse date of birth
        Date dob;
        try {
            dob = Date.valueOf(dobString);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date of birth format! Use yyyy-MM-dd.");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Check if age >= 18
        LocalDate birthDate = dob.toLocalDate();
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            request.setAttribute("error", "You must be at least 18 years old to register!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Validate gender
        if (!gender.equalsIgnoreCase("male") && !gender.equalsIgnoreCase("female") && !gender.equalsIgnoreCase("other")) {
            request.setAttribute("error", "Invalid gender value!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Validate driver’s license number (Vietnam standard: 12 digits)
        if (!licenseNumber.matches("^\\d{12}$")) {
            request.setAttribute("error", "Driver’s license number must contain exactly 12 digits!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }
        
        // 🟩 Parse roleId
        int roleId;
        try {
            roleId = Integer.parseInt(roleParam);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid role selected!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }


        UserDAO userDAO = new UserDAO();

        // 🟩 Kiểm tra trùng username
        if (userDAO.findUserByUsername(username) != null) {
            request.setAttribute("error", "This username is already taken!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Check if email already exists
        if (userDAO.findUserByEmail(email) != null) {
            request.setAttribute("error", "This email is already registered!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // 🟩 Gán username cho User
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoleId(roleId);

        UserProfile profile = new UserProfile(fullName, phone, dob, gender, licenseNumber);
        Address address = new Address(addressLine, city, city, postalCode, country);

        // Generate and send OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        ResetCodeStore.saveCode(email, otp);

        EmailUtil.sendEmail(email, "Account Verification - Rentaly",
                "Your verification code is: <h2><b>" + otp + "</b></h2>");

        // Save temporary data in session
        HttpSession session = request.getSession();
        session.setAttribute("temp_user", user);
        session.setAttribute("temp_profile", profile);
        session.setAttribute("temp_address", address);

        // Redirect to email verification page
        response.sendRedirect(request.getContextPath() + "/verify-email?email=" + email);
    }
}
