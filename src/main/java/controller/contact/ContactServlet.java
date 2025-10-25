package controller.contact;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

import model.Contact;
import service.ContactService;

/**
 * Servlet xử lý form Contact Us
 */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ContactService contactService;

    @Override
    public void init() throws ServletException {
        contactService = new ContactService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy thông tin từ form contact.jsp
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String message = request.getParameter("message");

        // Lấy userId từ session (nếu user đã đăng nhập)
        HttpSession session = request.getSession(false);
        int userId = 0;
        if (session != null && session.getAttribute("userId") != null) {
            userId = (int) session.getAttribute("userId");
        }

        // Tạo đối tượng Contact
        Contact contact = new Contact();
        contact.setUserId(userId);
        contact.setName(name);
        contact.setEmail(email);
        contact.setPhoneNumber(phone);
        contact.setMessage(message);
        contact.setCreatedAt(LocalDateTime.now());

        // Gọi service để lưu vào DB
        boolean isSaved = contactService.addContact(contact);

        if (isSaved) {
            // Gửi phản hồi cho người dùng (có thể chuyển hướng)
            request.setAttribute("successMessage", "Your message has been sent successfully!");
            request.getRequestDispatcher("/view/contact/contact.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to send your message. Please try again!");
            request.getRequestDispatcher("/view/contact/contact.jsp").forward(request, response);
        }
    }
}
