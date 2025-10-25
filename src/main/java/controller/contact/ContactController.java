package controller.contact;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.Contact;
import service.ContactService;

@WebServlet("/contact")
public class ContactController extends HttpServlet {
    private ContactService contactService = new ContactService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Giả sử bạn đã lưu userId trong session
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 0; // fallback nếu user chưa đăng nhập

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String message = request.getParameter("message");

        Contact contact = new Contact(userId, name, email, phone, message);
        boolean success = contactService.addContact(contact);

        if (success) {
            request.setAttribute("msg", "Gửi liên hệ thành công!");
        } else {
            request.setAttribute("msg", "Gửi liên hệ thất bại. Vui lòng thử lại.");
        }

        request.getRequestDispatcher("/view/contact/contact.jsp").forward(request, response);
    }
}
