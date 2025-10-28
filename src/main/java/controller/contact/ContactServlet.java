package controller.contact;

import model.Contact;
import dao.implement.ContactDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ContactServlet", urlPatterns = {"/contact"})
public class ContactServlet extends HttpServlet {

    private final ContactDAO contactDAO = new ContactDAO();

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/plain;charset=UTF-8");

    HttpSession session = request.getSession();
       Integer userId = (Integer) session.getAttribute("USER_ID");


       String name = request.getParameter("name");
    String email = request.getParameter("email");
    String phone = request.getParameter("phone");
    String message = request.getParameter("message");

    boolean success = false;

    if (name != null && !name.trim().isEmpty()
            && email != null && email.contains("@")
            && message != null && !message.trim().isEmpty()) {

        Contact contact = new Contact(userId, name, email, phone, message);
        success = contactDAO.insertContact(contact);
    }

    if (success) {
    response.getWriter().write("sent");
    } else {
        response.getWriter().write("error");
    }
}


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/contact/contact.jsp").forward(request, response);
    }
}
