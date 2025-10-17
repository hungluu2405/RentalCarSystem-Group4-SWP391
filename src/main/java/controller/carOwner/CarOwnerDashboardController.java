package controller.carOwner;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.BookingDetail;
import model.CarViewModel;
import model.User;
import model.UserProfile;

@WebServlet("/owner/dashboard")
public class CarOwnerDashboardController extends HttpServlet {

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

        User owner = (User) session.getAttribute("userCarOwner");
        if (owner == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CarDAO carDAO = new CarDAO();
        BookingDAO bookingDAO = new BookingDAO();

        // Đếm dữ liệu
        int totalCars = carDAO.countCarsByOwner(owner.getUserId());
        int totalBookings = bookingDAO.countByOwner(owner.getUserId());
        int activeBookings = bookingDAO.countByOwnerAndStatus(owner.getUserId(), "Active");
        int cancelledBookings = bookingDAO.countByOwnerAndStatus(owner.getUserId(), "Cancelled");

        // Lấy danh sách xe và booking gần nhất
        List<CarViewModel> myCars = carDAO.getCarsByOwner(owner.getUserId());
        List<BookingDetail> recentBookings = bookingDAO.getRecentBookingsByOwner(owner.getUserId(), 5);

        // Gửi sang JSP
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("activeBookings", activeBookings);
        request.setAttribute("cancelledBookings", cancelledBookings);
        request.setAttribute("myCars", myCars);
        request.setAttribute("recentBookings", recentBookings);

        request.getRequestDispatcher("/view//carOwner/carOwnerDashboard.jsp").forward(request, response);
    }
}
