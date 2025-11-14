package model;

import java.sql.Timestamp;

public class FavouriteCar {
    private int favouriteId;
    private int userId;
    private int carId;
    private Timestamp createdAt;

    public FavouriteCar() {
    }

    public FavouriteCar(int userId, int carId) {
        this.userId = userId;
        this.carId = carId;
    }

    public FavouriteCar(int favouriteId, int userId, int carId, Timestamp createdAt) {
        this.favouriteId = favouriteId;
        this.userId = userId;
        this.carId = carId;
        this.createdAt = createdAt;
    }


    // Getters and Setters
    public int getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(int favouriteId) {
        this.favouriteId = favouriteId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override

    public String toString() {
        return "FavouriteCar{" +
                "favouriteId=" + favouriteId +
                ", userId=" + userId +
                ", carId=" + carId +
                ", createdAt=" + createdAt +
                '}';
    }
}