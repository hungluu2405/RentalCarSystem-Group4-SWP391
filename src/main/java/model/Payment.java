package model;

import java.time.LocalDateTime;

public class Payment {

    private int paymentId;        // PAYMENT_ID (Primary Key)
    private int bookingId;        // BOOKING_ID (Foreign Key)
    private double amount;        // AMOUNT
    private String method;        // METHOD (e.g., "PayPal")
    private String status;        // STATUS (e.g., "COMPLETED")
    private LocalDateTime paidAt; // PAID_AT (GETDATE())
    // 💡 Thêm trường này để lưu ID giao dịch của PayPal
    private String paypalTransactionId; // Đây là ID mà PayPal trả về (executedPayment.getId())

    // --- Constructors ---
    // (Cần có constructor không tham số)
    public Payment() {
    }

    // --- Getters và Setters ---

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public String getPaypalTransactionId() {
        return paypalTransactionId;
    }

    public void setPaypalTransactionId(String paypalTransactionId) {
        this.paypalTransactionId = paypalTransactionId;
    }
}