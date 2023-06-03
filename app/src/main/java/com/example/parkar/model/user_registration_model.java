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


    public String getU_society() {
        return u_society;
    }

    public void setU_society(String u_society) {
        this.u_society = u_society;
    }


    String uid;
    String u_name;
    String u_phone;
    String u_email;
    String u_society;


    public user_registration_model(String uid, String u_name, String u_phone, String u_email, String u_society) {
        this.uid = uid;
        this.u_name = u_name;
        this.u_email = u_email;
        this.u_phone = u_phone;
        this.u_society = u_society;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("u_name", u_name);
        result.put("u_email", u_email);
        result.put("u_phone", u_phone);
        return result;
    }
}