package controller.message;

import com.google.gson.Gson;
import dao.implement.MessageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Message;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PollMessageServlet", urlPatterns = {"/poll-messages"})
public class PollMessageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        String partnerIdStr = req.getParameter("partnerId");
        String lastMsgIdStr = req.getParameter("lastMsgId");

        if (partnerIdStr == null || lastMsgIdStr == null ||
                partnerIdStr.isEmpty() || lastMsgIdStr.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Missing partnerId or lastMsgId\"}");
            return;
        }

        try {
            int currentUserId = currentUser.getUserId();
            int partnerId = Integer.parseInt(partnerIdStr);
            int lastMsgId = Integer.parseInt(lastMsgIdStr);

            MessageDAO dao = new MessageDAO();

            // Lấy tin nhắn mới trong cuộc hội thoại giữa 2 người
            List<Message> messages = dao.getNewMessagesBetween(currentUserId, partnerId, lastMsgId);

            // Đánh dấu tin nhắn đối phương gửi cho mình là đã đọc
            if (!messages.isEmpty()) {
                dao.markAsRead(partnerId, currentUserId);
            }

            String json = new Gson().toJson(messages);
            resp.getWriter().write(json);

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid partnerId or lastMsgId\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error while polling messages\"}");
        }
    }
}
