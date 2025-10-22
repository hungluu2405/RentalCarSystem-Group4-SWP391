package controller.carOwner;

import dao.implement.CarDAO;
import model.Car;
import model.CarViewModel;
import model.User;
import model.CarType;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/owner/addCar")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,     // 1MB
        maxFileSize = 1024 * 1024 * 5,               // 5MB
        maxRequestSize = 1024 * 1024 * 10)           // 10MB

public class AddCarController extends HttpServlet {
    private CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CarType> carTypes = carDAO.getAllCarTypes();
        List<String> fuelTypes = carDAO.getAllFuelTypess();
        List<String> transmissions = carDAO.getAllTransmissions();

        // Gán vào request để JSP hiển thị
        request.setAttribute("carTypes", carTypes);
        request.setAttribute("fuelTypes", fuelTypes);
        request.setAttribute("transmissions", transmissions);

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
        String priceRaw = request.getParameter("pricePerDay");
        if (priceRaw != null) {
            priceRaw = priceRaw.replace(".", "").replace(",", "").trim(); // loại dấu . hoặc ,
        }
        BigDecimal pricePerDay = new BigDecimal(priceRaw);

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
        CarViewModel cars = carDAO.getCarById(carId);

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

    //  Lưu carId vào session để trang success biết xe nào vừa thêm
            HttpSession session = request.getSession();
            session.setAttribute("addedCarId", carId);

    //  Chuyển hướng sang trang thành công
            response.sendRedirect(request.getContextPath() + "/add-car-success");

    }
}
