package controller.customer;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/favCar")
public class FavCarController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🟢 Sau này có DAO thì load danh sách xe yêu thích ở đây
        // Ví dụ:
        // List<Car> favoriteCars = carDAO.getFavoriteCarsByCustomer(customerId);
        // request.setAttribute("favoriteCars", favoriteCars);

        // 🟢 Hiện tại chỉ forward sang giao diện
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/customer/favCar.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
