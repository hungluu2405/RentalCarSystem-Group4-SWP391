package service.car;

import dao.implement.CarDAO;
import dao.implement.CarImageDAO;
import model.CarViewModel;

public class CarSingleService {

    private final CarDAO carDAO = new CarDAO();
    private final CarImageDAO carImageDAO = new CarImageDAO();

    /** ✅ Lấy thông tin chi tiết xe và danh sách ảnh */
    public CarViewModel getCarDetails(int carId) {
        CarViewModel car = carDAO.getCarById(carId);
        if (car != null) {
            car.setImages(carImageDAO.getImagesByCarId(carId));
        }
        return car;
    }
}
