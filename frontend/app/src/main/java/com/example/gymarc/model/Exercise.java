package com.example.gymarc.model;

import java.util.List;

public class Exercise {
    private String name;
    private List<Muscle> muscles;

    // Constructor, getters y setters

    public Exercise(String name, List<Muscle> muscles) {
        this.name = name;
        this.muscles = muscles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Muscle> getMuscles() {
        return muscles;
    }

    public void setMuscles(List<Muscle> muscles) {
        this.muscles = muscles;
    }
}