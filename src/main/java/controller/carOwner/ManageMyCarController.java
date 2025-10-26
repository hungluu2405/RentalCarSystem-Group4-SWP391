package controller.carOwner;

import dao.implement.CarDAO;
import model.Car;
import model.CarViewModel;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/owner/manageMyCar")
public class ManageMyCarController extends HttpServlet {

    private CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Lấy danh sách xe của chủ xe
        List<CarViewModel> carList = carDAO.getCarsByOwner(user.getUserId());
        request.setAttribute("carList", carList);

        // Forward sang JSP hiển thị danh sách xe
        request.getRequestDispatcher("/view/carOwner/manageMyCar.jsp").forward(request, response);
    }
}
