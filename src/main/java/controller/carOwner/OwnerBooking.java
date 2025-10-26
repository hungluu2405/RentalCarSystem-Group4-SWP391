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
import model.UserProfile;
import service.BookingService;

@WebServlet("/owner/ownerBooking")
public class OwnerBooking extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BookingDAO bookingDAO = new BookingDAO();
    private final BookingService bookingService = new BookingService();

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

        // 🔹 Thống kê
        int totalCars = carDAO.countCarsByOwner(ownerId);
        int totalBookings = carDAO.countTotalBookingsByOwner(ownerId);
        int activeBookings = carDAO.countApprovedBookingsByOwner(ownerId);
        int cancelledBookings = carDAO.countRejectedBookingsByOwner(ownerId);

        // 🔹 Lấy danh sách xe và booking
        List<CarViewModel> myCars = carDAO.getCarsByOwner(ownerId);
        List<BookingDetail> pendingBookings = bookingDAO.getPendingBookingsForOwner(ownerId);
       List<BookingDetail> recentBookings = bookingDAO.getRecentBookingsByOwner(ownerId, 100);
        List<BookingDetail> historyBookings = bookingDAO.getHistoryBookingsForOwner(ownerId);

        // 🔹 Gửi dữ liệu sang JSP
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("activeBookings", activeBookings);
        request.setAttribute("cancelledBookings", cancelledBookings);
        request.setAttribute("myCars", myCars);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("recentBookings", recentBookings);
        request.setAttribute("historyBookings", historyBookings);

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
                default:
                    break;
            }
        }

        // ✅ Sau khi cập nhật xong, quay lại trang danh sách
        response.sendRedirect(request.getContextPath() + "/owner/ownerBooking");

    }

}
