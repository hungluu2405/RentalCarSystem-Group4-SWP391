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

    /** ✅ Kiểm tra hợp lệ ngày giờ (dành cho form tìm kiếm ở trang chủ) */
    public String validateDateTime(String startDate, String pickupTime, String endDate, String dropoffTime) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDT = LocalDateTime.parse(startDate + "T" + pickupTime);
            LocalDateTime endDT = LocalDateTime.parse(endDate + "T" + dropoffTime);

            // ❌ 1. Ngày nhận xe < thời điểm hiện tại
            if (startDT.isBefore(now)) {
                return "❌ Thời gian nhận xe không được sớm hơn thời điểm hiện tại!";
            }

            // ❌ 2. Đặt xe trước quá 7 tháng
            LocalDateTime maxBookingDate = now.plusMonths(7);
            if (startDT.isAfter(maxBookingDate)) {
                return "❌ Không thể đặt xe trước quá 7 tháng!";
            }

            // ❌ 3. Thời gian thuê kéo dài quá 3 tháng
            if (endDT.isAfter(startDT.plusMonths(3))) {
                return "❌ Thời gian thuê không được vượt quá 3 tháng!";
            }

            // ❌ 4. Ngày trả xe < ngày nhận xe
            if (endDT.isBefore(startDT)) {
                return "❌ Ngày trả xe phải sau ngày nhận xe!";
            }

            // ❌ 5. Thời gian thuê < 24 giờ
            long totalHours = ChronoUnit.HOURS.between(startDT, endDT);
            if (totalHours < 24) {
                return "❌ Thời gian thuê tối thiểu là 24 giờ!";
            }

        } catch (Exception e) {
            return "❌ Định dạng ngày hoặc giờ không hợp lệ!";
        }

        return null; // ✅ Không có lỗi
    }

    /** ✅ Lưu thông báo lỗi và dữ liệu form cũ (flash attribute) */
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

    /** ✅ Đếm tổng số xe phù hợp */
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
