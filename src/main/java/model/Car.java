package model;

import java.math.BigDecimal;

public class Car {
    private int carId;
    private int ownerId;
    private int typeId;
    private String model;
    private String brand;
    private int year;
    private String licensePlate;
    private int capacity; // sửa từ String thành int
    private String transmission;
    private String fuelType;
    private BigDecimal pricePerDay;
    private String description;
    private boolean availability;

    private String location;
    private String carOwnerName; // FULL_NAME từ USER_PROFILE
    private String typeName;     // NAME từ CAR_TYPE


    public Car() {}

    // Getters và Setters
    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public BigDecimal getPricePerDay() { return pricePerDay; }
    public void setPricePerDay(BigDecimal pricePerDay) { this.pricePerDay = pricePerDay; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isAvailability() { return availability; }    
    public void setAvailability(boolean availability) { this.availability = availability; }

    public String getCarOwnerName() {
        return carOwnerName;
    }

    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    
}
