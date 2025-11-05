package service;

import dao.implement.CarDAO;
import model.CarViewModel;

import java.math.BigDecimal;
import java.time.Year;

public class ManageCarDetailService {

    private final CarDAO carDAO = new CarDAO();

    public String validateCarUpdate(CarViewModel car) {
        // 1️⃣ Validate dung tích
        if (car.getCapacity() < 2 || car.getCapacity() > 7) {
            return "Số chỗ ngồi phải trong khoảng từ 1 đến 7.";
        }

        // 2️⃣ Validate năm sản xuất
        int currentYear = Year.now().getValue();
        if (car.getYear() > currentYear) {
            return "Năm sản xuất không được lớn hơn năm hiện tại.";
        }

        // 3️⃣ Validate giá thuê
        if (car.getPricePerDay() == null || car.getPricePerDay().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá thuê mỗi ngày phải lớn hơn 0.";
        }

        // gọi lại hàm DAO bạn đã có
        // ✅ Check biển số trùng (nhưng cho phép trùng với chính xe hiện tại)
        if (isDuplicateLicensePlate(car.getLicensePlate(), car.getCarId())) {
            return "Biển số xe đã tồn tại trong hệ thống!";
        }

        return null; // Không có lỗi
    }

    /**
     * Kiểm tra xem biển số có trùng với xe khác trong hệ thống hay không
     */
    private boolean isDuplicateLicensePlate(String licensePlate, int currentCarId) {
        Integer existingCarId = carDAO.getCarIdByLicensePlate(licensePlate);
        return existingCarId != null && existingCarId != currentCarId;
    }
}

