package controller.account;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.User;
import model.UserProfile;
import service.account.RegisterService;

@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final RegisterService registerService = new RegisterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ✅ Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");
        String roleParam = request.getParameter("role_id");

        // ✅ Lưu lại dữ liệu để tránh mất form khi lỗi
        Map<String, String> formData = new HashMap<>();
        formData.put("username", username);
        formData.put("email", email);
        formData.put("full_name", fullName);
        formData.put("phone", phone);
        formData.put("dob", dobString);
        formData.put("gender", gender);
        formData.put("address_line", addressLine);
        formData.put("city", city);
        formData.put("country", country);
        formData.put("postal_code", postalCode);
        formData.put("role_id", roleParam);
        request.setAttribute("formData", formData);

        // ✅ Validate dữ liệu
        String error = registerService.validateInput(username, email, password, rePassword, fullName,
                phone, dobString, gender, addressLine, city, country, roleParam);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // ✅ Check trùng username/email
        String duplicateError = registerService.checkDuplicate(username, email);
        if (duplicateError != null) {
            request.setAttribute("error", duplicateError);
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // ✅ Tạo object tạm
        int roleId = Integer.parseInt(roleParam);
        Date dob = Date.valueOf(dobString);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoleId(roleId);

        UserProfile profile = new UserProfile(fullName, phone, dob, gender);
        Address address = new Address(addressLine, city, city, postalCode, country);

        // ✅ Gửi OTP xác thực
        registerService.registerTempUser(email);

        // ✅ Lưu tạm vào session
        HttpSession session = request.getSession();
        session.setAttribute("temp_user", user);
        session.setAttribute("temp_profile", profile);
        session.setAttribute("temp_address", address);

        response.sendRedirect(request.getContextPath() + "/verify-email?email=" + email);
    }
}
