package controller.account; // Gợi ý: Đặt servlet chung ở package controller gốc, không nên đặt trong /account

import dao.implement.CarDAO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarViewModel;

@WebServlet("/home")
public class HomeServlet extends HttpServlet { // Đổi tên thành HomeServlet cho rõ nghĩa
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         System.out.println("--- HomeServlet is running! ---");
        
        // --- BỔ SUNG LOGIC GỌI DATABASE ---
        CarDAO carDAO = new CarDAO();
        
        // Lấy 6 xe được đặt nhiều nhất để hiển thị
        List<CarViewModel> topBookedCars = carDAO.findTopBookedCars(6);
        
        System.out.println("DAO returned " + topBookedCars.size() + " cars.");
        
        // Gửi danh sách xe sang JSP thông qua request attribute
        request.setAttribute("topBookedCars", topBookedCars);
        // ------------------------------------

        // Chuyển tiếp đến trang JSP để hiển thị
        // Đường dẫn của bạn đã đúng với cấu trúc thư mục mới
        request.getRequestDispatcher("/view/homePage/home.jsp").forward(request, response);
    }
}