package com.example.gymarc;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymarc.model.RoutineGroup;
import com.example.gymarc.model.WeeklyRoutineDay;

public class activity_schedule_routine_viewholder extends RecyclerView.ViewHolder {
    private TextView text_day, text_muscle, text_routine_name;

    public activity_schedule_routine_viewholder(@NonNull View itemView) {
        super(itemView);
        text_day = itemView.findViewById(R.id.text_view_day);
        text_muscle = itemView.findViewById(R.id.text_view_muscles);
        text_routine_name = itemView.findViewById(R.id.text_view_routine_name);
    }

    public void showGroupData(RoutineGroup group) {
        text_routine_name.setText(group.getRoutineName());

        StringBuilder daysBuilder = new StringBuilder();
        for (WeeklyRoutineDay day : group.getDays()) {
            daysBuilder.append(day.getDay())
                    .append(" - Muscles: ").append(TextUtils.join(", ", day.getMuscles()))
                    .append("\n");
        }

        text_day.setText(daysBuilder.toString());
        text_muscle.setVisibility(View.GONE);
    }
}