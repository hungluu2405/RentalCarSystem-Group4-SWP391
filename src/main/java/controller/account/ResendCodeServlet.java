package controller.account;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.account.ResendCodeService;

@WebServlet(urlPatterns = {"/resend-code"})
public class ResendCodeServlet extends HttpServlet {

    private final ResendCodeService resendCodeService = new ResendCodeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String type = request.getParameter("type"); // 👈 Xác định trang gọi resend

        boolean success = resendCodeService.resendVerificationCode(email);

        String redirectTarget;
        if ("reset".equalsIgnoreCase(type)) {
            redirectTarget = "/verify-code?email=" + email;
        } else {
            redirectTarget = "/verify-email?email=" + email;
        }

        // ✅ Redirect về trang tương ứng kèm message
        if (success) {
            response.sendRedirect(request.getContextPath() + redirectTarget + "&msg=resent");
        } else {
            response.sendRedirect(request.getContextPath() + redirectTarget + "&msg=failed");
        }
    }
}
