package model;

import java.math.BigDecimal;
import java.util.List;

public class CarViewModel {
    private int carId;
    private String brand;
    private String model;
    private BigDecimal pricePerDay;
    private int capacity;
    private String transmission;
    private String fuelType;
    private String carTypeName;
    private String description;
    private String imageUrl;           // thêm trường ảnh chính
    private List<CarImage> images;     // thêm danh sách ảnh phụ (nếu có)

    public CarViewModel() {}

    // Constructor đầy đủ 10 tham số (nếu cần cho DAO)
    public CarViewModel(int carId, String brand, String model, BigDecimal pricePerDay,
                        int capacity, String transmission, String fuelType,
                        String carTypeName, String description, String imageUrl) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.pricePerDay = pricePerDay;
        this.capacity = capacity;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.carTypeName = carTypeName;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters và Setters
    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public String getCarTypeName() { return carTypeName; }
    public void setCarTypeName(String carTypeName) { this.carTypeName = carTypeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<CarImage> getImages() { return images; }
    public void setImages(List<CarImage> images) { this.images = images; }
}
