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

    // Constructor to initialize the list of exercises
    public activity_workout_adapter(List<Exercise> exercises_list) {
        this.exercises_list = exercises_list;
    }

    // Method to create a new ViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for exercise cell
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_workout_cell, parent, false);
        // Return a new ViewHolder containing the inflated view
        return new activity_workout_viewholder(view);
    }

    // Method to bind exercise data to a ViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Get exercise data at the given position
        Exercise exercise_data = exercises_list.get(position);
        // Cast the ViewHolder to the specific ViewHolder type for this activity
        activity_workout_viewholder exercise_holder = (activity_workout_viewholder) holder;
        // Show exercise data in the ViewHolder
        exercise_holder.showData(exercise_data);
    }

    // Method to get the total number of items in the exercises list
    @Override
    public int getItemCount() {
        return exercises_list.size();
    }
}
