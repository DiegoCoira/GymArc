package com.example.gymarc;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymarc.model.RoutineGroup;
import com.example.gymarc.model.WeeklyRoutineDay;

public class activity_schedule_routine_viewholder extends RecyclerView.ViewHolder {
    private TextView text_day, text_routine_name;

    // Constructor to initialize views
    public activity_schedule_routine_viewholder(@NonNull View itemView) {
        super(itemView);
        // Initialize views
        text_day = itemView.findViewById(R.id.text_view_day);
        text_routine_name = itemView.findViewById(R.id.text_view_routine_name);
    }

    // Method to display data for a RoutineGroup
    public void showGroupData(RoutineGroup group) {
        // Set routine name
        text_routine_name.setText(group.getRoutineName());

        // Build a string with routine days and associated muscles
        StringBuilder daysBuilder = new StringBuilder();
        for (WeeklyRoutineDay day : group.getDays()) {
            daysBuilder.append(day.getDay())
                    .append(" - Muscles: ").append(TextUtils.join(", ", day.getMuscles()))
                    .append("\n");
        }

        // Set the text for routine days
        text_day.setText(daysBuilder.toString());
    }
}
