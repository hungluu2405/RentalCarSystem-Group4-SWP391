package controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.account.RecoveryService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    private final RecoveryService.ForgotPasswordService service = new RecoveryService.ForgotPasswordService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        User user = service.findUserByEmail(email);
        if (user == null) {
            request.setAttribute("error", "Không tìm thấy địa chỉ email.");
            request.getRequestDispatcher("view/account/forgot-password.jsp").forward(request, response);
            return;
        }

        // Gửi mã OTP
        service.sendResetCode(email);

        // Chuyển hướng sang trang xác minh mã
        response.sendRedirect(request.getContextPath() + "/verify-code?email=" + email);
    }
}
