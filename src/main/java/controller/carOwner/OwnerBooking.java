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

@WebServlet("/owner/ownerBooking")
public class OwnerBooking extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Mock user để test
        User mockCarOwner = new User();
        mockCarOwner.setUserId(3);
        mockCarOwner.setEmail("owner@carrental.com");
        UserProfile profile = new UserProfile();
        profile.setFullName("Peter Parker");
        mockCarOwner.setUserProfile(profile);
        session.setAttribute("userCarOwner", mockCarOwner);

        User owner = (User) session.getAttribute("user");

        if (owner == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int ownerId = owner.getUserId();

        // 🔹 Thống kê
        int totalCars = carDAO.countCarsByOwner(ownerId);
        int totalBookings = bookingDAO.countByOwner(ownerId);
        int activeBookings = bookingDAO.countByOwnerAndStatus(ownerId, "Active");
        int cancelledBookings = bookingDAO.countByOwnerAndStatus(ownerId, "Cancelled");

        // 🔹 Lấy danh sách xe và booking
        List<CarViewModel> myCars = carDAO.getCarsByOwner(ownerId);
        List<BookingDetail> pendingBookings = bookingDAO.getPendingBookingsForOwner(ownerId);
        List<BookingDetail> recentBookings = bookingDAO.getRecentBookingsByOwner(ownerId, 5);

        // 🔹 Gửi dữ liệu sang JSP
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("activeBookings", activeBookings);
        request.setAttribute("cancelledBookings", cancelledBookings);
        request.setAttribute("myCars", myCars);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("recentBookings", recentBookings);

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
        String newStatus = null;

        switch (action.toLowerCase()) {
            case "accept":
                newStatus = "Accepted";
                break;
            case "reject":
                newStatus = "Rejected";
                break;
            case "changed":
                newStatus = "Changed";
                break;
            default:
                break;
        }

        if (newStatus != null) {
            bookingDAO.updateBookingStatus(bookingId, newStatus);
        }

        // ✅ Quay lại dashboard
        response.sendRedirect(request.getContextPath() + "/owner/ownerBooking");
    }


//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        String action = request.getParameter("action");
//        String bookingIdStr = request.getParameter("bookingId");
//
//        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
//            response.sendRedirect(request.getContextPath() + "/owner/ownerBooking");
//            return;
//        }
//
//        int bookingId = Integer.parseInt(bookingIdStr);
//        String newStatus = null;
//
//        if ("accept".equalsIgnoreCase(action)) {
//            newStatus = "Accepted";
//        } else if ("reject".equalsIgnoreCase(action)) {
//            newStatus = "Rejected";
//        }
//
//        if (newStatus != null) {
//            bookingDAO.updateBookingStatus(bookingId, newStatus);
//        }
//
//        // ✅ Quay lại dashboard sau khi cập nhật
//        response.sendRedirect(request.getContextPath() + "/owner/ownerBooking");
//
//    }
}
