package service.car;

import dao.implement.CarDAO;
import dao.implement.CarImageDAO;
import model.CarViewModel;

public class CarSingleService {

    private final CarDAO carDAO = new CarDAO();
    private final CarImageDAO carImageDAO = new CarImageDAO();

    /**
     * ✅ Lấy thông tin chi tiết xe (chỉ trả về nếu xe khả dụng)
     */
    public CarViewModel getCarDetails(int carId) {
        CarViewModel car = carDAO.getCarById(carId);

        // Nếu không tồn tại hoặc không khả dụng => trả về null
        if (car == null || car.getAvailability() == 0) {
            return null;
        }

        // Nếu xe hợp lệ => lấy thêm danh sách ảnh
        car.setImages(carImageDAO.getImagesByCarId(carId));
        return car;
    }
}
