package com.example.parkar.model;

public class notification_model {
    String message;
    String time;
    String date;
    String city;

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    String notification_id;
    String vehicle_no;
notification_model(){}
    public notification_model(String message, String time, String date, String city, String lat, String lon, String region, String notification_id , String vehicle_no) {
        this.message = message;
        this.time = time;
        this.date = date;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.region = region;
        this.notification_id = notification_id;
        this.vehicle_no = vehicle_no;
    }

    String lat;
    String lon;
    String region;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


}
