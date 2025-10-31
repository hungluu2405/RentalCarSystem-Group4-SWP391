package controller.account;

import dao.implement.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.GoogleUser;
import model.User;
import service.account.LoginGoogleHandlerService;

@WebServlet(urlPatterns = {"/login-google-handler"})
public class LoginGoogleHandlerServlet extends HttpServlet {

    private final LoginGoogleHandlerService service = new LoginGoogleHandlerService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String accessToken = service.getAccessToken(code);
            GoogleUser googleUser = service.getGoogleUserInfo(accessToken);

            UserDAO dao = new UserDAO();
            User user = dao.findUserByEmail(googleUser.getEmail());
            HttpSession session = request.getSession();

            if (user == null) {
                session.setAttribute("googleUser", googleUser);
                response.sendRedirect(request.getContextPath() + "/complete-registration");
            } else {
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/home");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
