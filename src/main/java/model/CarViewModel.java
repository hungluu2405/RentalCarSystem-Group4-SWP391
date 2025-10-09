package model;

import java.math.BigDecimal;

public class CarViewModel {
    private int carId;
    private String brand;
    private String model;
    private BigDecimal pricePerDay;
    private int capacity;
    private String transmission;
    private String fuelType;
    private String carTypeName;
    private String imageUrl;

    public CarViewModel() {}

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

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
