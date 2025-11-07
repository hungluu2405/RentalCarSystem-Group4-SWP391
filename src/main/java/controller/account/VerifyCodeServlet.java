package controller.account;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.account.VerificationService;

@WebServlet(urlPatterns = {"/verify-code"})
public class VerifyCodeServlet extends HttpServlet {

    private final VerificationService.VerifyCodeService verifyCodeService = new VerificationService.VerifyCodeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msg = request.getParameter("msg");
        if ("resent".equals(msg)) {
            request.setAttribute("message", "A new code has been sent to your email.");
        } else if ("failed".equals(msg)) {
            request.setAttribute("error", "Failed to resend code. Please try again.");
        }

        request.getRequestDispatcher("view/account/verify-code.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String code = request.getParameter("code");

        boolean isValid = verifyCodeService.verifyCode(email, code);

        if (isValid) {
            HttpSession session = request.getSession();
            session.setAttribute("reset_email", email);
            response.sendRedirect(request.getContextPath() + "/reset-password");
        } else {
            request.setAttribute("error", "Incorrect code or code has expired.");
            request.getRequestDispatcher("view/account/verify-code.jsp?email=" + email).forward(request, response);
        }
    }
}
