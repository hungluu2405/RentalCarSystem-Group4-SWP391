package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import model.User;
import service.BookingService;
import dao.implement.CarDAO;
import model.Car;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final CarDAO carDAO = new CarDAO();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy dữ liệu từ form

            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String pickupTimeStr = request.getParameter("pickupTime");
            String dropoffTimeStr = request.getParameter("dropoffTime");
            String location = request.getParameter("location");

            // Kiểm tra dữ liệu bắt buộc
            if (carIdStr == null || startDateStr == null || endDateStr == null ||
                    pickupTimeStr == null || dropoffTimeStr == null ||
                    carIdStr.isEmpty() || startDateStr.isEmpty() ||
                    endDateStr.isEmpty() || pickupTimeStr.isEmpty() ||
                    dropoffTimeStr.isEmpty()) {

                request.setAttribute("error", "Vui lòng điền đầy đủ thông tin đặt xe!");
                request.getRequestDispatcher("/car-single.jsp").forward(request, response);
                return;
            }

            int carId = Integer.parseInt(carIdStr);


            //  Kiểm tra user đăng nhập

            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int userId = user.getUserId();


            //  Chuyển đổi dữ liệu ngày & giờ

            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Format "HH:mm" hoặc "HH:mm:ss"
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");
            LocalTime pickupTime = LocalTime.parse(pickupTimeStr, timeFormatter);
            LocalTime dropoffTime = LocalTime.parse(dropoffTimeStr, timeFormatter);

            // =============================
            //  Tạo đối tượng Booking
            // =============================
            Booking booking = new Booking();
            booking.setCarId(carId);
            booking.setUserId(userId);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setPickupTime(pickupTime);
            booking.setDropoffTime(dropoffTime);
            booking.setLocation(location);
            booking.setStatus("Pending");
            booking.setCreatedAt(LocalDateTime.now());




            // Kiểm tra logic ngày thuê
            if (startDate.isBefore(LocalDate.now())) {
                request.setAttribute("error", "❌ Ngày bắt đầu không được nhỏ hơn ngày hiện tại!");
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
                return;
            }

            if (endDate.isBefore(startDate)) {
                request.setAttribute("error", "❌ Ngày trả xe phải sau ngày nhận xe!");
                CarViewModel car = carDAO.getCarById(carId);
                request.setAttribute("car", car);
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
                return;
            }


            //  Gọi service xử lý logic

            boolean success = bookingService.createBooking(booking);
            CarViewModel car = carDAO.getCarById(carId);
            request.setAttribute("car", car);

            // Xử lý kết quả phản hồi

            if (success) {
                request.setAttribute("message", "✅ Đặt xe thành công! Vui lòng chờ chủ xe duyệt.");
            } else {
                request.setAttribute("error", "❌ Xe đã có người thuê trong thời gian này. Vui lòng chọn thời gian khác.");
            }

            // Quay lại trang chi tiết xe
            request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // In lỗi ra log server và hiển thị thông báo dễ hiểu
            request.setAttribute("error", "⚠️ Đã xảy ra lỗi khi đặt xe: " + e.getMessage());
            request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);

        }
    }
}
