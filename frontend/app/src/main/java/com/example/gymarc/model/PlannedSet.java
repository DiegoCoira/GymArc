package com.example.gymarc.model;

public class PlannedSet {
    private PlannedExercise plannedExercise;
    private int setNumber;
    private int minReps;
    private int maxReps;
    private double weight;

    // Constructor, getters y setters

    public PlannedSet(PlannedExercise plannedExercise, int setNumber, int minReps, int maxReps, double weight) {
        this.plannedExercise = plannedExercise;
        this.setNumber = setNumber;
        this.minReps = minReps;
        this.maxReps = maxReps;
        this.weight = weight;
    }

    public PlannedExercise getPlannedExercise() {
        return plannedExercise;
    }

    public void setPlannedExercise(PlannedExercise plannedExercise) {
        this.plannedExercise = plannedExercise;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getMinReps() {
        return minReps;
    }

    public void setMinReps(int minReps) {
        this.minReps = minReps;
    }

    public int getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(int maxReps) {
        this.maxReps = maxReps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
