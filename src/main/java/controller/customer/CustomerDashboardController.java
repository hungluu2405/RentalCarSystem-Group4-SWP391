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

// Import model mới và xóa model Booking không cần thiết
import model.BookingDetail;
import model.User;
import model.UserProfile;

@WebServlet("/customer/customerDashboard")
public class CustomerDashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Dữ liệu mock user vẫn giữ nguyên để test
        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setEmail("demo@carrental.com");
        mockUser.setRoleId(3); // Customer role for mock session data
        UserProfile mockProfile = new UserProfile();
        mockProfile.setFullName("Monica Lucas");
        mockUser.setUserProfile(mockProfile);
        session.setAttribute("user", mockUser);

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        BookingDAO bookingDAO = new BookingDAO();

        // Các hàm đếm vẫn giữ nguyên
        int upcoming = bookingDAO.countByStatus(user.getUserId(), "Pending");
        int total = bookingDAO.countByUser(user.getUserId());
        int cancelled = bookingDAO.countByStatus(user.getUserId(), "Cancelled");

        // === THAY ĐỔI CHÍNH NẰM Ở ĐÂY ===
        // Gọi phương thức mới getRecentBookingDetails để lấy danh sách chi tiết
        List<BookingDetail> recentBookings = bookingDAO.getRecentBookingDetails(user.getUserId(), 5);
        // ================================

        // Gửi dữ liệu qua JSP
        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("recentBookings", recentBookings); // Gửi danh sách chi tiết qua JSP

        // Chuyển tiếp đến file JSP
        request.getRequestDispatcher("/view/customer/customerDashboard.jsp").forward(request, response);
    }
}
