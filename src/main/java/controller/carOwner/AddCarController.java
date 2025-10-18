package controller.carOwner;

import dao.implement.CarDAO;
import model.Car;
import model.CarImage;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/owner/addCar")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,     // 1MB
        maxFileSize = 1024 * 1024 * 5,               // 5MB
        maxRequestSize = 1024 * 1024 * 10)           // 10MB

public class AddCarController extends HttpServlet {
    private CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form thêm xe
        request.getRequestDispatcher("/view/carOwner/addCar.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        int ownerId = user.getUserId();

        // Lấy dữ liệu từ form
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        int year = Integer.parseInt(request.getParameter("year"));
        String licensePlate = request.getParameter("licensePlate");
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        String transmission = request.getParameter("transmission");
        String fuelType = request.getParameter("fuelType");
        BigDecimal pricePerDay = new BigDecimal(request.getParameter("pricePerDay"));
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        int typeId = Integer.parseInt(request.getParameter("typeId"));

        Car car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setLicensePlate(licensePlate);
        car.setCapacity(capacity);
        car.setTransmission(transmission);
        car.setFuelType(fuelType);
        car.setPricePerDay(pricePerDay);
        car.setDescription(description);
        car.setLocation(location);
        car.setTypeId(typeId);
        car.setOwnerId(ownerId);
        car.setAvailability(true);

        // 1. Thêm xe vào DB, lấy carId vừa tạo
        int carId = carDAO.addCarAndReturnId(car);

        // 2. Xử lý upload ảnh
        for (Part part : request.getParts()) {
            if (part.getName().equals("carImage") && part.getSize() > 0) {
                // Lưu file vào thư mục server
                String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
                String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "cars";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                part.write(uploadPath + File.separator + fileName);

                // Lưu đường dẫn vào DB
                String imageUrl = "images/cars/" + fileName;
                carDAO.addCarImage(carId, imageUrl);
            }
        }

        // 3. Chuyển hướng về danh sách xe
        response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
    }
}
