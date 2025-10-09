
package controller.account;


import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy session hiện tại (không tạo mới nếu chưa có)
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Xóa tất cả các thuộc tính trong session và vô hiệu hóa nó
            session.invalidate();
        }
        
        // Chuyển hướng người dùng về trang chủ
        response.sendRedirect(request.getContextPath() + "/home");
    }
}