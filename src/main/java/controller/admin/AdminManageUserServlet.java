package controller.admin;

import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.UserProfile;

@WebServlet("/admin/manageUser")
public class AdminManageUserServlet extends HttpServlet {


    private transient final UserProfileDAO profileDAO = new UserProfileDAO();

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

        if (action.equals("edit")) {

            UserProfile profile = profileDAO.findByUserId(userId);


            request.setAttribute("profile", profile);
            request.getRequestDispatcher("/view/customer/myProfile.jsp").forward(request, response);

        } else if (action.equals("remove")) {
            profileDAO.deleteByUserId(userId);

            response.sendRedirect(request.getContextPath() + "/accountDB");
        }
    }
}
