package controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Promotion;
import dao.implement.PromotionDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/check-promo")
public class PromotionController extends HttpServlet {
    private final PromotionDAO promoDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code");
        double total = Double.parseDouble(req.getParameter("total"));

        PrintWriter out = resp.getWriter();

        if (code == null || code.trim().isEmpty()) {
            out.print("{\"error\": \"Vui lòng nhập mã khuyến mãi!\"}");
            return;
        }

        Promotion promo = promoDAO.findByCode(code.trim());
        if (promo == null) {
            out.print("{\"error\": \"Mã khuyến mãi không tồn tại!\"}");
            return;
        }

        LocalDate today = LocalDate.now();
        if (promo.getStartDate().isAfter(today) || promo.getEndDate().isBefore(today)) {
            out.print("{\"error\": \"Mã khuyến mãi đã hết hạn!\"}");
            return;
        }

        if (!promo.isActive()) {
            out.print("{\"error\": \"Mã khuyến mãi đã bị vô hiệu hóa!\"}");
            return;
        }

        double discountAmount = total * (promo.getDiscountRate() / 100.0);
        double finalPrice = total - discountAmount;

        out.printf("{\"success\": true, \"discount\": %.2f, \"finalPrice\": %.2f, \"rate\": %.2f}",
                discountAmount, finalPrice, promo.getDiscountRate());
    }
}
