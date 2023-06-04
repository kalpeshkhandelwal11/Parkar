package com.example.parkar;

import com.google.gson.annotations.SerializedName;

public class FileUploadResponse {
    @SerializedName("isCar")
    private boolean isCar;

    @SerializedName("vehicleNumber")
    private String vehicleNumber;

    @SerializedName("color")
    private String color;

    // Constructor, getters, and setters

    public boolean isCar() {
        return isCar;
    }

    public void setCar(boolean car) {
        isCar = car;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
