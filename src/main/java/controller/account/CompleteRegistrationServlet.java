package controller.account;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GoogleUser;

@WebServlet(urlPatterns = {"/complete-registration"})
public class CompleteRegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check if googleUser exists in session
        if (session == null || session.getAttribute("googleUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Show form to complete registration
        request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("googleUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get submitted data
        String fullName = request.getParameter("fullname");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");
        String ageStr = request.getParameter("age");

        // Validate inputs
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Full name cannot be empty!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        if (!password.equals(rePassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        // Check valid age
        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Age must be a number!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        if (age < 18) {
            request.setAttribute("error", "You must be at least 18 years old to register!");
            request.getRequestDispatcher("view/account/complete-registration.jsp").forward(request, response);
            return;
        }

        // If everything is valid, continue saving info to database (you can add logic here)
        GoogleUser googleUser = (GoogleUser) session.getAttribute("googleUser");
        // save googleUser + other info to DB here...

        session.removeAttribute("googleUser");
        response.sendRedirect(request.getContextPath() + "/login?success=1");
    }
}
