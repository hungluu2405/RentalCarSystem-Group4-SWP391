package controller.customer;


import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.*;

import model.Promotion;

import dao.implement.PromotionDAO;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/promotions")
public class PromotionApiController extends HttpServlet {
    private final PromotionDAO promoDAO = new PromotionDAO();
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        try {

            List<Promotion> promotions = promoDAO.getAvailablePromotions();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("promotions", promotions);

            out.print(gson.toJson(response));
        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to load promotions");
            out.print(gson.toJson(errorResponse));
        }
    }
}