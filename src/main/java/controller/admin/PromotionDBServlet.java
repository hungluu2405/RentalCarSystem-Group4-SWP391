package controller.admin;

import dao.implement.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Promotion;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "PromotionDashBoard", urlPatterns = {"/promotionDB"})
public class PromotionDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDAO userDAO = new UserDAO();
        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        ContactDAO contactDAO = new ContactDAO();
        Driver_LicenseDAO licenseDAO = new Driver_LicenseDAO();
        PromotionDAO promoDAO = new PromotionDAO();

        String filterActive = request.getParameter("active");
        String filterRate = request.getParameter("rate");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        // Chuyển dữ liệu từ String sang dạng DAO cần:
        Boolean active = null;
        if ("true".equals(filterActive)) active = true;
        if ("false".equals(filterActive)) active = false;

        int totalUsers = userDAO.countAllUsers();
        int totalCars = carDAO.countAllCars();
        int totalBookings = bookingDAO.countAllBookings();
        int totalReports = paymentDAO.countAllReport();
        int totalContacts = contactDAO.countUnresolvedContacts();
        String action = request.getParameter("action");

        // ===========================
        // HANDLE TOGGLE ACTIVE
        // ===========================
        if ("toggle".equals(action)) {
            handleToggle(request, response, promoDAO);
            return;
        }

        // ===========================
        // LOAD ALL PROMOTIONS
        // ===========================
        Integer rateFilter = filterRate != null && !filterRate.isEmpty()
                ? Integer.parseInt(filterRate) : null;

        Date startFrom = (startDate != null && !startDate.isEmpty())
                ? Date.valueOf(startDate) : null;

        Date endTo = (endDate != null && !endDate.isEmpty())
                ? Date.valueOf(endDate) : null;

// gọi DAO filter
        List<Promotion> list = promoDAO.filterPromotions(active, rateFilter, startFrom, endTo);

        request.setAttribute("listP", list);
        request.setAttribute("totalUsers",totalUsers);
        request.setAttribute("totalCars",totalCars);
        request.setAttribute("totalBookings",totalBookings);
        request.setAttribute("totalReports",totalReports);
        request.setAttribute("totalContacts", totalContacts);
        request.setAttribute("activePage", "promotion");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            promoDAO.deletePromotion(id);
            response.sendRedirect("promotionDB");
            return;
        }


        request.getRequestDispatcher("/view/admin/promotionDashboard.jsp")
                .forward(request, response);
    }

    private void handleToggle(HttpServletRequest request, HttpServletResponse response, PromotionDAO dao)
            throws IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            dao.updateActiveStatus(id, status);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Quay lại dashboard sau khi toggle
        response.sendRedirect("promotionDB");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
