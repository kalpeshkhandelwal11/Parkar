package com.example.parkar.model;

public class add_vehicle_model {
    String vehicle_model;
    String vehicle_nickname;
    String vehicle_number;
    String vehicle_owner;
    String vehicle_owner_id;
    String vehicle_societycode;
    String vehicle_type;

    public add_vehicle_model(){}

    public add_vehicle_model(String vehicle_model,
                             String vehicle_nickname,
                             String vehicle_number,
                             String vehicle_owner,
                             String vehicle_owner_id,
                             String vehicle_societycode,
                             String vehicle_type){
        this.vehicle_model = vehicle_model;
        this.vehicle_nickname = vehicle_nickname;
        this.vehicle_number = vehicle_number;
        this.vehicle_owner_id = vehicle_owner_id;
        this.vehicle_owner = vehicle_owner;
        this.vehicle_societycode = vehicle_societycode;
        this.vehicle_type = vehicle_type;

    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_nickname() {
        return vehicle_nickname;
    }

    public void setVehicle_nickname(String vehicle_nickname) {
        this.vehicle_nickname = vehicle_nickname;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getVehicle_owner() {
        return vehicle_owner;
    }

    public void setVehicle_owner(String vehicle_owner) {
        this.vehicle_owner = vehicle_owner;
    }

    public String getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(String vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
    }

    public String getVehicle_societycode() {
        return vehicle_societycode;
    }

    public void setVehicle_societycode(String vehicle_societycode) {
        this.vehicle_societycode = vehicle_societycode;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }


}
