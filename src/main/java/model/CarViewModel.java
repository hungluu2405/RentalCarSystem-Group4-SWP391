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
    private String imageUrl;
    private List<CarImage> images;
    private String location;
    private String licensePlate;
    private int year;
    private int typeId;
    private int availability;
    private String carOwnerName;
    private int ownerId;


    public CarViewModel() {
    }


    public CarViewModel(int carId, String brand, String model, BigDecimal pricePerDay, int capacity, String transmission, String fuelType, String carTypeName, String description, String imageUrl, List<CarImage> images, String location, String licensePlate, int year, int typeId, int availability) {
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
        this.images = images;
        this.location = location;
        this.licensePlate = licensePlate;
        this.year = year;
        this.typeId = typeId;
        this.availability = availability;
    }


    public int getAvailability() {
        return availability;
    }


    public void setAvailability(int availability) {
        this.availability = availability;
    }


    public int getTypeId() {
        return typeId;
    }


    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }


    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public String getLicensePlate() {
        return licensePlate;
    }


    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    // Getters và Setters
    public int getCarId() {
        return carId;
    }


    public void setCarId(int carId) {
        this.carId = carId;
    }


    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }


    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }


    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }


    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public String getTransmission() {
        return transmission;
    }


    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }


    public String getFuelType() {
        return fuelType;
    }


    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }


    public String getCarTypeName() {
        return carTypeName;
    }


    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getImageUrl() {
        return imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public List<CarImage> getImages() {
        return images;
    }


    public void setImages(List<CarImage> images) {
        this.images = images;
    }


    public String getCarOwnerName() {
        return carOwnerName;
    }


    public void setCarOwnerName(String carOwnerName) {
        this.carOwnerName = carOwnerName;
    }


    public int getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
