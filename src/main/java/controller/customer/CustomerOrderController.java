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

@WebServlet("/customer/customerOrder")
public class CustomerOrderController extends HttpServlet {

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
        int userId = user.getUserId(); // 👈 Lấy userId động từ Session

        // Các hàm đếm (giữ nguyên, dùng userId động)
        int upcoming = bookingDAO.countByStatus(userId, "Pending");
        int total = bookingDAO.countByUser(userId);
        int cancelled = bookingDAO.countByStatus(userId, "Cancelled");

        // === THAY ĐỔI CHÍNH: LẤY DỮ LIỆU ĐỘNG ===
        // Lấy tất cả đơn hàng (để chia tab Current/History trong JSP)
        // Chúng ta lấy 100 đơn hàng gần nhất (hoặc tùy bạn đặt limit)
        List<BookingDetail> allBookings = bookingDAO.getBookingDetailsByUserId(userId, 100);
        // LƯU Ý: BookingDAO chưa có hàm getBookingDetailsByUserId, chúng ta sẽ sửa ở bước 2

        // Gửi dữ liệu qua JSP
        request.setAttribute("upcoming", upcoming);
        request.setAttribute("total", total);
        request.setAttribute("cancelled", cancelled);
        request.setAttribute("allBookings", allBookings); // 👈 Gửi toàn bộ danh sách

        // Chuyển tiếp đến file JSP
        request.getRequestDispatcher("/view/customer/customerOrder.jsp").forward(request, response);
    }
}
