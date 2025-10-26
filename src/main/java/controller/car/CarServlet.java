package controller.car;

import dao.implement.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarViewModel;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CarServlet", urlPatterns = {"/cars"})
public class CarServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ===== Lấy các tham số lọc từ người dùng =====
        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String typeId = request.getParameter("type");
        String capacity = request.getParameter("seats");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");
        String location = request.getParameter("location");

        // ===== LẤY CÁC THAM SỐ THỜI GIAN TÌM KIẾM (MỚI) =====
        String startDate = request.getParameter("startDate");
        String pickupTime = request.getParameter("pickupTime");
        String endDate = request.getParameter("endDate");
        String dropoffTime = request.getParameter("dropoffTime");

        // ===== Phân trang =====
        int page = 1;
        int pageSize = 9;
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        // ===== Lấy danh sách filter =====
        List<String> brandList = carDAO.getAllBrands();
        List<String> typeList = carDAO.getAllTypes(); // "id:name"
        List<Integer> capacityList = carDAO.getAllCapacities();
        List<String> fuelList = carDAO.getAllFuelTypes();


        List<CarViewModel> carList;
        int totalCars;

        // ===== KIỂM TRA ĐỂ QUYẾT ĐỊNH GỌI HÀM DAO NÀO =====
        boolean isDateTimeSearch = startDate != null && !startDate.isEmpty() &&
                pickupTime != null && !pickupTime.isEmpty() &&
                endDate != null && !endDate.isEmpty() &&
                dropoffTime != null && !dropoffTime.isEmpty();

        if (isDateTimeSearch) {
            // TRƯỜNG HỢP 1: Tìm kiếm từ /home (có ngày giờ)
            // Gọi hàm MỚI (overloaded)
            carList = carDAO.findCars(name, brand, typeId, capacity, fuel, price, location,
                    startDate, pickupTime, endDate, dropoffTime, // Tham số mới
                    page, pageSize);

            totalCars = carDAO.countCars(name, brand, typeId, capacity, fuel, price, location,
                    startDate, pickupTime, endDate, dropoffTime); // Tham số mới
        } else {
            // TRƯỜNG HỢP 2: Truy cập /cars trực tiếp (không có ngày giờ)
            // Gọi hàm CŨ của bạn (được giữ nguyên)
            carList = carDAO.findCars(name, brand, typeId, capacity, fuel, price, location, page, pageSize);
            totalCars = carDAO.countCars(name, brand, typeId, capacity, fuel, price, location);
        }

        int totalPages = (int) Math.ceil((double) totalCars / pageSize);

        // ===== Gửi dữ liệu sang JSP =====
        request.setAttribute("brandList", brandList);
        request.setAttribute("typeList", typeList);
        request.setAttribute("capacityList", capacityList);
        request.setAttribute("fuelTypeList", fuelList);
        request.setAttribute("carList", carList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // ===== Giữ lại các giá trị filter đã nhập (BAO GỒM CẢ THỜI GIAN) =====
        request.setAttribute("name", name);
        request.setAttribute("brand", brand);
        request.setAttribute("typeId", typeId);
        request.setAttribute("capacity", capacity);
        request.setAttribute("fuel", fuel);
        request.setAttribute("price", price);
        request.setAttribute("location", location);

        // Luôn set các attribute này, kể cả khi chúng null
        // để form tìm kiếm trên trang /cars có thể hiển thị lại
        request.setAttribute("startDate", startDate);
        request.setAttribute("pickupTime", pickupTime);
        request.setAttribute("endDate", endDate);
        request.setAttribute("dropoffTime", dropoffTime);

        // ===== Chuyển đến trang JSP =====
        request.getRequestDispatcher("view/car/cars-list.jsp").forward(request, response);
    }
}