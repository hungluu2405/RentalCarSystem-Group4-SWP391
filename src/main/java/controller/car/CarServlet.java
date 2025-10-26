package controller.car;

import dao.implement.CarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarViewModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CarServlet", urlPatterns = {"/cars"})
public class CarServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ... (Lấy các tham số name, brand, v.v. giữ nguyên) ...
        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String typeId = request.getParameter("type");
        String capacity = request.getParameter("seats");
        String fuel = request.getParameter("fuel");
        String price = request.getParameter("price");
        String location = request.getParameter("location");

        String startDate = request.getParameter("startDate");
        String pickupTime = request.getParameter("pickupTime");
        String endDate = request.getParameter("endDate");
        String dropoffTime = request.getParameter("dropoffTime");

        // ... (Lấy page, pageSize, brandList, typeList... giữ nguyên) ...
        int page = 1;
        int pageSize = 9;
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        List<String> brandList = carDAO.getAllBrands();
        List<String> typeList = carDAO.getAllTypes();
        List<Integer> capacityList = carDAO.getAllCapacities();
        List<String> fuelList = carDAO.getAllFuelTypes();

        List<CarViewModel> carList = new ArrayList<>();
        int totalCars = 0;
        int totalPages = 0;
        boolean hasError = false;

        boolean isDateTimeSearch = startDate != null && !startDate.isEmpty() &&
                pickupTime != null && !pickupTime.isEmpty() &&
                endDate != null && !endDate.isEmpty() &&
                dropoffTime != null && !dropoffTime.isEmpty();

        if (isDateTimeSearch) {
            // === LOGIC VALIDATION ===
            try {
                LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T" + pickupTime);
                LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T" + dropoffTime);

                if (!endDateTime.isAfter(startDateTime)) {
                    hasError = true;
                    // SỬA Ở ĐÂY: Lưu lỗi vào SESSION (flash attribute)
                    request.getSession().setAttribute("flashErrorMessage", "❌ Error:Return date/time must be after pickup date/time.");
                }
            } catch (Exception e) {
                hasError = true;
                // SỬA Ở ĐÂY: Lưu lỗi vào SESSION
                request.getSession().setAttribute("flashErrorMessage", "❌ Error: Invalid date/time format.");
            }

            // === SỬA LOGIC CHUYỂN HƯỚNG KHI LỖI ===
            if (hasError) {
                // Lưu lại các giá trị form cũ vào Session để trả về trang chủ
                request.getSession().setAttribute("flashForm_location", location);
                request.getSession().setAttribute("flashForm_startDate", startDate);
                request.getSession().setAttribute("flashForm_pickupTime", pickupTime);
                request.getSession().setAttribute("flashForm_endDate", endDate);
                request.getSession().setAttribute("flashForm_dropoffTime", dropoffTime);

                // Chuyển hướng (REDIRECT) về trang chủ
                response.sendRedirect(request.getContextPath() + "/home");
                return; // QUAN TRỌNG: Dừng thực thi servlet
            }

            // Nếu không có lỗi, tiếp tục tìm xe
            carList = carDAO.findCars(name, brand, typeId, capacity, fuel, price, location,
                    startDate, pickupTime, endDate, dropoffTime,
                    page, pageSize);

            totalCars = carDAO.countCars(name, brand, typeId, capacity, fuel, price, location,
                    startDate, pickupTime, endDate, dropoffTime);

        } else {
            // Trường hợp truy cập /cars trực tiếp (không có ngày giờ)
            carList = carDAO.findCars(name, brand, typeId, capacity, fuel, price, location, page, pageSize);
            totalCars = carDAO.countCars(name, brand, typeId, capacity, fuel, price, location);
        }

        // --- PHẦN NÀY CHỈ CHẠY KHI KHÔNG CÓ LỖI ---
        totalPages = (int) Math.ceil((double) totalCars / pageSize);

        // ===== Gửi dữ liệu sang JSP =====
        request.setAttribute("brandList", brandList);
        request.setAttribute("typeList", typeList);
        request.setAttribute("capacityList", capacityList);
        request.setAttribute("fuelTypeList", fuelList);
        request.setAttribute("carList", carList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // ... (Giữ lại các giá trị filter đã nhập) ...
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

        // Chuyển đến trang cars-list.jsp
        request.getRequestDispatcher("view/car/cars-list.jsp").forward(request, response);
    }
}