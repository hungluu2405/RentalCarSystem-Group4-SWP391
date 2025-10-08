package model;
import java.time.LocalDate;       // Cho kiểu SQL 'date' (START_DATE, END_DATE)
import java.time.LocalDateTime;

public class Booking {
    private int bookingId;
    private int carId;
    private int userId;
    private LocalDate START_DATE;
    private LocalDate END_DATE;
    private float TOTAL_PRICE;
    private String status;
    private LocalDateTime create_at;

    public Booking() {
    }

    public Booking(int bookingId, int carId, int userId, LocalDate START_DATE, LocalDate END_DATE, float TOTAL_PRICE, String status, LocalDateTime create_at) {
        this.bookingId = bookingId;
        this.carId = carId;
        this.userId = userId;
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
        this.TOTAL_PRICE = TOTAL_PRICE;
        this.status = status;
        this.create_at = create_at;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(LocalDate START_DATE) {
        this.START_DATE = START_DATE;
    }

    public LocalDate getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(LocalDate END_DATE) {
        this.END_DATE = END_DATE;
    }

    public float getTOTAL_PRICE() {
        return TOTAL_PRICE;
    }

    public void setTOTAL_PRICE(float TOTAL_PRICE) {
        this.TOTAL_PRICE = TOTAL_PRICE;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }
}
