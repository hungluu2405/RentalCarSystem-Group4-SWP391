package controller.userchat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.implement.UserChatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import model.UserConversation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "InitChatServlet", urlPatterns = {"/api/init-chat"})
public class InitChatServlet extends HttpServlet {

    private final UserChatDAO chatDAO = new UserChatDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            System.out.println("[InitChatServlet] ❌ Unauthorized: User not logged in");
            sendErrorResponse(response, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String bookingIdStr = request.getParameter("bookingId");
            System.out.println("[InitChatServlet] Request from userId=" + currentUser.getUserId() + ", bookingId=" + bookingIdStr);

            if (bookingIdStr == null) {
                System.out.println("[InitChatServlet] ❌ Missing bookingId parameter");
                sendErrorResponse(response, "Missing bookingId", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int bookingId = Integer.parseInt(bookingIdStr);

            // Verify booking exists and get customer_id and owner_id
            int customerId = 0;
            int ownerId = 0;

            String sql = "SELECT b.CUSTOMER_ID, c.OWNER_ID " +
                        "FROM BOOKING b " +
                        "JOIN CAR c ON b.CAR_ID = c.CAR_ID " +
                        "WHERE b.BOOKING_ID = ?";

            try (Connection conn = chatDAO.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    customerId = rs.getInt("CUSTOMER_ID");
                    ownerId = rs.getInt("OWNER_ID");
                    System.out.println("[InitChatServlet] Found booking: customerId=" + customerId + ", ownerId=" + ownerId);
                } else {
                    System.out.println("[InitChatServlet] ❌ Booking not found: " + bookingId);
                    sendErrorResponse(response, "Booking not found", HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
            }

            // Verify current user is either customer or owner
            if (currentUser.getUserId() != customerId && currentUser.getUserId() != ownerId) {
                System.out.println("[InitChatServlet] ❌ Access denied: userId=" + currentUser.getUserId() + " not in customerId=" + customerId + " or ownerId=" + ownerId);
                sendErrorResponse(response, "Access denied. You are not part of this booking.", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            System.out.println("[InitChatServlet] ✅ Access granted for userId=" + currentUser.getUserId());

            // Get or create conversation
            UserConversation conversation = chatDAO.getOrCreateConversation(bookingId, customerId, ownerId);

            if (conversation == null) {
                System.out.println("[InitChatServlet] ❌ Failed to create conversation");
                sendErrorResponse(response, "Failed to create conversation", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            System.out.println("[InitChatServlet] ✅ Conversation created/found: conversationId=" + conversation.getConversationId());

            // Get the other user's info
            int otherUserId = (currentUser.getUserId() == customerId) ? ownerId : customerId;
            String otherUserName = getUserName(otherUserId);
            String carName = getCarName(bookingId);

            // Send response
            JsonObject responseData = new JsonObject();
            responseData.addProperty("success", true);
            responseData.addProperty("conversationId", conversation.getConversationId());
            responseData.addProperty("bookingId", bookingId);
            responseData.addProperty("otherUserId", otherUserId);
            responseData.addProperty("otherUserName", otherUserName);
            responseData.addProperty("carName", carName);

            System.out.println("[InitChatServlet] ✅ Success response sent");
            sendJsonResponse(response, responseData);

        } catch (Exception e) {
            System.err.println("Error in InitChatServlet: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "Server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String getUserName(int userId) {
        String sql = "SELECT FULL_NAME FROM [USER] WHERE user_id = ?";
        try (Connection conn = chatDAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("FULL_NAME");
            }
        } catch (Exception e) {
            System.err.println("Error getting user name: " + e.getMessage());
        }
        return "Unknown";
    }

    private String getCarName(int bookingId) {
        String sql = "SELECT c.TITLE FROM CAR c " +
                    "JOIN BOOKING b ON c.CAR_ID = b.CAR_ID " +
                    "WHERE b.BOOKING_ID = ?";
        try (Connection conn = chatDAO.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TITLE");
            }
        } catch (Exception e) {
            System.err.println("Error getting car name: " + e.getMessage());
        }
        return "Unknown Car";
    }

    private void sendJsonResponse(HttpServletResponse response, JsonObject data) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(data));
        out.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage, int statusCode) throws IOException {
        response.setStatus(statusCode);
        JsonObject errorData = new JsonObject();
        errorData.addProperty("success", false);
        errorData.addProperty("error", errorMessage);
        sendJsonResponse(response, errorData);
    }
}
