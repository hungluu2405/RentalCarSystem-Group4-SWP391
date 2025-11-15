package controller.admin;

import dao.implement.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Promotion;
import service.booking.PromotionService;

import java.io.IOException;
import java.sql.Date;

@WebServlet(urlPatterns = {"/promotionCreate"})
public class PromotionCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/view/admin/promotionCreate.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PromotionDAO dao = new PromotionDAO();
        PromotionService service = new PromotionService();
        Promotion p = new Promotion();


        p.setCode(request.getParameter("code"));
        p.setDescription(request.getParameter("description"));
        p.setDiscountType(request.getParameter("type"));
        p.setActive(Boolean.parseBoolean(request.getParameter("active")));

        try {
            p.setDiscountRate(Double.parseDouble(request.getParameter("rate")));
        } catch (Exception e) {
            request.setAttribute("error", "Mức giảm không hợp lệ.");
            request.setAttribute("promotion", p);
            request.getRequestDispatcher("/view/admin/promotionCreate.jsp")
                    .forward(request, response);
            return;
        }

        // START DATE + END DATE
        try {
            p.setStartDate(Date.valueOf(request.getParameter("start")));
            p.setEndDate(Date.valueOf(request.getParameter("end")));
        } catch (Exception e) {
            request.setAttribute("error", "Ngày bắt đầu hoặc ngày kết thúc không hợp lệ.");
            request.setAttribute("promotion", p);
            request.getRequestDispatcher("/view/admin/promotionCreate.jsp")
                    .forward(request, response);
            return;
        }

        String error = service.validatePromotion(p);

        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("promotion", p);
            request.getRequestDispatcher("/view/admin/promotionCreate.jsp")
                    .forward(request, response);
            return;
        }
        dao.createPromotion(p);

        response.sendRedirect("promotionDB?status=created");
    }
}
