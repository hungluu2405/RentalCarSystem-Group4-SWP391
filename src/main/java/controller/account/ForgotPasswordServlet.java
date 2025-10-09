package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import util.EmailUtil;
import util.ResetCodeStore;

@WebServlet(urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/account/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findUserByEmail(email);

        if (user == null) {
            request.setAttribute("error", "Email address not found.");
            request.getRequestDispatcher("view/account/forgot-password.jsp").forward(request, response);
            return;
        }

        String otp = String.format("%06d", new Random().nextInt(999999));
        ResetCodeStore.saveCode(email, otp);

        String emailSubject = "Your Password Reset Code";
        String emailBody = "Your verification code is: <h2><b>" + otp + "</b></h2>";
        EmailUtil.sendEmail(email, emailSubject, emailBody);

        response.sendRedirect(request.getContextPath() + "/verify-code?email=" + email);
    }
}