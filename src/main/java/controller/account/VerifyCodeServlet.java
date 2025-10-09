package controller.account;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.ResetCodeStore;

@WebServlet(urlPatterns = {"/verify-code"})
public class VerifyCodeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/account/verify-code.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String code = request.getParameter("code");

        if (ResetCodeStore.validateCode(email, code)) {
            HttpSession session = request.getSession();
            session.setAttribute("reset_email", email);
            response.sendRedirect(request.getContextPath() + "/reset-password");
        } else {
            request.setAttribute("error", "Incorrect code or code has expired.");
            request.getRequestDispatcher("view/account/verify-code.jsp?email=" + email).forward(request, response);
        }
    }
}