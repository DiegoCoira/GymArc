package com.example.gymarc.model;

import java.util.List;

public class RoutineGroup {
    private String routineName;
    private List<WeeklyRoutineDay> days;

    public RoutineGroup(String routineName, List<WeeklyRoutineDay> days) {
        this.routineName = routineName;
        this.days = days;
    }

    public String getRoutineName() {
        return routineName;
    }

    public List<WeeklyRoutineDay> getDays() {
        return days;
    }
}
