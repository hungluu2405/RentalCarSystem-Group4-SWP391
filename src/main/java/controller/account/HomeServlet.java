package controller.account; // (Bạn nên xem xét đổi package này thành controller.home)

import dao.implement.CarDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // <<< IMPORT MỚI
import model.CarViewModel;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"}) // <<< ĐÃ SỬA LẠI TÊN (name)
public class HomeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("--- HomeServlet is running! ---");

        // ==========================================================
        // === BỔ SUNG LOGIC LẤY FLASH ATTRIBUTES TỪ SESSION ===
        // ==========================================================
        HttpSession session = request.getSession();

        // 1. Lấy thông báo lỗi
        String errorMessage = (String) session.getAttribute("flashErrorMessage");
        if (errorMessage != null) {
            request.setAttribute("errorMessage", errorMessage); // Chuyển vào request
            session.removeAttribute("flashErrorMessage"); // Xóa khỏi session
        }

        // 2. Lấy lại giá trị form cũ để điền lại
        String location = (String) session.getAttribute("flashForm_location");
        if (location != null) {
            request.setAttribute("location", location);
            session.removeAttribute("flashForm_location");
        }

        String startDate = (String) session.getAttribute("flashForm_startDate");
        if (startDate != null) {
            request.setAttribute("startDate", startDate);
            session.removeAttribute("flashForm_startDate");
        }

        String pickupTime = (String) session.getAttribute("flashForm_pickupTime");
        if (pickupTime != null) {
            request.setAttribute("pickupTime", pickupTime);
            session.removeAttribute("flashForm_pickupTime");
        }

        String endDate = (String) session.getAttribute("flashForm_endDate");
        if (endDate != null) {
            request.setAttribute("endDate", endDate);
            session.removeAttribute("flashForm_endDate");
        }

        String dropoffTime = (String) session.getAttribute("flashForm_dropoffTime");
        if (dropoffTime != null) {
            request.setAttribute("dropoffTime", dropoffTime);
            session.removeAttribute("flashForm_dropoffTime");
        }
        // === KẾT THÚC LOGIC FLASH ATTRIBUTES ===
        // ==========================================================


        // --- BỔ SUNG LOGIC GỌI DATABASE (Giữ nguyên code của bạn) ---
        CarDAO carDAO = new CarDAO();

        // Lấy 6 xe được đặt nhiều nhất để hiển thị
        List<CarViewModel> topBookedCars = carDAO.findTopBookedCars(6);

        System.out.println("DAO returned " + topBookedCars.size() + " cars.");

        // Gửi danh sách xe sang JSP thông qua request attribute
        request.setAttribute("topBookedCars", topBookedCars);
        // ------------------------------------

        // Chuyển tiếp đến trang JSP để hiển thị
        // (Sửa lại đường dẫn nếu cần, /view/homePage/home.jsp hoặc /view/home/home.jsp)
        request.getRequestDispatcher("/view/homePage/home.jsp").forward(request, response);
    }
}