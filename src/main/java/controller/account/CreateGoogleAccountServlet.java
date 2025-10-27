package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.GoogleUser;
import model.User;
import model.UserProfile;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/create-google-account"})
public class CreateGoogleAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        GoogleUser googleUser = (GoogleUser) session.getAttribute("googleUser");

        if (googleUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        // 🟩 Lấy thêm username
        String username = request.getParameter("username");

        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("driver_license_number");
        String roleIdStr = request.getParameter("role_id");
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");

        Map<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("phone", phone);
        formData.put("dob", dobString);
        formData.put("gender", gender);
        formData.put("driver_license_number", licenseNumber);
        formData.put("address_line", addressLine);
        formData.put("city", city);
        formData.put("country", country);
        formData.put("postal_code", postalCode);
        formData.put("role_id", roleIdStr);
        request.setAttribute("formData", formData);

        // 🟩 Kiểm tra username
        if (username == null || username.isEmpty()) {
            forwardWithError("Username cannot be empty!", request, response);
            return;
        }

        if (!username.matches("^[a-zA-Z0-9_]{4,20}$")) {
            forwardWithError("Username must be 4–20 characters and contain only letters, numbers, or underscore!", request, response);
            return;
        }

        // --- Kiểm tra mật khẩu ---
        if (password == null || password.isEmpty()) {
            forwardWithError("Password cannot be empty!", request, response);
            return;
        }

        if (!password.equals(rePassword)) {
            forwardWithError("Passwords do not match!", request, response);
            return;
        }

        if (password.length() < 6) {
            forwardWithError("Password must be at least 6 characters long!", request, response);
            return;
        }

        // --- Kiểm tra role ---
        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (NumberFormatException e) {
            forwardWithError("Please select a valid role.", request, response);
            return;
        }

        // --- Chuyển đổi ngày sinh ---
        Date dob = (dobString != null && !dobString.isEmpty()) ? Date.valueOf(dobString) : null;

        // --- Validate gender ---
        if (gender != null && !gender.isEmpty() &&
            !gender.equalsIgnoreCase("male") &&
            !gender.equalsIgnoreCase("female") &&
            !gender.equalsIgnoreCase("other")) {
            forwardWithError("Invalid gender value!", request, response);
            return;
        }

        // --- Validate license number ---
        if (licenseNumber != null && !licenseNumber.isEmpty() && !licenseNumber.matches("^\\d{12}$")) {
            forwardWithError("Driver’s license number must contain exactly 12 digits!", request, response);
            return;
        }

        // --- Kiểm tra username trùng ---
        UserDAO userDAO = new UserDAO();
        if (userDAO.findUserByUsername(username) != null) {
            forwardWithError("This username is already taken!", request, response);
            return;
        }

        // --- Kiểm tra email trùng ---
        if (userDAO.findUserByEmail(googleUser.getEmail()) != null) {
            forwardWithError("This email is already registered!", request, response);
            return;
        }

        // --- Tạo các model ---
        User user = new User();
        user.setUsername(username);
        user.setEmail(googleUser.getEmail());
        user.setPassword(password); // DAO sẽ hash
        user.setRoleId(roleId);

        UserProfile profile = new UserProfile();
        profile.setFullName(googleUser.getName());
        profile.setPhone(phone);
        profile.setDob(dob);
        profile.setGender(gender);
        profile.setDriverLicenseNumber(licenseNumber);

        Address address = new Address(addressLine, city, city, postalCode, country);

        // --- Gọi DAO ---
        boolean isSuccess = userDAO.registerUser(user, profile, address);

        if (isSuccess) {
            User newUser = userDAO.findUserByEmail(googleUser.getEmail());
            session.removeAttribute("googleUser");
            session.invalidate();

            if (newUser != null) {
                HttpSession newSession = request.getSession(true);
                newSession.setAttribute("user", newUser);
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        } else {
            forwardWithError("Error creating account. Please try again.", request, response);
        }
    }

    private void forwardWithError(String message, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
    }
}
