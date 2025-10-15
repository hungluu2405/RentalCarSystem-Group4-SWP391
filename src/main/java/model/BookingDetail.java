package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDetail {
    private int bookingId;
    private String carName;
    private String location;      // ✅ dùng khi xe có location cố định
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double totalPrice;    // ✅ hiển thị giá tổng (từ bảng booking)
    private LocalTime pickupTime;
    private LocalTime dropoffTime;
    // Constructors
    public BookingDetail() {}

    public BookingDetail(int bookingId, String carName, String location, LocalDate startDate, LocalDate endDate, String status, double totalPrice, LocalTime pickupTime, LocalTime dropoffTime) {
        this.bookingId = bookingId;
        this.carName = carName;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
    }

    public LocalTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalTime getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(LocalTime dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    // Getters & Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
