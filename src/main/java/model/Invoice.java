package model;


import java.time.LocalDateTime;


public class Invoice {


    private int invoiceId;

    private int bookingId;

    private Integer paymentId;

    private String invoiceNumber;

    private LocalDateTime issueDate;

    private LocalDateTime dueDate;

    private double totalAmount;

    private Double taxAmount;  // Deprecated - không sử dụng nữa, giữ để tương thích DB

    private String status;

    private String notes;


    public Invoice() {

    }


    public int getInvoiceId() {

        return invoiceId;

    }


    public void setInvoiceId(int invoiceId) {

        this.invoiceId = invoiceId;

    }


    public int getBookingId() {

        return bookingId;

    }


    public void setBookingId(int bookingId) {

        this.bookingId = bookingId;

    }


    public Integer getPaymentId() {

        return paymentId;

    }


    public void setPaymentId(Integer paymentId) {

        this.paymentId = paymentId;

    }


    public String getInvoiceNumber() {

        return invoiceNumber;

    }


    public void setInvoiceNumber(String invoiceNumber) {

        this.invoiceNumber = invoiceNumber;

    }


    public LocalDateTime getIssueDate() {

        return issueDate;

    }


    public void setIssueDate(LocalDateTime issueDate) {

        this.issueDate = issueDate;

    }


    public LocalDateTime getDueDate() {

        return dueDate;

    }


    public void setDueDate(LocalDateTime dueDate) {

        this.dueDate = dueDate;

    }


    public double getTotalAmount() {

        return totalAmount;

    }


    public void setTotalAmount(double totalAmount) {

        this.totalAmount = totalAmount;

    }


    public Double getTaxAmount() {

        return taxAmount;

    }


    public void setTaxAmount(Double taxAmount) {

        this.taxAmount = taxAmount;

    }


    public String getStatus() {

        return status;

    }


    public void setStatus(String status) {

        this.status = status;

    }


    public String getNotes() {

        return notes;

    }


    public void setNotes(String notes) {

        this.notes = notes;

    }

}