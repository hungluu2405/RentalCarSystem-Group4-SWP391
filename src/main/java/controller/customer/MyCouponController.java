package controller.customer;

import dao.implement.PromotionDAO;
import model.Promotion;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User; // Giả sử model User của bạn

@WebServlet("/customer/myCoupon")
public class MyCouponController extends HttpServlet {

    private final PromotionDAO promotionDAO = new PromotionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {

            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {

            List<Promotion> availableCoupons = promotionDAO.getAvailablePromotions();

            // 3. Đặt danh sách vào Request Scope (Tên này phải khớp với tên trong JSP)
            request.setAttribute("availableCoupons", availableCoupons);


            request.getRequestDispatcher("/view/customer/myCoupon.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi tải danh sách mã giảm giá.");
        }
    }
}