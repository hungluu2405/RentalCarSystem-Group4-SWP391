package controller.userchat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@WebServlet(name = "ChatFileUploadServlet", urlPatterns = {"/api/chat-upload"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 15     // 15MB
)
public class ChatFileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads/chat";
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"};
    private static final String[] ALLOWED_FILE_TYPES = {"application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/plain"};

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Check authentication
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            sendErrorResponse(response, "Unauthorized. Please login.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            // Get the uploaded file
            Part filePart = request.getPart("file");

            if (filePart == null) {
                sendErrorResponse(response, "No file uploaded", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String fileName = getFileName(filePart);
            String contentType = filePart.getContentType();
            long fileSize = filePart.getSize();

            System.out.println("Uploading file: " + fileName + ", Type: " + contentType + ", Size: " + fileSize);

            // Validate file size
            if (fileSize > MAX_FILE_SIZE) {
                sendErrorResponse(response, "File too large. Maximum size is 10MB.", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Validate file type
            String fileType = validateFileType(contentType);
            if (fileType == null) {
                sendErrorResponse(response, "Invalid file type. Allowed: images (jpg, png, gif, webp) and documents (pdf, doc, xls, txt)", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Create upload directory if it doesn't exist
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generate unique filename
            String fileExtension = getFileExtension(fileName);
            String uniqueFileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + fileExtension;
            String filePath = uploadPath + File.separator + uniqueFileName;

            // Save file
            try {
                Path targetPath = Paths.get(filePath);
                Files.copy(filePart.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Generate URL
                String fileUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + uniqueFileName;

                // Return success response
                JsonObject responseData = new JsonObject();
                responseData.addProperty("success", true);
                responseData.addProperty("fileUrl", fileUrl);
                responseData.addProperty("fileName", fileName);
                responseData.addProperty("fileType", fileType);
                responseData.addProperty("fileSize", fileSize);

                sendJsonResponse(response, responseData);

                System.out.println("File uploaded successfully: " + fileUrl);

            } catch (IOException e) {
                System.err.println("Error saving file: " + e.getMessage());
                e.printStackTrace();
                sendErrorResponse(response, "Failed to save file", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            System.err.println("Error in ChatFileUploadServlet: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(response, "Server error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String validateFileType(String contentType) {
        if (contentType == null) {
            return null;
        }

        // Check if it's an image
        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (contentType.equalsIgnoreCase(allowedType)) {
                return "image";
            }
        }

        // Check if it's a document
        for (String allowedType : ALLOWED_FILE_TYPES) {
            if (contentType.equalsIgnoreCase(allowedType)) {
                return "file";
            }
        }

        return null;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown";
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot);
        }
        return "";
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
