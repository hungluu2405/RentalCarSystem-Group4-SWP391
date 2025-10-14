package controller.customer;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.User;
import service.BookingService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private final BookingService bookingService = new BookingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1️⃣ Lấy dữ liệu từ form
            int carId = Integer.parseInt(request.getParameter("carId"));
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String location = request.getParameter("location");

            // 2️⃣ Lấy userId từ session
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int userId = user.getUserId();

            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // 3️⃣ Tạo booking object
            Booking booking = new Booking();
            booking.setCarId(carId);
            booking.setUserId(userId);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setLocation(location);
            booking.setStatus("Pending");
            booking.setCreatedAt(LocalDateTime.now());

            // 4️⃣ Gọi service để xử lý logic
            boolean success = bookingService.createBooking(booking);

            if (success) {
                request.setAttribute("message", "Đặt xe thành công! Vui lòng chờ chủ xe duyệt.");
            } else {
                request.setAttribute("error", "Xe đã được đặt trong khoảng thời gian này. Vui lòng chọn thời gian khác.");
            }

            // 5️⃣ Quay lại trang chi tiết xe (hoặc trang confirm)
            request.getRequestDispatcher("/car-single.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi đặt xe. Vui lòng thử lại sau.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
