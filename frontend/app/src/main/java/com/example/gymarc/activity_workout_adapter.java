package com.example.gymarc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymarc.model.Exercise;
import com.example.gymarc.model.RoutineGroup;

import java.util.List;

public class activity_workout_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Exercise> exercises_list;

    public activity_workout_adapter(List<Exercise> exercises_list) {
        this.exercises_list = exercises_list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_workout_cell, parent, false);
        return new activity_workout_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Exercise exercise_data = exercises_list.get(position);
        activity_workout_viewholder exercise_holder = (activity_workout_viewholder) holder;
        exercise_holder.showData(exercise_data);
    }

    @Override
    public int getItemCount() {
        return exercises_list.size();
    }
}
