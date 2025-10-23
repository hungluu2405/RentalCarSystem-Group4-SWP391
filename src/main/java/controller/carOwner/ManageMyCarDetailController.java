package controller.carOwner;

import dao.implement.CarDAO;
import jakarta.servlet.annotation.MultipartConfig;
import model.Car;
import model.CarType;
import model.CarViewModel;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

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

    // ✅ Xử lý cập nhật và xóa
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


                boolean updated = carDAO.updateCar(car);
                if (updated) {
                    response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
                } else {
                    request.setAttribute("error", "Update failed!");
                    request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Failed to update car!");
                request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
            }
        }

        else if ("delete".equals(action)) {
            try {
                carDAO.deleteCar(carId);
                response.sendRedirect(request.getContextPath() + "/owner/manageMyCar");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Failed to delete car!");
                request.getRequestDispatcher("/view/carOwner/manageMyCarDetail.jsp").forward(request, response);
            }
        }
    }
}

