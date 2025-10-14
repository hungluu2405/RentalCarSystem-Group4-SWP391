package controller.carOwner;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import model.Car;
import dao.implement.CarDAO; // Import DAO tương ứng
import model.CarViewModel;

@WebServlet(name = "ManageMyCarController", urlPatterns = {"/owner/manageMyCar"})
public class ManageMyCarController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy id của chủ xe từ session
        Integer ownerId = (Integer) request.getSession().getAttribute("ownerId");
        if (ownerId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Gọi DAO để lấy danh sách xe của chủ xe này
        CarDAO carDAO = new CarDAO();
        List<CarViewModel> carList = carDAO.getCarsByOwner(ownerId);

        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String capacity = request.getParameter("seats");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");

        int page = 1, pageSize = 10;
        try { page = Integer.parseInt(request.getParameter("page")); } catch(Exception e){}

        List<String> brandList = carDAO.getAllBrands();
        List<String> modelList = carDAO.getAllModels();
        List<Integer> capacityList = carDAO.getAllCapacities();
        List<String> fuelList = carDAO.getAllFuelTypes();

        int totalCars = carDAO.countCars(name, brand, model, capacity, fuel, price);
        int totalPages = (int)Math.ceil((double)totalCars/pageSize);

        request.setAttribute("brandList", brandList);
        request.setAttribute("modelList", modelList);
        request.setAttribute("capacityList", capacityList);
        request.setAttribute("fuelTypeList", fuelList);
        request.setAttribute("carList", carList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        // Gửi dữ liệu sang JSP
        request.setAttribute("MyCarList", carList);
        request.setAttribute("activePage", "manageMyCar");

        // Forward đến trang JSP
        request.getRequestDispatcher("/view//carOwner/My_cars_list.jsp").forward(request, response);
    }
}
