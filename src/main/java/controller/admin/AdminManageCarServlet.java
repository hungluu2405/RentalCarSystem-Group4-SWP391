package controller.admin;

import dao.implement.CarDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.CarViewModel;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/manageCar"})
public class AdminManageCarServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        // --- Nếu có ID: hiển thị chi tiết ---
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int carId = Integer.parseInt(idParam);
                CarViewModel car = carDAO.getCarByIdForAdmin(carId);
                if (car == null) {
                    response.sendRedirect(request.getContextPath() + "/carDB");
                    return;
                }
                request.setAttribute("car", car);
                request.getRequestDispatcher("/view/admin/viewCarDetails.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/carDB");
                return;
            }
        }


    }

    // --- Xử lý xóa xe ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String deleteId = request.getParameter("deleteId");
        if (deleteId != null && !deleteId.isEmpty()) {
            try {
                int carId = Integer.parseInt(deleteId);
                boolean ok = carDAO.deleteCar(carId);
                if (ok) {
                    response.sendRedirect(request.getContextPath() + "/carDB");
                    return;
                } else {
                    request.setAttribute("error", "This car is currently in booking and cannot be deleted !");
                    CarViewModel car = carDAO.getCarByIdForAdmin(carId);
                    request.setAttribute("car", car);
                    request.getRequestDispatcher("/view/admin/viewCarDetails.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }

        response.sendRedirect(request.getContextPath() + "/admin/manageCar");
    }
}
