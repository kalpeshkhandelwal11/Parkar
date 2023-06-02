package com.example.parkar.model;

public class global_parking_model {
    String Park_name;
    double Latitude;
    double Longitude;
    String Address;
    String No_of_parking;
    String Paid_free;
    String Charges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String id;
    String user_id;
    public String getPark_name() {
        return Park_name;
    }

    public void setPark_name(String park_name) {
        Park_name = park_name;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getNo_of_parking() {
        return No_of_parking;
    }

    public void setNo_of_parking(String no_of_parking) {
        No_of_parking = no_of_parking;
    }

    public String getPaid_free() {
        return Paid_free;
    }

    public void setPaid_free(String paid_free) {
        Paid_free = paid_free;
    }

    public String getCharges() {
        return Charges;
    }

    public void setCharges(String charges) {
        Charges = charges;
    }

    public global_parking_model(String Park_name, double Latitude, double Longitude, String Address, String No_of_parking, String Paid_free, String Charges, String id , String user_id) {
        this.Park_name = Park_name;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Address = Address;
        this.No_of_parking = No_of_parking;
        this.Paid_free = Paid_free;
        this.Charges = Charges;
        this.id = id;
        this.user_id = user_id;
    }


}
