package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import java.sql.Date;
import java.util.regex.*;
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

        // --- Lấy thông tin từ form ---
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

        // --- Kiểm tra mật khẩu ---
        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "Password cannot be empty!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        if (!password.equals(rePassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu
        if (password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // --- Kiểm tra role ---
        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Please select a valid role.");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        // --- Chuyển đổi ngày sinh ---
        Date dob = (dobString != null && !dobString.isEmpty()) ? Date.valueOf(dobString) : null;

        // --- Tạo các model ---
        User user = new User();
        user.setEmail(googleUser.getEmail());
        user.setPassword(password); // Sẽ được hash trong DAO
        user.setRoleId(roleId);

        UserProfile profile = new UserProfile();
        profile.setFullName(googleUser.getName());
        profile.setPhone(phone);
        profile.setDob(dob);
        profile.setGender(gender);
        profile.setDriverLicenseNumber(licenseNumber);

        Address address = new Address(addressLine, city, city, postalCode, country);

        // --- Gọi DAO ---
        UserDAO userDAO = new UserDAO();
        boolean isSuccess = userDAO.registerUser(user, profile, address);

        if (isSuccess) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?register=success");
        } else {
            request.setAttribute("error", "Error creating account. Please try again.");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
        }
    }
}
