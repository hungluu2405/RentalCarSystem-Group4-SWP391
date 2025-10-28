package controller.admin;

import dao.implement.*;
import model.Payment;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reportDB")
public class ReportDBServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PaymentDAO paymentDAO = new PaymentDAO();
        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();

        // Giả sử DAO của bạn có hàm getAll()
//        List<Payment> listRP = paymentDAO.getAll();

        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();
        int totalReports = paymentDAO.countAllReport();

        String type = request.getParameter("type");

        List<Payment> listRP;
        double totalRevenue = 0;

        if ("week".equals(type) || "month".equals(type)) {
            // Nếu có chọn lọc hợp lệ → lọc theo tuần/tháng
            listRP = paymentDAO.getPaymentsByWeekOrMonth(type);
            totalRevenue = paymentDAO.getTotalRevenueByWeekOrMonth(type);
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("type", type);
        } else {
            // Nếu không có chọn gì → hiển thị tất cả
            listRP = paymentDAO.getAll();
        }



        request.setAttribute("listRP", listRP);
        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);
        request.setAttribute("totalReports",totalReports);
        request.setAttribute("activePage", "report");
        request.getRequestDispatcher("/view/admin/Report.jsp").forward(request, response);
    }
}
