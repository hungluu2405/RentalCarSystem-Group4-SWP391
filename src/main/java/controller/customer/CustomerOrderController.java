package controller.customer;

import dao.implement.BookingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import model.BookingDetail;
import model.User;

@WebServlet("/customer/customerOrder")
public class CustomerOrderController extends HttpServlet {

    private static final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        BookingDAO bookingDAO = new BookingDAO();
        int userId = user.getUserId();

        // Get tab parameter
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "current";
        }

        // Get filter parameters
        String filterStatus = request.getParameter("filterStatus");
        String startDateFrom = request.getParameter("startDateFrom");
        String startDateTo = request.getParameter("startDateTo");
        String endDateFrom = request.getParameter("endDateFrom");
        String endDateTo = request.getParameter("endDateTo");
        String carName = request.getParameter("carName");
        String priceRange = request.getParameter("priceRange");

        // Get page parameter
        int currentPage = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        int offset = (currentPage - 1) * PAGE_SIZE;

        // Statistics
        int upcoming = bookingDAO.countByStatus(userId, "Pending");
        int total = bookingDAO.countByUser(userId);
        int cancelled = bookingDAO.countByStatus(userId, "Cancelled");

        // Get bookings with filters
        List<BookingDetail> bookings;
        int totalRecords;
        int totalPages;

        if ("history".equals(tab)) {
            // Check if any filter is applied
            boolean hasFilters = hasFilters(filterStatus, startDateFrom, startDateTo,
                    endDateFrom, endDateTo, carName, priceRange);

            if (hasFilters) {
                bookings = bookingDAO.getHistoryTripsWithFilters(userId, filterStatus,
                        startDateFrom, startDateTo, endDateFrom, endDateTo, carName, priceRange,
                        offset, PAGE_SIZE);
                totalRecords = bookingDAO.countHistoryTripsWithFilters(userId, filterStatus,
                        startDateFrom, startDateTo, endDateFrom, endDateTo, carName, priceRange);
            } else {
                bookings = bookingDAO.getHistoryTripsByUserId(userId, offset, PAGE_SIZE);
                totalRecords = bookingDAO.countHistoryTripsByUserId(userId);
            }
        } else {
            boolean hasFilters = hasFilters(filterStatus, startDateFrom, startDateTo,
                    endDateFrom, endDateTo, carName, priceRange);

            if (hasFilters) {
                bookings = bookingDAO.getCurrentTripsWithFilters(userId, filterStatus,
                        startDateFrom, startDateTo, endDateFrom, endDateTo, carName, priceRange,
                        offset, PAGE_SIZE);
                totalRecords = bookingDAO.countCurrentTripsWithFilters(userId, filterStatus,
                        startDateFrom, startDateTo, endDateFrom, endDateTo, carName, priceRange);
            } else {
                bookings = bookingDAO.getCurrentTripsByUserId(userId, offset, PAGE_SIZE);
                totalRecords = bookingDAO.countCurrentTripsByUserId(userId);
            }
        }

        totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        // Set attributes
        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("bookings", bookings);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("tab", tab);

        // Set filter parameters for JSP
        request.setAttribute("filterStatus", filterStatus);
        request.setAttribute("startDateFrom", startDateFrom);
        request.setAttribute("startDateTo", startDateTo);
        request.setAttribute("endDateFrom", endDateFrom);
        request.setAttribute("endDateTo", endDateTo);
        request.setAttribute("carName", carName);
        request.setAttribute("priceRange", priceRange);

        request.getRequestDispatcher("/view/customer/customerOrder.jsp").forward(request, response);
    }

    private boolean hasFilters(String status, String startFrom, String startTo,
                               String endFrom, String endTo, String carName, String price) {
        return (status != null && !status.isEmpty() && !status.equals("All")) ||
                (startFrom != null && !startFrom.isEmpty()) ||
                (startTo != null && !startTo.isEmpty()) ||
                (endFrom != null && !endFrom.isEmpty()) ||
                (endTo != null && !endTo.isEmpty()) ||
                (carName != null && !carName.isEmpty()) ||
                (price != null && !price.isEmpty());
    }
}