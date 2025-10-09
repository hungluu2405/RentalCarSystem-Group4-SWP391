package controller;

import dao.implement.UserDAO;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Address;
import model.User;
import model.UserProfile;
import util.SecurityUtils; // Đảm bảo bạn đã có lớp này

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // --- Lấy tất cả thông tin từ form ---
        // 1. Thông tin tài khoản
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");

        // 2. Thông tin cá nhân
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("driver_license_number");

        // 3. Thông tin địa chỉ
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");

        // --- Kiểm tra dữ liệu ---
        if (!password.equals(rePassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.findUserByEmail(email) != null) {
            request.setAttribute("error", "Email already exists!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // --- Tạo các đối tượng Model ---
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // DAO sẽ mã hóa mật khẩu này

        Date dob = (dobString != null && !dobString.isEmpty()) ? Date.valueOf(dobString) : null;
        UserProfile profile = new UserProfile(fullName, phone, dob, gender, licenseNumber);

        // Giả sử province và city là một
        Address address = new Address(addressLine, city, city, postalCode, country);

        // --- Gọi DAO với đủ 3 tham số ---
        boolean isSuccess = userDAO.registerUser(user, profile, address);

        // --- Xử lý kết quả ---
        if (isSuccess) {
            response.sendRedirect(request.getContextPath() + "/login?register=success");
        } else {
            request.setAttribute("error", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
        }
    }
}