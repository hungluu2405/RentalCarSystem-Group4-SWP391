package controller.chatbot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.implement.ChatbotDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ChatConversation;
import model.ChatMessage;
import model.User;
import service.chatbot.GeminiService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "ChatbotServlet", urlPatterns = {"/api/chatbot"})
public class ChatbotServlet extends HttpServlet {

    private final ChatbotDAO chatbotDAO = new ChatbotDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Read request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JsonObject requestData = gson.fromJson(sb.toString(), JsonObject.class);
            String userMessage = requestData.get("message").getAsString();

            if (userMessage == null || userMessage.trim().isEmpty()) {
                sendErrorResponse(response, "Message cannot be empty");
                return;
            }

            // Get or create conversation
            HttpSession session = request.getSession();
            String sessionId = session.getId();
            User user = (User) session.getAttribute("user");
            Integer userId = user != null ? user.getUserId() : null;

            ChatConversation conversation = chatbotDAO.getActiveConversationBySessionId(sessionId);
            if (conversation == null) {
                conversation = chatbotDAO.createConversation(userId, sessionId);
                if (conversation == null) {
                    sendErrorResponse(response, "Failed to create conversation");
                    return;
                }
            }

            // Save user message
            long startTime = System.currentTimeMillis();
            ChatMessage userMsg = chatbotDAO.createMessage(
                conversation.getConversationId(),
                "user",
                userMessage,
                null,
                null
            );

            if (userMsg == null) {
                sendErrorResponse(response, "Failed to save user message");
                return;
            }

            // Get Gemini API key from config
            String geminiApiKey = chatbotDAO.getConfigValue("GEMINI_API_KEY");
            if (geminiApiKey == null || geminiApiKey.isEmpty()) {
                sendErrorResponse(response, "Gemini API key not configured. Please contact administrator.");
                return;
            }

            // Generate response using Gemini
            GeminiService geminiService = new GeminiService(geminiApiKey);
            String botResponse = geminiService.generateResponse(userMessage);
            long responseTime = System.currentTimeMillis() - startTime;

            // Save bot response
            ChatMessage botMsg = chatbotDAO.createMessage(
                conversation.getConversationId(),
                "model",
                botResponse,
                null,
                (int) responseTime
            );

            if (botMsg == null) {
                sendErrorResponse(response, "Failed to save bot response");
                return;
            }

            // Send success response
            JsonObject responseData = new JsonObject();
            responseData.addProperty("success", true);
            responseData.addProperty("message", botResponse);
            responseData.addProperty("conversationId", conversation.getConversationId());
            responseData.addProperty("messageId", botMsg.getMessageId());

            sendJsonResponse(response, responseData);

        } catch (Exception e) {
            System.err.println("Error in chatbot servlet: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "An error occurred while processing your request");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String action = request.getParameter("action");

            if ("history".equals(action)) {
                // Get conversation history
                HttpSession session = request.getSession();
                String sessionId = session.getId();

                ChatConversation conversation = chatbotDAO.getActiveConversationBySessionId(sessionId);
                if (conversation != null) {
                    List<ChatMessage> messages = chatbotDAO.getMessagesByConversationId(
                        conversation.getConversationId()
                    );

                    JsonObject responseData = new JsonObject();
                    responseData.addProperty("success", true);
                    responseData.add("messages", gson.toJsonTree(messages));
                    responseData.addProperty("conversationId", conversation.getConversationId());

                    sendJsonResponse(response, responseData);
                } else {
                    JsonObject responseData = new JsonObject();
                    responseData.addProperty("success", true);
                    responseData.add("messages", gson.toJsonTree(new ChatMessage[0]));
                    sendJsonResponse(response, responseData);
                }
            } else {
                sendErrorResponse(response, "Invalid action");
            }

        } catch (Exception e) {
            System.err.println("Error getting chat history: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "An error occurred while retrieving chat history");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, JsonObject data) throws IOException {
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(data));
        out.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JsonObject errorData = new JsonObject();
        errorData.addProperty("success", false);
        errorData.addProperty("error", errorMessage);
        sendJsonResponse(response, errorData);
    }
}
