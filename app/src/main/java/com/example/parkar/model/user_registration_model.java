package com.example.parkar.model;

import java.util.HashMap;
import java.util.Map;

public class user_registration_model {
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getU_society() {
        return u_society;
    }

    public void setU_society(String u_society) {
        this.u_society = u_society;
    }

    public String getU_dob() {
        return u_dob;
    }

    public void setU_dob(String u_dob) {
        this.u_dob = u_dob;
    }

    public String getU_gender() {
        return u_gender;
    }

    public void setU_gender(String u_gender) {
        this.u_gender = u_gender;
    }

    public String getU_whatsapp() {
        return u_whatsapp;
    }

    public void setU_whatsapp(String u_whatsapp) {
        this.u_whatsapp = u_whatsapp;
    }

    String uid;
    String u_name;
    String u_phone;
    String u_email;
    String u_address;
    String u_society;
    String u_dob;
    String u_gender;
    String u_whatsapp;

    public user_registration_model(String uid, String u_name, String u_phone, String u_email, String u_address, String u_society, String u_dob, String u_gender, String u_whatsapp) {
        this.uid = uid;
        this.u_name = u_name;
        this.u_address = u_address;
        this.u_dob = u_dob;
        this.u_email = u_email;
        this.u_gender = u_gender;
        this.u_phone = u_phone;
        this.u_whatsapp = u_whatsapp;
        this.u_society = u_society;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("u_name", u_name);
        result.put("u_address", u_address);
        result.put("u_dob", u_dob);
        result.put("u_email", u_email);
        result.put("u_gender", u_gender);
        result.put("u_phone", u_phone);
        result.put("u_whatsapp", u_whatsapp);
        return result;
    }
}