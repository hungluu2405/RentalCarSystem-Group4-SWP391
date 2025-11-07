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

    private static final int PAGE_SIZE = 5; // 5 records per page

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

        // Get tab parameter (current or history)
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "current"; // default to current trips
        }

        // Get page parameter
        String pageParam = request.getParameter("page");
        int currentPage = 1;
        try {
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
        }

        // Calculate offset
        int offset = (currentPage - 1) * PAGE_SIZE;

        // Statistics
        int upcoming = bookingDAO.countByStatus(userId, "Pending");
        int total = bookingDAO.countByUser(userId);
        int cancelled = bookingDAO.countByStatus(userId, "Cancelled");

        // Pagination data based on tab
        List<BookingDetail> bookings;
        int totalRecords;
        int totalPages;

        if ("history".equals(tab)) {
            totalRecords = bookingDAO.countHistoryTripsByUserId(userId);
            totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
            bookings = bookingDAO.getHistoryTripsByUserId(userId, offset, PAGE_SIZE);
        } else {
            totalRecords = bookingDAO.countCurrentTripsByUserId(userId);
            totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
            bookings = bookingDAO.getCurrentTripsByUserId(userId, offset, PAGE_SIZE);
        }

        // Set attributes
        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("bookings", bookings);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("tab", tab);

        request.getRequestDispatcher("/view/customer/customerOrder.jsp").forward(request, response);
    }
}