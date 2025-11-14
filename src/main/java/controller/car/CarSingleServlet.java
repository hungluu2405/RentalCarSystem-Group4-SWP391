package controller.car;

import dao.implement.FavouriteCarDAO;
import dao.implement.ReviewDAO;
import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CarViewModel;
import model.User;
import model.UserProfile;
import service.car.CarSingleService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CarSingleServlet", urlPatterns = {"/car-single"})
public class CarSingleServlet extends HttpServlet {

    private final CarSingleService carService = new CarSingleService();
    private final FavouriteCarDAO favouriteCarDAO = new FavouriteCarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("cars");
            return;
        }

        try {
            int carId = Integer.parseInt(idStr);
            request.getSession().setAttribute("lastViewedCarId", carId);

            // ✅ Lấy thông tin xe
            CarViewModel car = carService.getCarDetails(carId);
            if (car == null) {
                response.sendRedirect("cars");
                return;
            }

            // ✅ Lấy thông tin chủ xe
            UserProfileDAO profileDAO = new UserProfileDAO();
            UserProfile ownerProfile = profileDAO.findByUserId(car.getOwnerId());

            // ✅ Lấy tham số lọc rating (1–5 sao)
            String ratingParam = request.getParameter("rating");
            Integer ratingFilter = (ratingParam != null && !ratingParam.isEmpty())
                    ? Integer.parseInt(ratingParam)
                    : null;

            // ✅ Thiết lập phân trang
            int page = 1;
            int pageSize = 5;
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException ignored) {
                }
            }
            int offset = (page - 1) * pageSize;

            // ✅ Lấy danh sách review có phân trang
            ReviewDAO reviewDAO = new ReviewDAO();
            List<Map<String, Object>> reviews = reviewDAO.getReviewsByCarId(carId, ratingFilter, offset, pageSize);
            int totalReviews = reviewDAO.countReviewsByCarId(carId, ratingFilter);
            int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

            boolean isFavourite = false;
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null && user.getRoleId() == 3) { // Role 3 = Customer
                    isFavourite = favouriteCarDAO.isFavourite(user.getUserId(), carId);
                }
            }


            request.setAttribute("car", car);

            request.setAttribute("isFavourite", isFavourite);
            request.setAttribute("ownerProfile", ownerProfile);
            request.setAttribute("reviews", reviews);
            request.setAttribute("ratingFilter", ratingFilter);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            // ✅ Debug log
            System.out.println("✅ car-single: carId=" + carId +
                    ", ratingFilter=" + ratingFilter +
                    ", totalReviews=" + totalReviews +
                    ", currentPage=" + page);

            request.getRequestDispatcher("/view/car/car-single.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("cars");
        }
    }
}