package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form đổi mật khẩu
        request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 1. Kiểm tra xem người dùng đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Lấy các tham số từ form
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        // 3. Validation dữ liệu đầu vào chặt chẽ hơn
        if (newPass == null || newPass.trim().isEmpty()) {
            request.setAttribute("error", "Mật khẩu mới không được để trống.");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }
        
        if (!newPass.equals(confirmPass)) {
            request.setAttribute("error", "Mật khẩu mới và mật khẩu xác nhận không khớp.");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }
        
        // (Tùy chọn) Thêm các rule validation khác cho mật khẩu mới
        // ví dụ: độ dài tối thiểu, phải có chữ hoa, chữ thường, số...
        if (newPass.length() < 8) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 8 ký tự.");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }

        // 4. Gọi DAO để xử lý logic đổi mật khẩu
        UserDAO dao = new UserDAO();
        boolean success = dao.changePassword(user.getEmail(), oldPass, newPass);

        // 5. Xử lý kết quả
        if (success) {
            // Đổi mật khẩu thành công -> Hủy session cũ và yêu cầu đăng nhập lại
            // Đây là một phương pháp bảo mật tốt để đảm bảo mọi session cũ đều bị vô hiệu hóa.
            session.invalidate(); 
            
            // Dùng redirect để tránh lỗi F5 (resubmit form), và truyền thông báo qua URL parameter
            response.sendRedirect(request.getContextPath() + "/login?message=PasswordChangedSuccess");
        } else {
            // Đổi mật khẩu thất bại (sai mật khẩu cũ)
            request.setAttribute("error", "Mật khẩu cũ không chính xác.");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
        }
    }
}