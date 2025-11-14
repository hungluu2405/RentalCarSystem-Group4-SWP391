package controller.customer;


import dao.implement.CarImageDAO;
import dao.implement.FavouriteCarDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Car;
import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Servlet xử lý tính năng Xe Yêu Thích
 */

@WebServlet("/customer/favourite/*")

public class FavouriteCarServlet extends HttpServlet {


    private final FavouriteCarDAO favouriteCarDAO = new FavouriteCarDAO();

    private final CarImageDAO carImageDAO = new CarImageDAO();


    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)

            throws ServletException, IOException {


        HttpSession session = request.getSession(false);

        User user = (session != null) ? (User) session.getAttribute("user") : null;


        // Kiểm tra login

        if (user == null) {

            response.sendRedirect(request.getContextPath() + "/account/login");

            return;

        }


        // Kiểm tra role Customer

        if (user.getRoleId() != 3) {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            response.getWriter().write("{\"success\": false, \"message\": \"Chỉ khách hàng mới có thể sử dụng tính năng này\"}");

            return;

        }


        String pathInfo = request.getPathInfo();


        if ("/check".equals(pathInfo)) {

            handleCheckFavourite(request, response, user.getUserId());

        } else if ("/list".equals(pathInfo) || pathInfo == null) {

            handleListFavourites(request, response, user.getUserId());

        } else {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);

        }

    }


    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response)

            throws ServletException, IOException {


        HttpSession session = request.getSession(false);

        User user = (session != null) ? (User) session.getAttribute("user") : null;


        // Kiểm tra login

        if (user == null) {

            response.setContentType("application/json");

            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\"success\": false, \"message\": \"Vui lòng đăng nhập\"}");

            return;

        }


        // Kiểm tra role Customer

        if (user.getRoleId() != 3) {

            response.setContentType("application/json");

            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\"success\": false, \"message\": \"Chỉ khách hàng mới có thể sử dụng tính năng này\"}");

            return;

        }


        String pathInfo = request.getPathInfo();


        if ("/add".equals(pathInfo)) {

            handleAddFavourite(request, response, user.getUserId());

        } else if ("/remove".equals(pathInfo)) {

            handleRemoveFavourite(request, response, user.getUserId());

        } else {

            response.sendError(HttpServletResponse.SC_NOT_FOUND);

        }

    }


    /**
     * Thêm xe yêu thích
     */

    private void handleAddFavourite(HttpServletRequest request, HttpServletResponse response, int userId)

            throws IOException {


        String carIdStr = request.getParameter("carId");


        response.setContentType("application/json");

        response.setCharacterEncoding("UTF-8");


        if (carIdStr == null || carIdStr.isEmpty()) {

            response.getWriter().write("{\"success\": false, \"message\": \"Thiếu thông tin xe\"}");

            return;

        }


        try {

            int carId = Integer.parseInt(carIdStr);


            // Kiểm tra đã yêu thích chưa

            if (favouriteCarDAO.isFavourite(userId, carId)) {

                response.getWriter().write("{\"success\": false, \"message\": \"Xe này đã có trong danh sách yêu thích\"}");

                return;

            }


            // Thêm yêu thích

            boolean success = favouriteCarDAO.addFavourite(userId, carId);


            if (success) {

                response.getWriter().write("{\"success\": true, \"message\": \"Đã thêm vào danh sách yêu thích\"}");

            } else {

                response.getWriter().write("{\"success\": false, \"message\": \"Không thể thêm vào danh sách yêu thích\"}");

            }


        } catch (NumberFormatException e) {

            response.getWriter().write("{\"success\": false, \"message\": \"ID xe không hợp lệ\"}");

        }

    }


    /**
     * Xóa xe yêu thích
     */

    private void handleRemoveFavourite(HttpServletRequest request, HttpServletResponse response, int userId)

            throws IOException {


        String carIdStr = request.getParameter("carId");


        response.setContentType("application/json");

        response.setCharacterEncoding("UTF-8");


        if (carIdStr == null || carIdStr.isEmpty()) {

            response.getWriter().write("{\"success\": false, \"message\": \"Thiếu thông tin xe\"}");

            return;

        }


        try {

            int carId = Integer.parseInt(carIdStr);


            boolean success = favouriteCarDAO.removeFavourite(userId, carId);


            if (success) {

                response.getWriter().write("{\"success\": true, \"message\": \"Đã xóa khỏi danh sách yêu thích\"}");

            } else {

                response.getWriter().write("{\"success\": false, \"message\": \"Không thể xóa khỏi danh sách yêu thích\"}");

            }


        } catch (NumberFormatException e) {

            response.getWriter().write("{\"success\": false, \"message\": \"ID xe không hợp lệ\"}");

        }

    }


    /**
     * Kiểm tra xe đã yêu thích chưa (JSON)
     */

    private void handleCheckFavourite(HttpServletRequest request, HttpServletResponse response, int userId)

            throws IOException {


        String carIdStr = request.getParameter("carId");


        response.setContentType("application/json");

        response.setCharacterEncoding("UTF-8");


        if (carIdStr == null || carIdStr.isEmpty()) {

            response.getWriter().write("{\"success\": false, \"isFavourite\": false}");

            return;

        }


        try {

            int carId = Integer.parseInt(carIdStr);

            boolean isFavourite = favouriteCarDAO.isFavourite(userId, carId);


            response.getWriter().write("{\"success\": true, \"isFavourite\": " + isFavourite + "}");


        } catch (NumberFormatException e) {

            response.getWriter().write("{\"success\": false, \"isFavourite\": false}");

        }

    }


    /**
     * Hiển thị trang danh sách xe yêu thích
     */

    private void handleListFavourites(HttpServletRequest request, HttpServletResponse response, int userId)

            throws ServletException, IOException {


        // Lấy danh sách xe yêu thích

        List<Car> favouriteCars = favouriteCarDAO.getFavouriteCarsByUser(userId);


        // Lấy hình ảnh cho mỗi xe

        Map<Integer, String> carImagesMap = new HashMap<>();

        for (Car car : favouriteCars) {

            String imageUrl = carImageDAO.getImagesByCarId(car.getCarId()).toString();

            if (imageUrl != null && !imageUrl.isEmpty()) {

                carImagesMap.put(car.getCarId(), imageUrl);

            } else {

                carImagesMap.put(car.getCarId(), "/images/cars/default-car.jpg");

            }

        }


        // Set attributes

        request.setAttribute("favouriteCars", favouriteCars);

        request.setAttribute("carImagesMap", carImagesMap);

        request.setAttribute("activePage", "favourites");


        // Forward to JSP

        request.getRequestDispatcher("/view/customer/favouriteCars.jsp").forward(request, response);

    }

}