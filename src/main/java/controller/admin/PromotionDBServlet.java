package controller.admin;

import dao.implement.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Promotion;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PromotionDashBoard", urlPatterns = {"/promotionDB"})
public class PromotionDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PromotionDAO promoDAO = new PromotionDAO();

        String action = request.getParameter("action");

        // ===========================
        // HANDLE TOGGLE ACTIVE
        // ===========================
        if ("toggle".equals(action)) {
            handleToggle(request, response, promoDAO);
            return;
        }

        // ===========================
        // LOAD ALL PROMOTIONS
        // ===========================
        List<Promotion> list = promoDAO.getAllPromotions();

        request.setAttribute("listP", list);
        request.setAttribute("activePage", "promotion");

        request.getRequestDispatcher("/view/admin/promotionDashboard.jsp")
                .forward(request, response);
    }

    private void handleToggle(HttpServletRequest request, HttpServletResponse response, PromotionDAO dao)
            throws IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));

            dao.updateActiveStatus(id, status);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Quay lại dashboard sau khi toggle
        response.sendRedirect("promotionDB");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
