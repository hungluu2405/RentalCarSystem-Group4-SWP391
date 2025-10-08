package controller.authentication;

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
        // Lấy thông tin tài khoản
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");

        // Lấy thông tin cá nhân
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("driver_license_number");

        // Lấy thông tin địa chỉ
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");

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

        String roleIdStr = request.getParameter("role_id");
        int roleId = 3; // mặc định là Customer
        if (roleIdStr != null && !roleIdStr.isEmpty()) {
            roleId = Integer.parseInt(roleIdStr);
        }

        String hashedPassword = UserDAO.hashPassword(password);
        // Tạo các đối tượng model
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRoleId(roleId);

        Date dob = (dobString != null && !dobString.isEmpty()) ? Date.valueOf(dobString) : null;
        UserProfile profile = new UserProfile(fullName, phone, dob, gender, licenseNumber);

        Address address = new Address(addressLine, city, city, postalCode, country); // Dùng city cho cả city và province

        boolean isSuccess = userDAO.registerUser(user, profile, address);

        if (isSuccess) {
            response.sendRedirect(request.getContextPath() + "/login?register=success");
        } else {
            request.setAttribute("error", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
        }
    }
}
