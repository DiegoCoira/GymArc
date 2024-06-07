package com.example.gymarc;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymarc.model.Exercise;
import com.example.gymarc.model.RoutineGroup;
import com.example.gymarc.model.WeeklyRoutineDay;

public class activity_workout_viewholder extends RecyclerView.ViewHolder {

    private TextView exercise_name;

    // Constructor to initialize views
    public activity_workout_viewholder(@NonNull View itemView) {
        super(itemView);
        // Initialize the exercise name view
        exercise_name = itemView.findViewById(R.id.text_workout_cell_exercise_name);
    }

    // Method to display exercise data in the ViewHolder
    public void showData(Exercise exercise_data) {
        // Set the exercise name in the TextView
        exercise_name.setText(exercise_data.getName());
    }
}
