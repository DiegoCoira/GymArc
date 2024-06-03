package com.example.gymarc.model;

import java.util.Date;

public class CompletedExercise {
    private CustomUser user;
    private PlannedExercise plannedExercise;
    private Date date;

    // Constructor, getters y setters

    public CompletedExercise(CustomUser user, PlannedExercise plannedExercise, Date date) {
        this.user = user;
        this.plannedExercise = plannedExercise;
        this.date = date;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public PlannedExercise getPlannedExercise() {
        return plannedExercise;
    }

    public void setPlannedExercise(PlannedExercise plannedExercise) {
        this.plannedExercise = plannedExercise;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}