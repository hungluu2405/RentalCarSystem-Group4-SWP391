package controller.car;

import dao.implement.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarViewModel;
import service.car.CarListService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CarListServlet", urlPatterns = {"/cars"})
public class CarListServlet extends HttpServlet {

    private final CarListService carListService = new CarListService();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String typeId = request.getParameter("type");
        String capacity = request.getParameter("capacity");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");
        String location = request.getParameter("location");

        String startDate = request.getParameter("startDate");
        String pickupTime = request.getParameter("pickupTime");
        String endDate = request.getParameter("endDate");
        String dropoffTime = request.getParameter("dropoffTime");

        int page = 1, pageSize = 9;
        try {
            if (request.getParameter("page") != null)
                page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ignored) {
        }

        boolean isDateTimeSearch = carListService.isDateTimeSearch(startDate, pickupTime, endDate, dropoffTime);

        if (isDateTimeSearch) {
            // ✅ Validate thời gian (chỉ khi tìm từ /home)
            String error = carListService.validateDateTime(startDate, pickupTime, endDate, dropoffTime);
            if (error != null) {
                carListService.saveFlashError(request, error, location, startDate, pickupTime, endDate, dropoffTime);
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
        }

        // ✅ Lấy danh sách xe (theo kiểu search phù hợp)
        List<CarViewModel> carList = carListService.findCars(
                name, brand, typeId, capacity, fuel, price, location,
                startDate, pickupTime, endDate, dropoffTime, page, pageSize, isDateTimeSearch
        );

        int totalCars = carListService.countCars(
                name, brand, typeId, capacity, fuel, price, location,
                startDate, pickupTime, endDate, dropoffTime, isDateTimeSearch
        );

        int totalPages = (int) Math.ceil((double) totalCars / pageSize);

        // Gửi dữ liệu sang JSP
        request.setAttribute("brandList", carDAO.getAllBrands());
        request.setAttribute("typeList", carDAO.getAllTypes());
        request.setAttribute("capacityList", carDAO.getAllCapacities());
        request.setAttribute("fuelTypeList", carDAO.getAllFuelTypes());
        request.setAttribute("carList", carList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("name", name);
        request.setAttribute("brand", brand);
        request.setAttribute("typeId", typeId);
        request.setAttribute("capacity", capacity);
        request.setAttribute("fuel", fuel);
        request.setAttribute("price", price);
        request.setAttribute("location", location);
        request.setAttribute("startDate", startDate);
        request.setAttribute("pickupTime", pickupTime);
        request.setAttribute("endDate", endDate);
        request.setAttribute("dropoffTime", dropoffTime);

        request.getRequestDispatcher("view/car/cars-list.jsp").forward(request, response);
    }
}
