package service;

import dao.implement.CarDAO;

public class AddCarService {
    private CarDAO carDAO = new CarDAO();

    public boolean isDuplicateLicensePlate(String licensePlate) {
        return carDAO.isLicensePlateExists(licensePlate);
    }
}
