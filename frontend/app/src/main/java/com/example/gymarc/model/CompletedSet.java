package com.example.gymarc.model;

public class CompletedSet {
    private CompletedExercise completedExercise;
    private int setNumber;
    private int actualReps;
    private double actualWeight;

    // Constructor, getters y setters

    public CompletedSet(CompletedExercise completedExercise, int setNumber, int actualReps, double actualWeight) {
        this.completedExercise = completedExercise;
        this.setNumber = setNumber;
        this.actualReps = actualReps;
        this.actualWeight = actualWeight;
    }

    public CompletedExercise getCompletedExercise() {
        return completedExercise;
    }

    public void setCompletedExercise(CompletedExercise completedExercise) {
        this.completedExercise = completedExercise;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getActualReps() {
        return actualReps;
    }

    public void setActualReps(int actualReps) {
        this.actualReps = actualReps;
    }

    public double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(double actualWeight) {
        this.actualWeight = actualWeight;
    }
}