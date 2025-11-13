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
        int ownerId = user.getUserId();


        // Lấy danh sách xe của chủ xe
        List<CarViewModel> carList = carDAO.getCarsByOwner(ownerId);

        int totalCars = carDAO.countCarsByOwner(ownerId);
        int availableCars = carDAO.countAvailableCarsByOwner(ownerId);
        int unavailableCars = carDAO.countUnavailableCarsByOwner(ownerId);

        request.setAttribute("carList", carList);
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("availableCars", availableCars);
        request.setAttribute("unavailableCars", unavailableCars);


        // Forward sang JSP hiển thị danh sách xe
        request.getRequestDispatcher("/view/carOwner/manageMyCar.jsp").forward(request, response);
    }
}
