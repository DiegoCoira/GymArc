package com.example.gymarc.model;

public class PlannedExercise {
    private CustomUser user;
    private WeeklyRoutineDay routineDay;
    private Exercise exercise;

    // Constructor, getters y setters

    public PlannedExercise(CustomUser user, WeeklyRoutineDay routineDay, Exercise exercise) {
        this.user = user;
        this.routineDay = routineDay;
        this.exercise = exercise;
    }

    public CustomUser getUser() {
        return user;
    }

    public void setUser(CustomUser user) {
        this.user = user;
    }

    public WeeklyRoutineDay getRoutineDay() {
        return routineDay;
    }

    public void setRoutineDay(WeeklyRoutineDay routineDay) {
        this.routineDay = routineDay;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}