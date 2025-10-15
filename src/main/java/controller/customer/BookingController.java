package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Booking;
import model.CarViewModel;
import model.User;
import service.BookingService;
import dao.implement.CarDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private final BookingService bookingService = new BookingService();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // =============== LẤY DỮ LIỆU TỪ FORM ===============
            String carIdStr = request.getParameter("carId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String pickupTimeStr = request.getParameter("pickupTime");
            String dropoffTimeStr = request.getParameter("dropoffTime");
            String location = request.getParameter("location");

            // ⭐ THÊM: Nhận các tham số giá từ JavaScript
            String calculatedDiscountStr = request.getParameter("calculatedDiscount");
            String finalCalculatedPriceStr = request.getParameter("finalCalculatedPrice");
            String appliedPromoCode = request.getParameter("appliedPromoCode");

            // =============== KIỂM TRA INPUT ===============
            if (carIdStr == null || startDateStr == null || endDateStr == null ||
                    pickupTimeStr == null || dropoffTimeStr == null ||
                    carIdStr.isEmpty() || startDateStr.isEmpty() ||
                    endDateStr.isEmpty() || pickupTimeStr.isEmpty() ||
                    dropoffTimeStr.isEmpty()) {

                request.setAttribute("error", "⚠️ Vui lòng điền đầy đủ thông tin đặt xe!");
                request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
                return;
            }

            int carId = Integer.parseInt(carIdStr);

            // =============== KIỂM TRA USER ĐĂNG NHẬP ===============
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            int userId = user.getUserId();

            // =============== CHUYỂN ĐỔI DỮ LIỆU NGÀY GIỜ ===============
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");
            LocalTime pickupTime = LocalTime.parse(pickupTimeStr, timeFormatter);
            LocalTime dropoffTime = LocalTime.parse(dropoffTimeStr, timeFormatter);

            // =============== TẠO ĐỐI TƯỢNG BOOKING ===============
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

            // =============== VALIDATE LOGIC NGÀY THUÊ ===============
            if (startDate.isBefore(LocalDate.now())) {
                request.setAttribute("error", "❌ Ngày nhận xe không được nhỏ hơn hôm nay!");
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

            // ⭐ QUAN TRỌNG: Sử dụng giá đã tính từ frontend
            double finalPrice;
            double discountAmount = 0;

            if (finalCalculatedPriceStr != null && !finalCalculatedPriceStr.isEmpty()) {
                // Sử dụng giá từ frontend (đã áp discount)
                finalPrice = Double.parseDouble(finalCalculatedPriceStr);

                if (calculatedDiscountStr != null && !calculatedDiscountStr.isEmpty()) {
                    discountAmount = Double.parseDouble(calculatedDiscountStr);
                }
            } else {
                // Fallback: tính toán như cũ nếu không có giá từ frontend
                finalPrice = calculatePrice(booking, carId);
            }

            booking.setTotalPrice(finalPrice);

            // ⭐ THÊM: Sử dụng mã khuyến mãi từ form
            String finalPromoCode = (appliedPromoCode != null && !appliedPromoCode.trim().isEmpty()) ? appliedPromoCode.trim() : null;

            // =============== GỌI SERVICE XỬ LÝ BOOKING ===============
            String result = bookingService.createBooking(booking, finalPromoCode, discountAmount);

            // Lấy lại thông tin xe để hiển thị lại trang chi tiết
            CarViewModel car = carDAO.getCarById(carId);
            request.setAttribute("car", car);

            // =============== XỬ LÝ KẾT QUẢ ===============
            if (result.equals("success")) {
                request.setAttribute("message", "✅ Đặt xe thành công! Vui lòng chờ chủ xe duyệt.");
            } else {
                request.setAttribute("error", result);
            }

            request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "⚠️ Lỗi đặt xe: " + e.getMessage());
            request.getRequestDispatcher("/view/car/car-single.jsp").forward(request, response);
        }
    }

    // Hàm tính giá fallback
    private double calculatePrice(Booking booking, int carId) {
        CarDAO carDAO = new CarDAO();
        double pricePerDay = carDAO.getCarPrice(carId);

        long hours = ChronoUnit.HOURS.between(
                booking.getStartDate().atTime(booking.getPickupTime()),
                booking.getEndDate().atTime(booking.getDropoffTime())
        );
        if (hours <= 0) hours = 1;

        return (pricePerDay / 24.0) * hours;
    }
}
