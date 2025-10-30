package controller.admin;


//import dao.ContactDAO;
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
        // Gọi DAO để lấy danh sách contact
//        ContactDAO contactDAO = new ContactDAO();
//        List<Contact> contactList = contactDAO.getAllContacts();

        // Gửi dữ liệu qua JSP
//        request.setAttribute("contactList", contactList);
        request.getRequestDispatcher("/view/admin/supportDashboard.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
