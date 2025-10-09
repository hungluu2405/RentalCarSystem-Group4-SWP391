package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import java.sql.Date;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        // ... Lấy tất cả các trường khác từ form ...
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("driver_license_number");
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");
        
        UserDAO userDAO = new UserDAO();
        if (userDAO.findUserByEmail(email) != null) {
            request.setAttribute("error", "Email already exists!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        Date dob = (dobString != null && !dobString.isEmpty()) ? Date.valueOf(dobString) : null;
        UserProfile profile = new UserProfile(fullName, phone, dob, gender, licenseNumber);
        Address address = new Address(addressLine, city, city, postalCode, country);

        String otp = String.format("%06d", new Random().nextInt(999999));
        ResetCodeStore.saveCode(email, otp);

        EmailUtil.sendEmail(email, "Verify Your Email for Rentaly", "Your verification code is: <h2><b>" + otp + "</b></h2>");
        
        HttpSession session = request.getSession();
        session.setAttribute("temp_user", user);
        session.setAttribute("temp_profile", profile);
        session.setAttribute("temp_address", address);

        response.sendRedirect(request.getContextPath() + "/verify-email?email=" + email);
    }
}