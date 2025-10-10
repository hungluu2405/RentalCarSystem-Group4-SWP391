package controller.account;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GoogleUser;

// Đăng ký servlet với URL /complete-registration
@WebServlet(urlPatterns = {"/complete-registration"})
public class CompleteRegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Kiểm tra xem có thông tin googleUser trong session không
        // Nếu không có, tức là người dùng truy cập trực tiếp, chuyển họ về trang login
        if (session == null || session.getAttribute("googleUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Nếu có, hiển thị trang để họ điền nốt thông tin
        request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
    }
}