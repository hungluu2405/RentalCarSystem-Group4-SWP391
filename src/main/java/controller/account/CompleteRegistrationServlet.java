package controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.account.CompleteRegistrationService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/complete-registration"})
public class CompleteRegistrationServlet extends HttpServlet {

    private final CompleteRegistrationService completeRegistrationService = new CompleteRegistrationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // ✅ Dùng service để kiểm tra googleUser
        if (!completeRegistrationService.hasGoogleUser(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Hiển thị form hoàn tất đăng ký
        request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
    }
}
