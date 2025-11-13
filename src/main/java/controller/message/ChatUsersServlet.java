package controller.message;

import com.google.gson.Gson;
import dao.implement.MessageDAO;
import dao.implement.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.UserProfile;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ChatUsersServlet", urlPatterns = {"/chat-users"})
public class ChatUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Lấy user hiện tại từ session
        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        int currentUserId = currentUser.getUserId();

        try {
            MessageDAO messageDAO = new MessageDAO();
            UserDAO userDAO = new UserDAO();

            // Lấy danh sách partnerId đã từng chat
            List<Integer> partnerIds = messageDAO.getRecentChatPartners(currentUserId);

            if (partnerIds.isEmpty()) {
                resp.getWriter().write("[]");
                return;
            }

            // Lấy profile (userId, fullName, profileImage)
            List<UserProfile> partners = userDAO.getProfilesByIds(partnerIds);

            String json = new Gson().toJson(partners);
            resp.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error while loading chat users\"}");
        }
    }
}
