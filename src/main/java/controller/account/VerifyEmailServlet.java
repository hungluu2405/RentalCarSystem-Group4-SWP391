package controller.account;

import dao.implement.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.User;
import model.UserProfile;
import util.ResetCodeStore;

@WebServlet(urlPatterns = {"/verify-email"})
public class VerifyEmailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/account/verify-email.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String code = request.getParameter("code");

        if (ResetCodeStore.validateCode(email, code)) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("temp_user");
            UserProfile profile = (UserProfile) session.getAttribute("temp_profile");
            Address address = (Address) session.getAttribute("temp_address");
            
            if (user != null) {
                UserDAO userDAO = new UserDAO();
                boolean isSuccess = userDAO.registerUser(user, profile, address);

                if (isSuccess) {
                    session.invalidate(); // Xóa toàn bộ session tạm
                    response.sendRedirect(request.getContextPath() + "/login?register=success");
                } else {
                    request.setAttribute("error", "Error creating account. Please try registering again.");
                    request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
                }
            } else {
                 request.setAttribute("error", "Session expired. Please register again.");
                 request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Incorrect or expired code.");
            request.getRequestDispatcher("view/account/verify-email.jsp").forward(request, response);
        }
    }
}