package com.example.gymarc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymarc.model.RoutineGroup;

import java.util.List;

public class activity_schedule_routine_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RoutineGroup> routineGroups;

    // Constructor to initialize the list of RoutineGroups
    public activity_schedule_routine_adapter(List<RoutineGroup> routineGroups) {
        this.routineGroups = routineGroups;
    }

    // Method to specify the type of view for each position in the RecyclerView
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Method to create ViewHolder objects
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each cell
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell_view = inflater.inflate(R.layout.activity_schedule_routine_cell, parent, false);
        // Return a new instance of the ViewHolder
        return new activity_schedule_routine_viewholder(cell_view);
    }

    // Method to bind data to ViewHolder objects
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Get the RoutineGroup at the specified position
        RoutineGroup group = routineGroups.get(position);
        // Cast the holder to the appropriate ViewHolder type
        activity_schedule_routine_viewholder groupHolder = (activity_schedule_routine_viewholder) holder;
        // Call the method to display data for the RoutineGroup
        groupHolder.showGroupData(group);
    }

    // Method to determine the number of items in the list
    @Override
    public int getItemCount() {
        return routineGroups.size();
    }
}
