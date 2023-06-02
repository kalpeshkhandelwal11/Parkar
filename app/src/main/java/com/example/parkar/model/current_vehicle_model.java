package com.example.parkar.model;

public class current_vehicle_model {
    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;

    }

    String vehicle_no;

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLongi() {
        return Longi;
    }

    public void setLongi(String Longi) {
        this.Longi = Longi;
    }

    String Lat;
    String Longi;

    public current_vehicle_model(String vehicle_no, String Lat, String Longi){
        this.vehicle_no = vehicle_no;
        this.Lat = Lat;
        this.Longi = Longi;
    }
}
