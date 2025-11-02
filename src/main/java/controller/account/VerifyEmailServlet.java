package controller.account;

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
import service.account.VerifyEmailService;

@WebServlet(urlPatterns = {"/verify-email"})
public class VerifyEmailServlet extends HttpServlet {

    private final VerifyEmailService verifyEmailService = new VerifyEmailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/verify-email.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String code = request.getParameter("code");

        if (!verifyEmailService.verifyCode(email, code)) {
            request.setAttribute("error", "Incorrect or expired code.");
            request.getRequestDispatcher("view/account/verify-email.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("temp_user");
        UserProfile profile = (UserProfile) session.getAttribute("temp_profile");
        Address address = (Address) session.getAttribute("temp_address");

        // Kiểm tra session
        if (user == null) {
            request.setAttribute("error", "Session expired. Please register again.");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
            return;
        }

        // Gọi service để lưu vào DB
        boolean isSuccess = verifyEmailService.registerAfterVerification(user, profile, address);

        if (isSuccess) {
            session.removeAttribute("temp_user");
            session.removeAttribute("temp_profile");
            session.removeAttribute("temp_address");

            // Đăng nhập tự động sau xác thực thành công
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("error", "Error creating account. Please try registering again.");
            request.getRequestDispatcher("view/account/register.jsp").forward(request, response);
        }
    }

}
