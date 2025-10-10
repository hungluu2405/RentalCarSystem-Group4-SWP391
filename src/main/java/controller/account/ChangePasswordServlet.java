package controller.account;

import dao.implement.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirmPass = request.getParameter("confirmPassword");

        // --- Password validation ---
        if (newPass == null || newPass.isEmpty()) {
            request.setAttribute("error", "New password cannot be empty!");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }

        if (newPass.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long!");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }

        if (!newPass.equals(confirmPass)) {
            request.setAttribute("error", "Confirm password does not match!");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }

        // --- Update password ---
        UserDAO dao = new UserDAO();
        boolean success = dao.changePassword(user.getEmail(), oldPass, newPass);

        if (success) {
            request.setAttribute("message", "Password changed successfully!");
        } else {
            request.setAttribute("error", "Incorrect old password!");
        }

        request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
    }
}
