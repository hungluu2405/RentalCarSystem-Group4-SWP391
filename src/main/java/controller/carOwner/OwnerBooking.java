package controller.carOwner;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.BookingDetail;
import model.CarViewModel;
import model.User;
import service.booking.BookingService;

@WebServlet("/owner/ownerBooking")
public class OwnerBooking extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final BookingService bookingService = new BookingService();
    private static final int PAGE_SIZE = 5; // 5 records per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        User owner = (User) session.getAttribute("user");

        if (owner == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int ownerId = owner.getUserId();

        // --- LẤY TAB HIỆN TẠI ---
        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "pending";
        }

        // --- LẤY PAGE HIỆN TẠI ---
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

        int offset = (currentPage - 1) * PAGE_SIZE;

        System.out.println("🔍 DEBUG OwnerBooking Controller - OwnerID: " + ownerId + ", Tab: " + tab + ", Page: " + currentPage);

        // --- BIẾN DỮ LIỆU ---
        List<BookingDetail> bookings;
        List<BookingDetail> allBookings = bookingDAO.getAllBookingsForOwner(ownerId,100);

        int totalRecords;
        int totalPages;

        // --- LẤY DỮ LIỆU THEO TAB ---
        switch (tab.toLowerCase()) {
            case "tabactive":
                System.out.println("   ➡️ Loading ACTIVE tab");
                totalRecords = bookingDAO.countActiveBookingsByOwner(ownerId);
                totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
                bookings = bookingDAO.getActiveBookingsByOwner(ownerId, offset, PAGE_SIZE);
                break;
            case "tabhistory":
                System.out.println("   ➡️ Loading HISTORY tab");
                totalRecords = bookingDAO.countHistoryBookingsByOwner(ownerId);
                totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
                bookings = bookingDAO.getHistoryBookingsByOwner(ownerId, offset, PAGE_SIZE);
                break;
            default: // pending
                System.out.println("   ➡️ Loading PENDING tab (default)");
                totalRecords = bookingDAO.countPendingBookingsByOwner(ownerId);
                totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
                bookings = bookingDAO.getPendingBookingsByOwner(ownerId, offset, PAGE_SIZE);
                tab = "pending";
                break;
        }

        System.out.println("   📊 Results: " + bookings.size() + " bookings, " + totalRecords + " total records");

        // Thống kê
        int totalCars = carDAO.countCarsByOwner(ownerId);
        int totalBookings = carDAO.countTotalBookingsByOwner(ownerId);
        int activeBookings = carDAO.countApprovedBookingsByOwner(ownerId);
        int cancelledBookings = carDAO.countRejectedBookingsByOwner(ownerId);

        // Lấy danh sách xe và booking
        List<CarViewModel> myCars = carDAO.getCarsByOwner(ownerId);


        // Gửi dữ liệu sang JSP
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("activeBookings", activeBookings);
        request.setAttribute("cancelledBookings", cancelledBookings);
        request.setAttribute("myCars", myCars);
        request.setAttribute("bookings", bookings);
        request.setAttribute("allBookings", allBookings);
        request.setAttribute("tab", tab);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/view/carOwner/ownerBooking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String bookingIdStr = request.getParameter("bookingId");

        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/owner/ownerBooking");
            return;
        }
        int bookingId = Integer.parseInt(bookingIdStr);
        boolean result = false;

        if (action != null) {
            switch (action.toLowerCase()) {
                case "accept":
                    result = bookingService.approveBooking(bookingId);
                    break;
                case "reject":
                    result = bookingService.rejectBooking(bookingId);
                    break;
                case "confirmreturn":
                    result = bookingService.confirmReturnBooking(bookingId);
                    if (result) {
                        request.getSession().setAttribute("successMessage", "✅ Bạn đã xác nhận xe được trả thành công!");
                    } else {
                        request.getSession().setAttribute("errorMessage", "❌ Không thể xác nhận trả xe.");
                    }
                    break;
                default:
                    break;
            }
        }

        // Sau khi cập nhật xong, quay lại trang danh sách
        response.sendRedirect(request.getContextPath() + "/owner/ownerBooking");

    }

}
