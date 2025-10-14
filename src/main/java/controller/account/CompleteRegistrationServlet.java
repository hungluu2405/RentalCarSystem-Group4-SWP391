package controller.account;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
}
