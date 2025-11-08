package controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.account.ResetPasswordService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    private final ResetPasswordService resetPasswordService = new ResetPasswordService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("reset_email") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        String email = (session != null) ? (String) session.getAttribute("reset_email") : null;

        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");

        // ✅ Kiểm tra input
        String error = resetPasswordService.validatePassword(password, rePassword);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
            return;
        }

        // ✅ Cập nhật mật khẩu
        boolean success = resetPasswordService.resetPassword(email, password);

        if (success) {
            session.removeAttribute("reset_email");
            response.sendRedirect(request.getContextPath() + "/login?reset=success");
        } else {
            request.setAttribute("error", "No matching user found!");
            request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
        }
    }
}
