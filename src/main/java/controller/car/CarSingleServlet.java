package controller;

import dao.implement.CarDAO;
import dao.implement.CarImageDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.CarViewModel;

@WebServlet(name = "CarSingleServlet", urlPatterns = {"/car-single"})
public class CarSingleServlet extends HttpServlet {
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
            CarDAO carDAO = new CarDAO();
            CarImageDAO imgDAO = new CarImageDAO();

            CarViewModel car = carDAO.getCarById(carId);
            if (car == null) {
                response.sendRedirect("cars");
                return;
            }

            car.setImages(imgDAO.getImagesByCarId(carId));

            request.setAttribute("car", car);
            request.getRequestDispatcher("view/car/car-single.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("cars");
        }
    }
}
