package controller;

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

        if (!newPass.equals(confirmPass)) {
            request.setAttribute("error", "New passwords do not match!");
            request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
            return;
        }

        UserDAO dao = new UserDAO();
        boolean success = dao.changePassword(user.getEmail(), oldPass, newPass);

        if (success) {
            request.setAttribute("message", "Password changed successfully!");
        } else {
            request.setAttribute("error", "Old password incorrect!");
        }

        request.getRequestDispatcher("view/account/change-password.jsp").forward(request, response);
    }
}
