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
import service.AddCarService;

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
    private final AddCarService addCarService = new AddCarService();
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
    String licensePlate = request.getParameter("licensePlate");
    String transmission = request.getParameter("transmission");
    String fuelType = request.getParameter("fuelType");
    String description = request.getParameter("description");
    String location = request.getParameter("location");

    // Dùng hàm parse an toàn trong service
    int year = AddCarService.safeParseInt(request.getParameter("year"));
    int capacity = AddCarService.safeParseInt(request.getParameter("capacity"));
    int typeId = AddCarService.safeParseInt(request.getParameter("typeId"));
    BigDecimal pricePerDay = AddCarService.safeParseBigDecimal(request.getParameter("pricePerDay"));

    // Validate
    String validationError = addCarService.validateCarData(year, pricePerDay, capacity, licensePlate);
    if (validationError != null) {
        // Giữ lại dữ liệu người dùng đã nhập
        request.setAttribute("errorMessage", validationError);
        request.setAttribute("brand", brand);
        request.setAttribute("model", model);
        request.setAttribute("year", year > 0 ? year : "");
        request.setAttribute("licensePlate", licensePlate);
        request.setAttribute("capacity", capacity > 0 ? capacity : "");
        request.setAttribute("transmission", transmission);
        request.setAttribute("fuelType", fuelType);
        request.setAttribute("pricePerDay", pricePerDay != null ? pricePerDay : "");
        request.setAttribute("description", description);
        request.setAttribute("location", location);
        request.setAttribute("typeId", typeId > 0 ? typeId : "");

        // Gắn lại dropdown
        request.setAttribute("carTypes", carDAO.getAllCarTypes());
        request.setAttribute("fuelTypes", carDAO.getAllFuelTypess());
        request.setAttribute("transmissions", carDAO.getAllTransmissions());

        request.getRequestDispatcher("/view/carOwner/addCar.jsp").forward(request, response);
        return;
    }

    // Tạo đối tượng Car
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

    // Lưu xe và hình ảnh
    int carId = carDAO.addCarAndReturnId(car);
    for (Part part : request.getParts()) {
        if ("carImage".equals(part.getName()) && part.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("") + "images/cars";
            new File(uploadPath).mkdirs();
            part.write(uploadPath + File.separator + fileName);
            carDAO.addCarImage(carId, "images/cars/" + fileName);
        }
    }

    request.getSession().setAttribute("addedCarId", carId);
    response.sendRedirect(request.getContextPath() + "/add-car-success");
}

}
