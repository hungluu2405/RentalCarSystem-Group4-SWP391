package controller;

import dao.UserDAO;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp; // Import Timestamp cho createdAt
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Address; // Giả sử bạn có model này
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
        
        // --- 1. Lấy tất cả tham số từ request ---
        // Thông tin tài khoản
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");
        String roleIdStr = request.getParameter("role_id");

        // Thông tin cá nhân (Profile)
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String dobString = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String licenseNumber = request.getParameter("driver_license_number");

        // Thông tin địa chỉ (Address)
        String addressLine = request.getParameter("address_line");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String postalCode = request.getParameter("postal_code");

        // --- 2. Validation cơ bản ---
        if (!password.equals(rePassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.findUserByEmail(email) != null) {
            request.setAttribute("error", "Email này đã tồn tại!");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // --- 3. Xử lý và tạo các đối tượng Model ---
        
        // Tạo UserProfile trước
        UserProfile profile = new UserProfile();
        profile.setFullName(fullName);
        profile.setPhone(phone);
        profile.setGender(gender);
        profile.setDriverLicenseNumber(licenseNumber);
        profile.setIsVerified(false); // Mặc định là chưa xác thực khi mới đăng ký

        // Xử lý ngày sinh (Date of Birth) an toàn hơn
        try {
            if (dobString != null && !dobString.isEmpty()) {
                profile.setDob(Date.valueOf(dobString));
            }
        } catch (IllegalArgumentException e) {
            // Xử lý nếu định dạng ngày tháng không hợp lệ
            request.setAttribute("error", "Định dạng ngày sinh không hợp lệ (YYYY-MM-DD).");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }
        
        // Tạo User
        User user = new User();
        user.setEmail(email);
        user.setPassword(UserDAO.hashPassword(password)); // Mã hóa mật khẩu
        user.setIsEmailVerified(true); // Mặc định là chưa xác thực email
        user.setCreatedAt(new Timestamp(System.currentTimeMillis())); // Đặt thời gian tạo
        
        // Mặc định role là 3 (Customer) nếu không được cung cấp
        int roleId = (roleIdStr != null && !roleIdStr.isEmpty()) ? Integer.parseInt(roleIdStr) : 3;
        user.setRoleId(roleId);
        
        // *** Quan trọng: Liên kết UserProfile vào User ***
        user.setUserProfile(profile);

        // Tạo Address (Nếu có)
        // LƯU Ý: Constructor của bạn đang dùng 'city' 2 lần. Hãy kiểm tra lại lớp Address.
        // new Address(addressLine, city, city, postalCode, country)
        Address address = new Address(addressLine, city, city, postalCode, country);

        // --- 4. Gọi DAO để lưu vào CSDL ---
        // Giả sử phương thức registerUser đã được cập nhật để nhận User và Address
        boolean isSuccess = userDAO.registerUser(user, address);

        if (isSuccess) {
            response.sendRedirect(request.getContextPath() + "/login?register=success");
        } else {
            request.setAttribute("error", "Đã có lỗi xảy ra trong quá trình đăng ký. Vui lòng thử lại.");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
        }
    }
}