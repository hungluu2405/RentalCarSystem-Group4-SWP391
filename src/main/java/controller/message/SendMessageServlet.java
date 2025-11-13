package controller.message;

import dao.implement.MessageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Message;
import model.User;

import java.io.IOException;

@WebServlet(name = "SendMessageServlet", urlPatterns = {"/send-message"})
public class SendMessageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        User currentUser = (User) req.getSession().getAttribute("user");
        if (currentUser == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Not logged in\"}");
            return;
        }

        String receiverIdStr = req.getParameter("receiverId");
        String content = req.getParameter("content");

        if (receiverIdStr == null || receiverIdStr.isEmpty() || content == null || content.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Missing receiverId or content\"}");
            return;
        }

        try {
            int senderId = currentUser.getUserId();
            int receiverId = Integer.parseInt(receiverIdStr);

            Message msg = new Message(senderId, receiverId, content.trim());
            MessageDAO dao = new MessageDAO();
            dao.insertMessage(msg);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"status\":\"ok\"}");

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid receiverId\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Server error while sending message\"}");
        }
    }
}
