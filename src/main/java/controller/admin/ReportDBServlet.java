package controller.admin;

import dao.implement.BookingPromotionDAO;
import model.BookingPromotion;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/reportDB")
public class ReportDBServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BookingPromotionDAO dao = new BookingPromotionDAO();

        // Giả sử DAO của bạn có hàm getAll()
//        List<BookingPromotion> list = dao.getAll();

//        request.setAttribute("list", list);
        request.getRequestDispatcher("/view/admin/Report.jsp").forward(request, response);
    }
}
