package com.example.gymarc.model;

import java.util.List;

public class WeeklyRoutineDay {
    private String routine_name;
    private String day;
    private List<String> muscles;

    // Constructor, getters y setters

    public WeeklyRoutineDay(String routine_name, String day, List<String> muscles) {
        this.routine_name = routine_name;
        this.day = day;
        this.muscles = muscles;
    }

    public String getRoutine_name() {
        return routine_name;
    }

    public void setRoutine_name(String routine_name) {
        this.routine_name = routine_name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getMuscles() {
        return muscles;
    }

    public void setMuscles(List<String> muscles) {
        this.muscles = muscles;
    }
}