package controller.carOwner;

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

@WebServlet("/owner/myBooking")
public class CarOwnerOrderController extends HttpServlet {

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

        // Lấy tham số tab
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "current";
        }

        // Lấy tham số page
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

        // Thống kê
        int upcoming = bookingDAO.countByStatus(userId, "Pending");
        int total = bookingDAO.countByUser(userId);
        int cancelled = bookingDAO.countByStatus(userId, "Cancelled");

        // Lấy bookings theo tab
        List<BookingDetail> bookings;
        int totalRecords;

        if ("history".equals(tab)) {
            bookings = bookingDAO.getHistoryTripsByUserId(userId, offset, PAGE_SIZE);
            totalRecords = bookingDAO.countHistoryTripsByUserId(userId);
        } else {
            bookings = bookingDAO.getCurrentTripsByUserId(userId, offset, PAGE_SIZE);
            totalRecords = bookingDAO.countCurrentTripsByUserId(userId);
        }

        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

        request.setAttribute("bookings", bookings);
        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("tab", tab);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/view/carOwner/carOwnerOrder.jsp").forward(request, response);
    }
}