package controller.admin;


import dao.implement.*;
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
        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ContactDAO contactDAO = new ContactDAO();
        List<Contact> contactList = contactDAO.getAllContacts();

        int totalContacts = contactDAO.countUnresolvedContacts();
        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();
        int totalReports = paymentDAO.countAllReport();


        request.setAttribute("contactList", contactList);
        request.setAttribute("totalContacts", totalContacts);
        request.setAttribute("activePage", "support");
        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);
        request.setAttribute("totalReports",totalReports);
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

