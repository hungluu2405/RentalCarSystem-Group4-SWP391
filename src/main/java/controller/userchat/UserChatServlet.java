package controller.userchat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.implement.UserChatDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.UserConversation;
import model.UserMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "UserChatServlet", urlPatterns = {"/api/user-chat"})
public class UserChatServlet extends HttpServlet {

    private final UserChatDAO chatDAO = new UserChatDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            sendErrorResponse(response, "Unauthorized. Please login.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String action = request.getParameter("action");

            if (action == null) {
                sendErrorResponse(response, "Missing action parameter", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            switch (action) {
                case "send":
                    handleSendMessage(request, response, currentUser);
                    break;
                case "typing":
                    handleTypingStatus(request, response, currentUser);
                    break;
                case "markRead":
                    handleMarkAsRead(request, response, currentUser);
                    break;
                default:
                    sendErrorResponse(response, "Invalid action", HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (Exception e) {
            System.err.println("Error in UserChatServlet POST: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            sendErrorResponse(response, "Server error: " + errorMsg, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            sendErrorResponse(response, "Unauthorized. Please login.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String action = request.getParameter("action");

            if (action == null) {
                sendErrorResponse(response, "Missing action parameter", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            switch (action) {
                case "conversations":
                    handleGetConversations(request, response, currentUser);
                    break;
                case "messages":
                    handleGetMessages(request, response, currentUser);
                    break;
                case "poll":
                    handlePollNewMessages(request, response, currentUser);
                    break;
                case "checkTyping":
                    handleCheckTyping(request, response, currentUser);
                    break;
                default:
                    sendErrorResponse(response, "Invalid action", HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (Exception e) {
            System.err.println("Error in UserChatServlet GET: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "Server error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // ==================== SEND MESSAGE ====================
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String jsonString = sb.toString();
        System.out.println("[UserChatServlet] Received JSON for send message: " + jsonString);

        JsonObject requestData = gson.fromJson(jsonString, JsonObject.class);

        if (!requestData.has("conversationId")) {
            sendErrorResponse(response, "Missing conversationId in request", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int conversationId = requestData.get("conversationId").getAsInt();
        String content = requestData.has("content") && !requestData.get("content").isJsonNull()
                ? requestData.get("content").getAsString() : null;

        // Handle attachmentUrl - it might be a string, object, or null
        String attachmentUrl = null;
        if (requestData.has("attachmentUrl") && !requestData.get("attachmentUrl").isJsonNull()) {
            if (requestData.get("attachmentUrl").isJsonPrimitive()) {
                attachmentUrl = requestData.get("attachmentUrl").getAsString();
            }
            // If it's a JsonObject (like {"isTrusted":true}), ignore it
        }

        String attachmentType = requestData.has("attachmentType") && !requestData.get("attachmentType").isJsonNull()
                ? requestData.get("attachmentType").getAsString() : null;

        System.out.println("[UserChatServlet] Parsed - conversationId: " + conversationId + ", content: " + content + ", attachmentUrl: " + attachmentUrl);

        // Verify user has access to this conversation
        if (!chatDAO.canUserAccessConversation(conversationId, currentUser.getUserId())) {
            sendErrorResponse(response, "Access denied to this conversation", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Send message
        UserMessage message = chatDAO.sendMessage(conversationId, currentUser.getUserId(), content, attachmentUrl, attachmentType);

        if (message == null) {
            sendErrorResponse(response, "Failed to send message", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        // Clear typing status
        chatDAO.updateTypingStatus(conversationId, currentUser.getUserId(), false);

        // Send success response
        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);
        responseData.add("message", gson.toJsonTree(message));

        sendJsonResponse(response, responseData);
    }

    // ==================== GET CONVERSATIONS ====================
    private void handleGetConversations(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        List<UserConversation> conversations = chatDAO.getConversationsForUser(currentUser.getUserId());

        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);
        responseData.add("conversations", gson.toJsonTree(conversations));

        sendJsonResponse(response, responseData);
    }

    // ==================== GET MESSAGES ====================
    private void handleGetMessages(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        String conversationIdStr = request.getParameter("conversationId");
        String limitStr = request.getParameter("limit");
        String offsetStr = request.getParameter("offset");

        if (conversationIdStr == null) {
            sendErrorResponse(response, "Missing conversationId parameter", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int conversationId = Integer.parseInt(conversationIdStr);
        int limit = limitStr != null ? Integer.parseInt(limitStr) : 50;
        int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;

        // Verify access
        if (!chatDAO.canUserAccessConversation(conversationId, currentUser.getUserId())) {
            sendErrorResponse(response, "Access denied", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<UserMessage> messages = chatDAO.getMessages(conversationId, limit, offset);

        // Mark messages as read
        chatDAO.markMessagesAsRead(conversationId, currentUser.getUserId());

        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);
        responseData.add("messages", gson.toJsonTree(messages));

        sendJsonResponse(response, responseData);
    }

    // ==================== POLL NEW MESSAGES ====================
    private void handlePollNewMessages(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        String conversationIdStr = request.getParameter("conversationId");
        String lastMessageIdStr = request.getParameter("lastMessageId");

        if (conversationIdStr == null || lastMessageIdStr == null) {
            sendErrorResponse(response, "Missing parameters", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int conversationId = Integer.parseInt(conversationIdStr);
        int lastMessageId = Integer.parseInt(lastMessageIdStr);

        // Verify access
        if (!chatDAO.canUserAccessConversation(conversationId, currentUser.getUserId())) {
            sendErrorResponse(response, "Access denied", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<UserMessage> newMessages = chatDAO.getNewMessages(conversationId, lastMessageId);

        // Mark new messages as read
        if (!newMessages.isEmpty()) {
            chatDAO.markMessagesAsRead(conversationId, currentUser.getUserId());
        }

        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);
        responseData.add("messages", gson.toJsonTree(newMessages));
        responseData.addProperty("count", newMessages.size());

        sendJsonResponse(response, responseData);
    }

    // ==================== TYPING STATUS ====================
    private void handleTypingStatus(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JsonObject requestData = gson.fromJson(sb.toString(), JsonObject.class);
        int conversationId = requestData.get("conversationId").getAsInt();
        boolean isTyping = requestData.get("isTyping").getAsBoolean();

        // Verify access
        if (!chatDAO.canUserAccessConversation(conversationId, currentUser.getUserId())) {
            sendErrorResponse(response, "Access denied", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chatDAO.updateTypingStatus(conversationId, currentUser.getUserId(), isTyping);

        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);

        sendJsonResponse(response, responseData);
    }

    private void handleCheckTyping(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        String conversationIdStr = request.getParameter("conversationId");

        if (conversationIdStr == null) {
            sendErrorResponse(response, "Missing conversationId", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int conversationId = Integer.parseInt(conversationIdStr);

        // Verify access
        if (!chatDAO.canUserAccessConversation(conversationId, currentUser.getUserId())) {
            sendErrorResponse(response, "Access denied", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Get conversation to find the other user
        UserConversation conv = chatDAO.getConversationById(conversationId);
        int otherUserId = (conv.getCustomerId() == currentUser.getUserId())
                ? conv.getOwnerId() : conv.getCustomerId();

        boolean isTyping = chatDAO.isUserTyping(conversationId, otherUserId);

        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);
        responseData.addProperty("isTyping", isTyping);

        sendJsonResponse(response, responseData);
    }

    // ==================== MARK AS READ ====================
    private void handleMarkAsRead(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        JsonObject requestData = gson.fromJson(sb.toString(), JsonObject.class);
        int conversationId = requestData.get("conversationId").getAsInt();

        // Verify access
        if (!chatDAO.canUserAccessConversation(conversationId, currentUser.getUserId())) {
            sendErrorResponse(response, "Access denied", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chatDAO.markMessagesAsRead(conversationId, currentUser.getUserId());

        JsonObject responseData = new JsonObject();
        responseData.addProperty("success", true);

        sendJsonResponse(response, responseData);
    }

    // ==================== HELPER METHODS ====================
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
