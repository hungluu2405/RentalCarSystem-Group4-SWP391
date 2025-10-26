package model;


import java.sql.Date;

public class Promotion {
    private int promoId;
    private String code;
    private String description;
    private double discountRate;
    private String discountType; // percent / fixed
    private Date startDate;
    private Date endDate;
    private boolean active;

    public Promotion() {
    }

    public Promotion(int promoId, String code, String description, double discountRate, String discountType, Date startDate, Date endDate, boolean active) {
        this.promoId = promoId;
        this.code = code;
        this.description = description;
        this.discountRate = discountRate;
        this.discountType = discountType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    // Getters & Setters
    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
