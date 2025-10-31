package service.car;

import dao.implement.CarDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import model.CarViewModel;

public class CarService {

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
            LocalDateTime start = LocalDateTime.parse(startDate + "T" + pickupTime);
            LocalDateTime end = LocalDateTime.parse(endDate + "T" + dropoffTime);

            // ❌ Nếu ngày nhận < hiện tại
            if (start.isBefore(now)) {
                return "❌ Pickup date/time cannot be earlier than the current time.";
            }

            // ❌ Nếu ngày trả <= ngày nhận
            if (!end.isAfter(start)) {
                return "❌ Return date/time must be after pickup date/time.";
            }

        } catch (Exception e) {
            return "❌ Invalid date/time format.";
        }

        return null; // không lỗi
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
