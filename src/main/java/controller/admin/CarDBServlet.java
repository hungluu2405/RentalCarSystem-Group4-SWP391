package controller.admin;

import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import model.Car;

@WebServlet( urlPatterns = {"/carDB"})
public class CarDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");
        String priceRange = request.getParameter("price");

        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ContactDAO contactDAO = new ContactDAO();
//        List<Car> listC = carDAO.getAllCarsForAdmin();
        List<Car> listC = carDAO.getCarsFiltered(type,priceRange);

        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();
        int totalReports = paymentDAO.countAllReport();
        int totalContacts = contactDAO.countUnresolvedContacts();
        // 2️⃣ Gửi danh sách sang JSP
        request.setAttribute("listC", listC);
        request.setAttribute("selectedType", type);
        request.setAttribute("selectedPrice", priceRange);

        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);
        request.setAttribute("totalReports",totalReports);
        request.setAttribute("totalContacts", totalContacts);
        // 3️⃣ Forward sang JSP hiển thị
        request.setAttribute("activePage", "car");
        request.getRequestDispatcher("/view/admin/Cardashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
