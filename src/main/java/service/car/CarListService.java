package service.car;

import dao.implement.CarDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.CarViewModel;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CarListService {

    private final CarDAO carDAO = new CarDAO();

    /** ✅ Kiểm tra xem form có chứa dữ liệu ngày giờ không (form ở /home) */
    public boolean isDateTimeSearch(String startDate, String pickupTime, String endDate, String dropoffTime) {
        return startDate != null && !startDate.isEmpty()
                && pickupTime != null && !pickupTime.isEmpty()
                && endDate != null && !endDate.isEmpty()
                && dropoffTime != null && !dropoffTime.isEmpty();
    }

    /** ✅ Validate ngày giờ (chỉ dùng cho form ở /home) */
    public String validateDateTime(String startDate, String pickupTime, String endDate, String dropoffTime) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDT = LocalDateTime.parse(startDate + "T" + pickupTime);
            LocalDateTime endDT = LocalDateTime.parse(endDate + "T" + dropoffTime);

            // ❌ 1. Ngày nhận < hiện tại
            if (startDT.isBefore(now)) {
                return "❌ The pickup date/time cannot be earlier than the current time!";
            }

            // ❌ 2. Đặt xe trước quá 7 tháng
            LocalDateTime maxBookingDate = now.plusMonths(7);
            if (startDT.isAfter(maxBookingDate)) {
                return "❌ Cannot book more than 7 months in advance!";
            }

            // ❌ 3. Thời gian thuê vượt quá 7 tháng
            if (endDT.isAfter(maxBookingDate)) {
                return "❌ Booking period cannot extend beyond 7 months!";
            }

            // ❌ 4. Ngày trả < ngày nhận
            if (endDT.isBefore(startDT)) {
                return "❌ Return date/time must be after pickup date/time!";
            }

            // ❌ 5. Thời gian thuê < 24 giờ
            long totalHours = ChronoUnit.HOURS.between(startDT, endDT);
            if (totalHours < 24) {
                return "❌ Minimum rental period is 24 hours!";
            }

        } catch (Exception e) {
            return "❌ Invalid date/time format.";
        }

        return null; // ✅ Không lỗi
    }



    /** ✅ Lưu thông báo lỗi và dữ liệu cũ (flash attribute) */
    public void saveFlashError(HttpServletRequest request, String message,
                               String location, String startDate, String pickupTime,
                               String endDate, String dropoffTime) {
        HttpSession session = request.getSession();
        session.setAttribute("flashErrorMessage", message);
        session.setAttribute("flashForm_location", location);
        session.setAttribute("flashForm_startDate", startDate);
        session.setAttribute("flashForm_pickupTime", pickupTime);
        session.setAttribute("flashForm_endDate", endDate);
        session.setAttribute("flashForm_dropoffTime", dropoffTime);
    }

    /** ✅ Tìm danh sách xe */
    public List<CarViewModel> findCars(String name, String brand, String typeId, String capacity, String fuel,
                                       String price, String location, String startDate, String pickupTime,
                                       String endDate, String dropoffTime, int page, int pageSize, boolean isDateTime) {
        if (isDateTime) {
            return carDAO.findCars(name, brand, typeId, capacity, fuel, price, location,
                    startDate, pickupTime, endDate, dropoffTime, page, pageSize);
        }
        return carDAO.findCars(name, brand, typeId, capacity, fuel, price, location, page, pageSize);
    }

    /** ✅ Đếm tổng số xe */
    public int countCars(String name, String brand, String typeId, String capacity, String fuel,
                         String price, String location, String startDate, String pickupTime,
                         String endDate, String dropoffTime, boolean isDateTime) {
        if (isDateTime) {
            return carDAO.countCars(name, brand, typeId, capacity, fuel, price, location,
                    startDate, pickupTime, endDate, dropoffTime);
        }
        return carDAO.countCars(name, brand, typeId, capacity, fuel, price, location);
    }
}
