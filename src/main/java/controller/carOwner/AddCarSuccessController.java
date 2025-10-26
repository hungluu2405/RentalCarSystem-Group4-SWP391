package controller.carOwner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import dao.implement.CarDAO;
import model.Car;
import model.CarViewModel;


import java.io.IOException;

@WebServlet("/add-car-success")
public class AddCarSuccessController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy ID xe vừa thêm từ session hoặc request parameter
        HttpSession session = request.getSession();
        Integer carId = (Integer) session.getAttribute("addedCarId");

        if (carId == null) {
            response.sendRedirect(request.getContextPath() + "/owner/addCar");
            return;
        }

        // Lấy thông tin xe vừa thêm
        CarViewModel car = carDAO.getCarById(carId);
        request.setAttribute("car", car);


        // Xóa session sau khi hiển thị
        session.removeAttribute("addedCarId");

        // Chuyển đến trang JSP hiển thị thành công
        request.getRequestDispatcher("/view/carOwner/addCarSuccess.jsp").forward(request, response);
    }
}
