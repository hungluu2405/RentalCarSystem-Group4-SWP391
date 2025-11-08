package controller.admin;

import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import model.User;

@WebServlet(name = "AccountDashBoard", urlPatterns = {"/accountDB"})
public class AccountDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ContactDAO contactDAO = new ContactDAO();
        Driver_LicenseDAO licenseDAO = new Driver_LicenseDAO();
        String role = request.getParameter("role");
        List<User> listU;

        if (role != null && !role.isEmpty()) {
            listU = userDAO.getUsers(role); // dùng hàm mới getUsers(role)
        } else {
            listU = userDAO.getAllUsersForAdmin();
        }


        for (User u : listU) {
            var license = licenseDAO.getLicenseByUserId(u.getUserId());
            if (license != null && u.getUserProfile() != null) {
                u.getUserProfile().setDriverLicenseNumber(license.getLicense_number());
            }
        }

        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();
        int totalReports = paymentDAO.countAllReport();
        int totalContacts = contactDAO.countUnresolvedContacts();

        request.setAttribute("listU", listU);
        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);
        request.setAttribute("totalReports",totalReports);
        request.setAttribute("totalContacts", totalContacts);
        request.setAttribute("activePage", "account");
        request.getRequestDispatcher("/view/admin/accountdashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
