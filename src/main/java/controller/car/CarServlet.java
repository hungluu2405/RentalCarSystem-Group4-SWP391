package controller.car;

import dao.implement.CarDAO;
import dao.implement.CarTypeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import model.CarType;
import model.CarViewModel;

@WebServlet(name = "CarServlet", urlPatterns = {"/cars"})
public class CarServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CarDAO carDAO = new CarDAO();
        CarTypeDAO carTypeDAO = new CarTypeDAO();

        String typeIdStr = request.getParameter("typeId");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String capacityStr = request.getParameter("capacity");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");

        Integer typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.valueOf(typeIdStr) : null;
        Integer capacity = (capacityStr != null && !capacityStr.isEmpty()) ? Integer.valueOf(capacityStr) : null;
        BigDecimal minPrice = (minPriceStr != null && !minPriceStr.isEmpty()) ? new BigDecimal(minPriceStr) : null;
        BigDecimal maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty()) ? new BigDecimal(maxPriceStr) : null;

        System.out.println("Filters: typeId=" + typeId + ", brand=" + brand + ", model=" + model
                + ", capacity=" + capacity + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice);

        long start = System.currentTimeMillis();
        List<CarViewModel> carList = carDAO.findCars(typeId, brand, model, capacity, minPrice, maxPrice);
        long end = System.currentTimeMillis();
        System.out.println("Query time: " + (end - start) + " ms, carList size=" + carList.size());

        List<CarType> carTypeList = carTypeDAO.getAllCarTypes();

        request.setAttribute("carList", carList);
        request.setAttribute("carTypeList", carTypeList);
        request.getRequestDispatcher("/view/car/cars-list.jsp").forward(request, response);
    }
}
