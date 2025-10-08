package model;

public class Address {
    private int addressId;
    private int userId;
    private String addressLine;
    private String city;
    private String province;
    private String postalCode;
    private String country;

    // Constructors, Getters and Setters
    public Address() {}

    public Address(String addressLine, String city, String province, String postalCode, String country) {
        this.addressLine = addressLine;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getUserId() {
        return userId;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    
}