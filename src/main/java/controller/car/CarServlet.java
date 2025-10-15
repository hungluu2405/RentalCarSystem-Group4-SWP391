package controller.car;

import dao.implement.CarDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarViewModel;

@WebServlet(name = "CarServlet", urlPatterns = {"/cars"})
public class CarServlet extends HttpServlet {

    private CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String typeId = request.getParameter("type"); // typeId
        String capacity = request.getParameter("seats");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");

        int page = 1, pageSize = 9;
        try { page = Integer.parseInt(request.getParameter("page")); } catch(Exception e){}

        List<String> brandList = carDAO.getAllBrands();
        List<String> typeList = carDAO.getAllTypes(); // format "id:name"
        List<Integer> capacityList = carDAO.getAllCapacities();
        List<String> fuelList = carDAO.getAllFuelTypes();

        List<CarViewModel> carList = carDAO.findCars(name, brand, typeId, capacity, fuel, price, page, pageSize);
        int totalCars = carDAO.countCars(name, brand, typeId, capacity, fuel, price);
        int totalPages = (int)Math.ceil((double)totalCars / pageSize);

        request.setAttribute("brandList", brandList);
        request.setAttribute("typeList", typeList);
        request.setAttribute("capacityList", capacityList);
        request.setAttribute("fuelTypeList", fuelList);
        request.setAttribute("carList", carList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("view/car/cars-list.jsp").forward(request, response);
    }
}

