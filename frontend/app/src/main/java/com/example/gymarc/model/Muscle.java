package com.example.gymarc.model;

import org.json.JSONObject;

public class Muscle {
    private int id;
    private String name;

    // Constructor, getters y setters

    public Muscle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Muscle(JSONObject muscleObject) {
        this.id = muscleObject.optInt("id", -1);
        this.name = muscleObject.optString("name", "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}