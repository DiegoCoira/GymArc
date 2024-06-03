package com.example.gymarc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymarc.model.RoutineGroup;
import com.example.gymarc.model.WeeklyRoutineDay;

import java.util.List;

public class activity_workout_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RoutineGroup> routineGroups;

    public activity_workout_adapter(List<RoutineGroup> routineGroups) {
        this.routineGroups = routineGroups;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell_view = inflater.inflate(R.layout.activity_workout_cell, parent, false);
        return new activity_workout_viewholder(cell_view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RoutineGroup group = routineGroups.get(position);
        activity_workout_viewholder groupHolder = (activity_workout_viewholder) holder;
        groupHolder.showGroupData(group);
    }

    @Override
    public int getItemCount() {
        return routineGroups.size();
    }
}