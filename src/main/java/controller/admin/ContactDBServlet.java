package controller.admin;


import dao.implement.ContactDAO;
import model.Contact;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ContactDBServlet", urlPatterns = {"/contactDB"})
public class ContactDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contactList = contactDAO.getAllContacts();


        request.setAttribute("contactList", contactList);
//        request.setAttribute("activePage", "support");
        request.getRequestDispatcher("/view/admin/supportDashboard.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ticketIdParam = request.getParameter("ticketId");
        String statusParam = request.getParameter("status");

        if (ticketIdParam != null && statusParam != null) {
            int ticketId = Integer.parseInt(ticketIdParam);
            boolean status = Boolean.parseBoolean(statusParam);

            ContactDAO dao = new ContactDAO();
            dao.updateStatus(ticketId, status);
        }

        // Cập nhật xong load lại trang
        response.sendRedirect("contactDB");
    }
}

