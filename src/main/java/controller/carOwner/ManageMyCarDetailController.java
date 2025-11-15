package controller.carOwner;

import dao.implement.CarDAO;
import jakarta.servlet.annotation.MultipartConfig;
import model.CarType;
import model.CarViewModel;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.car.ManageCarDetailService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/owner/manageCarDetail")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 5 * 1024 * 1024,             // 5MB
        maxRequestSize = 10 * 1024 * 1024)         // 10MB
public class ManageMyCarDetailController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final ManageCarDetailService carService = new ManageCarDetailService();

    // Xử lý hiển thị chi tiết xe
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carIdParam = request.getParameter("carId");
        if (carIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
            return;
        }

        int carId = Integer.parseInt(carIdParam);
        CarViewModel car = carDAO.getCarSingleById(carId);

        if (car == null) {
            response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
            return;
        }
        List<CarType> carTypes = carDAO.getAllCarTypes();
        List<String> fuelTypes = carDAO.getAllFuelTypess();
        List<String> transmissions = carDAO.getAllTransmissions();

        // Gán vào request để JSP hiển thị
        request.setAttribute("car", car);
        request.setAttribute("carTypes", carTypes);
        request.setAttribute("fuelTypes", fuelTypes);
        request.setAttribute("transmissions", transmissions);

        request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
    }

    // Xử lý cập nhật và xóa
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int carId = Integer.parseInt(request.getParameter("carId"));

        if ("update".equals(action)) {
            try {
                CarViewModel car = new CarViewModel();
                car.setCarId(carId);
                car.setBrand(request.getParameter("brand"));
                car.setModel(request.getParameter("model"));
                car.setYear(Integer.parseInt(request.getParameter("year")));
                car.setFuelType(request.getParameter("fuelType"));
                car.setTransmission(request.getParameter("transmission"));
                car.setCapacity(Integer.parseInt(request.getParameter("capacity")));
                car.setPricePerDay(new BigDecimal(request.getParameter("pricePerDay")));
                car.setDescription(request.getParameter("description"));
                car.setLocation(request.getParameter("location"));
                car.setLicensePlate(request.getParameter("licensePlate"));
                car.setTypeId(Integer.parseInt(request.getParameter("typeId")));
                // Lấy tất cả các giá trị của tham số "availability" (có thể là ["0"] hoặc ["0","1"] khi checkbox checked)
                String[] availabilityValues = request.getParameterValues("availability");
                boolean isAvailable = false;
                if (availabilityValues != null) {
                    for (String v : availabilityValues) {
                        if ("1".equals(v)) {
                            isAvailable = true;
                            break;
                        }
                    }
                }
                if (isAvailable) {
                    car.setAvailability(1);
                } else {
                    car.setAvailability(0);
                }

                Part imagePart = request.getPart("carImage");
                if (imagePart != null && imagePart.getSize() > 0) {
                    String fileName = System.currentTimeMillis() + "_" + imagePart.getSubmittedFileName();
                    String uploadPath = request.getServletContext().getRealPath("") + "images/cars";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    imagePart.write(uploadPath + File.separator + fileName);

                    // Lưu đường dẫn tương đối
                    car.setImageUrl("images/cars/" + fileName);
                } else {
                    // Nếu không upload ảnh mới, giữ nguyên ảnh cũ
                    car.setImageUrl(request.getParameter("oldImageUrl"));
                }
                // 🔹 Đã chỉnh: gọi service để kiểm tra hợp lệ
                String error = carService.validateCarUpdate(car);
                if (error != null) {
                    request.setAttribute("error", error);
                    request.setAttribute("car", car);
                    request.setAttribute("carTypes", carDAO.getAllCarTypes());
                    request.setAttribute("fuelTypes", carDAO.getAllFuelTypess());
                    request.setAttribute("transmissions", carDAO.getAllTransmissions());
                    request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
                    return;
                }

                // 🔹 Đã chỉnh: chỉ update nếu validate pass
                boolean updated = carDAO.updateCar(car);
                if (updated) {
                    response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
                } else {
                    request.setAttribute("error", "Cập nhật xe thất bại!");
                    request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Failed to update car!");
                request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
            }
        } else if ("delete".equals(action)) {
            try {
                boolean deleted = carDAO.deleteCar(carId);

                if (deleted) {
                    response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
                    return;
                }

            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
                request.setAttribute("car", carDAO.getCarSingleById(carId));
                request.setAttribute("carTypes", carDAO.getAllCarTypes());
                request.setAttribute("fuelTypes", carDAO.getAllFuelTypess());
                request.setAttribute("transmissions", carDAO.getAllTransmissions());
                request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
            }
        }

    }
}


