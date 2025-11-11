package service;

import dao.implement.CarDAO;
import model.CarViewModel;

import java.math.BigDecimal;
import java.time.Year;

public class ManageCarDetailService {

    private final CarDAO carDAO = new CarDAO();

    public String validateCarUpdate(CarViewModel car) {
        //  Validate dung tích
        if (car.getCapacity() < 2 || car.getCapacity() > 7) {
            return "Số chỗ ngồi phải trong khoảng từ 2 đến 7.";
        }

        //  Validate năm sản xuất
        int currentYear = Year.now().getValue();
        if (car.getYear() > currentYear) {
            return "Năm sản xuất không được lớn hơn năm hiện tại.";
        }

        //  Validate giá thuê
        if (car.getPricePerDay() == null || car.getPricePerDay().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá thuê mỗi ngày phải lớn hơn 0.";
        }
        // Validate biển số
        String licensePlate = car.getLicensePlate();
        if (!licensePlate.matches("^\\d{2}[A-Z]-\\d{3}\\.\\d{2}$")) {
            return "Biển số xe không hợp lệ. Ví dụ: 29A-123.45";
        }

        // Check biển số trùng (nhưng cho phép trùng với chính xe hiện tại)
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

