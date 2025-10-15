package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet(urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

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

        // Check if both fields are filled
        if (password == null || password.isEmpty() || rePassword == null || rePassword.isEmpty()) {
            request.setAttribute("error", "Please enter both the new password and confirmation!");
            request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
            return;
        }

        // Check if passwords match
        if (!password.equals(rePassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long!");
            request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.findUserByEmail(email);

        if (user != null) {
            userDAO.updatePassword(user.getUserId(), password);
            session.removeAttribute("reset_email");
            response.sendRedirect(request.getContextPath() + "/login?reset=success");
        } else {
            request.setAttribute("error", "No matching user found!");
            request.getRequestDispatcher("view/account/reset-password.jsp").forward(request, response);
        }
    }
}
