package controller.customer;

import dao.implement.CarDAO;
import dao.implement.UserDAO;
import model.Car;
import model.CarType;
import model.User;
import service.car.AddCarService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/customer/becomeCarOwner")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)
public class BecomeCarOwnerController extends HttpServlet {
    private final CarDAO carDAO = new CarDAO();
    private final UserDAO userDAO = new UserDAO();
    private final AddCarService addCarService = new AddCarService();

    // ✅ doGet: load dropdown dữ liệu
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println(">>> [DEBUG] Entered BecomeCarOwnerController#doGet()");

        CarDAO carDAO = new CarDAO();

        // 🔹 Lấy danh sách dropdown giống AddCarController
        List<CarType> carTypes = carDAO.getAllCarTypes();
        List<String> fuelTypes = carDAO.getAllFuelTypess();
        List<String> transmissions = carDAO.getAllTransmissions();

        // 🔹 Đưa sang JSP
        request.setAttribute("carTypes", carTypes);
        request.setAttribute("fuelTypes", fuelTypes);
        request.setAttribute("transmissions", transmissions);

        // 🔹 Hiển thị trang BecomeCarOwner.jsp
        request.getRequestDispatcher("/view/customer/becomeCarOwner.jsp").forward(request, response);
    }

    // ✅ doPost: thêm xe + đổi role
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            int year = Integer.parseInt(request.getParameter("year"));
            String licensePlate = request.getParameter("licensePlate");
            int capacity = Integer.parseInt(request.getParameter("capacity"));
            String transmission = request.getParameter("transmission");
            String fuelType = request.getParameter("fuelType");
            BigDecimal pricePerDay = new BigDecimal(request.getParameter("pricePerDay").replace(",", "").trim());
            String description = request.getParameter("description");
            String location = request.getParameter("location");
            int typeId = Integer.parseInt(request.getParameter("typeId"));

            String validationError = addCarService.validateCarData(year, pricePerDay, capacity, licensePlate);
            if (validationError != null) {
                request.setAttribute("errorMessage", validationError);
                doGet(request, response);
                return;
            }

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
            car.setOwnerId(currentUser.getUserId());
            car.setAvailability(true);

            int carId = carDAO.addCarAndReturnId(car);

            // ✅ Upload ảnh xe
            for (Part part : request.getParts()) {
                if (part.getName().equals("carImage") && part.getSize() > 0) {
                    String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "images" + File.separator + "cars";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();
                    part.write(uploadPath + File.separator + fileName);
                    carDAO.addCarImage(carId, "images/cars/" + fileName);
                }
            }

            // ✅ Đổi role Customer -> Car Owner (chính xác)
            if (currentUser.getRoleId() == 3) { // 3 = Customer
                System.out.println(">>> [DEBUG] Role before update: " + currentUser.getRoleId() + " (" + currentUser.getRoleName() + ")");

                boolean updated = userDAO.updateUserRoleById(currentUser.getUserId(), 2); // 2 = Car Owner
                System.out.println(">>> [DEBUG] Role update in DB = " + updated);

                if (updated) {
                    // ✅ Reload lại user sau khi đổi role
                    User refreshedUser = userDAO.getUserByIdWithRole(currentUser.getUserId());
                    if (refreshedUser != null) {
                        session.setAttribute("user", refreshedUser);
                        System.out.println(">>> [DEBUG] Reloaded user from DB. New roleId = " +
                                refreshedUser.getRoleId() + ", roleName = " + refreshedUser.getRoleName());
                    }
                } else {
                    System.out.println(">>> [DEBUG] Role update failed!");
                }
            }

            // ✅ Lưu carId vào session (nếu cần)
            session.setAttribute("addedCarId", carId);

            // ✅ Chuyển sang trang My Cars của Car Owner
            response.sendRedirect(request.getContextPath() + "/home");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error adding car: " + e.getMessage());
            doGet(request, response);
        }
    }
}
