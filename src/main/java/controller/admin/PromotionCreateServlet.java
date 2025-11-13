package controller.admin;

import dao.implement.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Promotion;

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

        Promotion p = new Promotion();
        p.setCode(request.getParameter("code"));
        p.setDescription(request.getParameter("description"));
        p.setDiscountRate(Double.parseDouble(request.getParameter("rate")));
        p.setDiscountType(request.getParameter("type"));
        p.setStartDate(Date.valueOf(request.getParameter("start")));
        p.setEndDate(Date.valueOf(request.getParameter("end")));
        p.setActive(Boolean.parseBoolean(request.getParameter("active")));

        dao.createPromotion(p);

        response.sendRedirect("promotionDB");
    }
}
