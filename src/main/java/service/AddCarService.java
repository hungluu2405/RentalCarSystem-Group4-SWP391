package service;

import dao.implement.CarDAO;

import java.math.BigDecimal;
import java.time.Year;

public class AddCarService {
    private CarDAO carDAO = new CarDAO();

    public String validateCarData(int year, BigDecimal pricePerDay, int capacity, String licensePlate) {
        int currentYear = Year.now().getValue();

        if (year <= 0) {
            return "Năm sản xuất không được âm hoặc bằng 0!";
        }
        if (year > currentYear) {
            return "Năm sản xuất không được lớn hơn năm hiện tại!";
        }
        if (pricePerDay == null || pricePerDay.compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá thuê mỗi ngày phải lớn hơn 0!";
        }
        if (capacity < 2) {
            return "Sức chứa của xe phải lớn hơn hoặc bằng 2!";
        }
        if (capacity > 7) {
            return "Sức chứa của xe không được lớn hơn 7!";
        }
        if (licensePlate == null || !licensePlate.matches("^\\d{2}[A-Z]-\\d{3}\\.\\d{2}$")) {
            return "Biển số xe không hợp lệ. Ví dụ: 29A-123.45";
        }
        if (isDuplicateLicensePlate(licensePlate)) {
            return "Biển số xe đã tồn tại trong hệ thống!";
        }

        return null;
    }

    public boolean isDuplicateLicensePlate(String licensePlate) {
        return carDAO.isLicensePlateExists(licensePlate);
    }


    /**
     * Chuyển chuỗi sang int an toàn (trả về 0 nếu null, rỗng, hoặc không phải số)
     */
    public static int safeParseInt(String value) {
        if (value == null || value.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Chuyển chuỗi sang BigDecimal an toàn (trả về null nếu không hợp lệ)
     */
    public static BigDecimal safeParseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            value = value.replace(".", "").replace(",", "").trim();
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Lấy chuỗi an toàn, loại bỏ khoảng trắng, trả về "" nếu null
     */
    public String safeGetString(String value) {
        return (value == null) ? "" : value.trim();
    }
}
