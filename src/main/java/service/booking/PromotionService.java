package service.booking;

import dao.implement.PromotionDAO;
import model.Promotion;

import java.time.LocalDate;

public class PromotionService {

    PromotionDAO dao = new PromotionDAO();

    public String validatePromotion(Promotion p) {

        LocalDate today = LocalDate.now();

        if (p.getStartDate().toLocalDate().isBefore(today)) {
            return "Ngày bắt đầu không được trước ngày hôm nay.";
        }

        // 1) Ngày hết hạn không được nhỏ hơn hôm nay
        if (p.getEndDate().toLocalDate().isBefore(today)) {
            return "Ngày kết thúc không được trước ngày hôm nay.";
        }

        // 2) Nếu giảm theo phần trăm → không được quá 100%
        if ("percent".equalsIgnoreCase(p.getDiscountType())) {
            if (p.getDiscountRate() >= 100) {
                return "Mức giảm phần trăm không được lớn hơn 100%.";
            }
            if (p.getDiscountRate() < 0) {
                return "Mức giảm phần trăm không được âm.";
            }
        }

        if (!p.getStartDate().toLocalDate().isBefore(p.getEndDate().toLocalDate())) {
            return "Ngày bắt đầu phải nhỏ hơn ngày kết thúc.";
        }
        return null; // hợp lệ
    }

    public boolean createPromotion(Promotion p) {
        return dao.createPromotion(p);
    }
}
