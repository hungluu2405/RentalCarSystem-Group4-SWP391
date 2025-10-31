package controller.carOwner;

import dao.implement.BookingDAO;
import dao.implement.CarDAO;
import model.Booking;
import model.Car;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/owner/rental-calendar")
public class RentalCalendarServlet extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final CarDAO carDAO = new CarDAO();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User owner = (User) session.getAttribute("user");

        if (owner == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 1. Lấy danh sách xe
        List<Car> ownerCars = carDAO.getCarsByOwnerId(owner.getUserId());
        request.setAttribute("ownerCars", ownerCars);

        // 2. Lấy parameters
        String carIdParam = request.getParameter("carId");
        String monthParam = request.getParameter("month"); // "2025-10"

        // ===== SỬA PHẦN NÀY =====
        YearMonth currentYearMonth = YearMonth.now();
        int year = currentYearMonth.getYear();
        int month = currentYearMonth.getMonthValue();

        // Parse month parameter nếu có
        if (monthParam != null && !monthParam.isEmpty()) {
            try {
                String[] parts = monthParam.split("-"); // Split "2025-10"
                year = Integer.parseInt(parts[0]);      // 2025
                month = Integer.parseInt(parts[1]);     // 10
            } catch (Exception e) {
                // Nếu lỗi thì dùng tháng hiện tại
                year = currentYearMonth.getYear();
                month = currentYearMonth.getMonthValue();
            }
        }
        // ===== KẾT THÚC SỬA =====

        Integer selectedCarId = null;
        if (carIdParam != null && !carIdParam.isEmpty()) {
            selectedCarId = Integer.parseInt(carIdParam);
        } else if (!ownerCars.isEmpty()) {
            selectedCarId = ownerCars.get(0).getCarId();
        }

        request.setAttribute("selectedCarId", selectedCarId);
        request.setAttribute("selectedYear", year);
        request.setAttribute("selectedMonth", month);

        // 3. Load calendar
        if (selectedCarId != null) {
            List<Booking> bookings = bookingDAO.getBookingsByCarAndMonth(selectedCarId, year, month);

            Map<LocalDate, List<Booking>> calendarData = new HashMap<>();
            for (Booking booking : bookings) {
                LocalDate current = booking.getStartDate();
                while (!current.isAfter(booking.getEndDate())) {
                    calendarData.computeIfAbsent(current, k -> new java.util.ArrayList<>()).add(booking);
                    current = current.plusDays(1);
                }
            }

            request.setAttribute("calendarData", calendarData);
        }

        request.getRequestDispatcher("/view/carOwner/rental-calendar.jsp").forward(request, response);
    }
}
