package controller.car;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.CarViewModel;
import service.car.CarSingleService;

@WebServlet(name = "CarSingleServlet", urlPatterns = {"/car-single"})
public class CarSingleServlet extends HttpServlet {

    private final CarSingleService carService = new CarSingleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("cars");
            return;
        }

        try {
            int carId = Integer.parseInt(idStr);
            CarViewModel car = carService.getCarDetails(carId);

            if (car == null) {
                response.sendRedirect("cars");
                return;
            }

            request.setAttribute("car", car);
            request.getRequestDispatcher("view/car/car-single.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("cars");
        }
    }
}
