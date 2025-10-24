package controller.admin;

import dao.implement.UserDAO;
import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.UserProfile;
import model.User;

@WebServlet("/admin/manageUser")
public class AdminManageUserServlet extends HttpServlet {


    private transient final UserProfileDAO profileDAO = new UserProfileDAO();
    private transient final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idRaw = request.getParameter("userId");

        if (action == null || idRaw == null) {
            response.sendRedirect(request.getContextPath() + "/accountDB");
            return;
        }

        int userId = Integer.parseInt(idRaw);

        if (action.equals("view")) {  // <-- thêm view
            UserProfile profile = profileDAO.findByUserId(userId);

            String email = userDAO.findEmailByUserId(userId);

            request.setAttribute("profile", profile);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/admin/viewUserProfile.jsp").forward(request, response);

        } else if (action.equals("remove")) {
            profileDAO.deleteByUserId(userId);
            response.sendRedirect(request.getContextPath() + "/accountDB");
        }
    }
}
