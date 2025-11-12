package controller.carOwner;

import dao.implement.CarDAO;
import dao.implement.ReviewDAO;
import dao.implement.UserProfileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CarViewModel;
import model.UserProfile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CarOwnerInfoController", urlPatterns = {"/carOwner/info"})
public class CarOwnerInfoController extends HttpServlet {

    private final UserProfileDAO userProfileDAO = new UserProfileDAO();
    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cars");
            return;
        }

        try {
            int ownerId = Integer.parseInt(idStr);

            // ✅ Lấy thông tin chủ xe
            UserProfile owner = userProfileDAO.findByUserId(ownerId);
            if (owner == null) {
                response.sendRedirect(request.getContextPath() + "/cars");
                return;
            }

            // ✅ Lọc + phân trang review
            ReviewDAO reviewDAO = new ReviewDAO();
            String ratingParam = request.getParameter("rating");
            Integer ratingFilter = (ratingParam != null && !ratingParam.isEmpty())
                    ? Integer.parseInt(ratingParam)
                    : null;

            String carFilter = request.getParameter("car");
            int page = (request.getParameter("page") != null)
                    ? Integer.parseInt(request.getParameter("page"))
                    : 1;
            int limit = 5;
            int offset = (page - 1) * limit;

            // ✅ Gọi DAO (trả về list Map gồm Review + user info)
            List<Map<String, Object>> reviewList =
                    reviewDAO.getReviewsByOwner(ownerId, ratingFilter, carFilter, offset, limit);
            int totalReviews = reviewDAO.countReviewsByOwner(ownerId, ratingFilter, carFilter);
            int totalPages = (int) Math.ceil((double) totalReviews / limit);

            // ✅ Lấy danh sách xe mà chủ này sở hữu
            List<CarViewModel> carList = carDAO.getCarsByOwner(ownerId);

            // ✅ Lưu dữ liệu sang JSP
            request.setAttribute("owner", owner);
            request.setAttribute("carList", carList);
            request.setAttribute("reviewList", reviewList);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("ratingFilter", ratingFilter);
            request.setAttribute("carFilter", carFilter);
            request.setAttribute("totalReviews", totalReviews);

            // ✅ Lưu lại carId gần nhất (nếu có từ trang chi tiết xe)
            Integer carId = (Integer) request.getSession().getAttribute("lastViewedCarId");
            request.setAttribute("carId", carId);

            System.out.println("✅ Chủ xe: " + owner.getFullName()
                    + " | " + carList.size() + " xe | "
                    + totalReviews + " đánh giá.");

            request.getRequestDispatcher("/view/carOwner/carOwnerInfo.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/cars");
        }
    }
}