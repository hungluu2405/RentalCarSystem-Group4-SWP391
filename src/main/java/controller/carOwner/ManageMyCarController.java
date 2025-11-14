package controller.carOwner;

import dao.implement.CarDAO;
import model.Car;
import model.CarViewModel;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/owner/manageMyCar")
public class ManageMyCarController extends HttpServlet {

    private CarDAO carDAO = new CarDAO();
    private static final int PAGE_SIZE = 4;
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

        // Pagination
        String pageParam = request.getParameter("page");
        int currentPage = 1;

        try {
            if (pageParam != null) {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            }
        } catch (Exception e) {
            currentPage = 1;
        }

        int offset = (currentPage - 1) * PAGE_SIZE;
        // Lấy bộ lọc availability từ request
        String availabilityParam = request.getParameter("availability");
        Integer availabilityFilter = null;

        if (availabilityParam != null && !availabilityParam.isEmpty()) {
            try {
                availabilityFilter = Integer.parseInt(availabilityParam);
            } catch (Exception ignored) {}
        }

        List<CarViewModel> carList = carDAO.getCarsByOwnerWithPaging(
                ownerId, offset, PAGE_SIZE, availabilityFilter
        );
        if (carList == null) carList = new ArrayList<>();
        request.setAttribute("carList", carList);

        // Lấy danh sách xe của chủ xe
        int totalCars = carDAO.countCarsByOwners(ownerId, availabilityFilter);
        int totalPages = (int) Math.ceil((double) totalCars / PAGE_SIZE);

        int totalOwnerCars = carDAO.countCarsByOwner(ownerId);
        int availableCars = carDAO.countAvailableCarsByOwner(ownerId);
        int unavailableCars = carDAO.countUnavailableCarsByOwner(ownerId);

        request.setAttribute("carList", carList);
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalOwnerCars", totalOwnerCars);
        request.setAttribute("availableCars", availableCars);
        request.setAttribute("unavailableCars", unavailableCars);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("availabilityFilter", availabilityFilter);

        // Forward sang JSP hiển thị danh sách xe
        request.getRequestDispatcher("/view/carOwner/manageMyCar.jsp").forward(request, response);
    }
}
