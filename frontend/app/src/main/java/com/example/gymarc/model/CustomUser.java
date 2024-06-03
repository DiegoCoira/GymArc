package com.example.gymarc.model;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomUser {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String height;
    private String weight;
    private Date birthDate;
    private String gender;
    private Date registerDay;

    // Constructor, getters y setters

    public CustomUser(int userId, String username, String email, String password, String height, String weight, Date birthDate, String gender, Date registerDay) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.birthDate = birthDate;
        this.gender = gender;
        this.registerDay = registerDay;
    }

    public CustomUser(JSONObject userObject) {
        this.userId = userObject.optInt("userId", -1);
        this.username = userObject.optString("username", "");
        this.email = userObject.optString("email", "");
        this.password = userObject.optString("password", "");
        this.height = userObject.optString("height", "");
        this.weight = userObject.optString("weight", "");
        this.birthDate = parseDate(userObject.optString("birthDate", ""));
        this.gender = userObject.optString("gender", "");
        this.registerDay = parseDate(userObject.optString("registerDay", ""));
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getRegisterDay() {
        return registerDay;
    }

    public void setRegisterDay(Date registerDay) {
        this.registerDay = registerDay;
    }
}
