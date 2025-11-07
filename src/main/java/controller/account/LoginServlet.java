package controller.account;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.account.LoginService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final LoginService loginService = new LoginService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("view/account/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String loginKey = request.getParameter("loginKey");
        String password = request.getParameter("password");

        Map<String, String> formData = new HashMap<>();
        formData.put("loginKey", loginKey);
        request.setAttribute("formData", formData);

        // ✅ Kiểm tra input
        String error = loginService.validateInput(loginKey, password);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("view/account/login.jsp").forward(request, response);
            return;
        }

        // ✅ Xác thực tài khoản
        User user = loginService.authenticate(loginKey, password);

        if (user == null) {
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("view/account/login.jsp").forward(request, response);
            return;
        }

        // ✅ Lưu vào session và điều hướng
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        int role = user.getRoleId();
        if (role == 1) {
            response.sendRedirect(request.getContextPath() + "/accountDB"); // Admin
        } else {
            response.sendRedirect(request.getContextPath() + "/home"); // Người dùng thường
        }
    }
}
