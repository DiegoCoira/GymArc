package com.example.gymarc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymarc.model.RoutineGroup;
import com.example.gymarc.model.WeeklyRoutineDay;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_schedule_routine extends AppCompatActivity {
    private Context context = this;
    private RecyclerView recyclerView;
    private ProgressBar progressbar;
    private Button create_rutine_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if the user has already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Log.e("schedule", "token: " + token);

        if (token != null) {
            setContentView(R.layout.activity_schedule_routine);
            progressbar = findViewById(R.id.progress_bar_schedule_routine);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Intent intent = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_schedule) {
                    intent = new Intent(activity_schedule_routine.this, activity_schedule_routine.class);
                } else if (itemId == R.id.nav_workout) {
                    intent = new Intent(activity_schedule_routine.this, activity_workout.class);
                } else if (itemId == R.id.nav_calculator) {
                    intent = new Intent(activity_schedule_routine.this, activity_calorie_calculator.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    return true;
                }

                return false;
            });

            create_rutine_button = findViewById(R.id.create_routine_button);
            create_rutine_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open the create routine fragment
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragment_create_routine fragment = new fragment_create_routine();
                    fragmentTransaction.add(android.R.id.content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            List<RoutineGroup> routineGroups = new ArrayList<>();
            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://10.0.2.2:8000/weekly-training/",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressbar.setVisibility(View.INVISIBLE);
                            try {
                                if (response != null && response.length() > 0) {
                                    // Parse the JSON response and populate routineGroups list
                                    Map<String, List<WeeklyRoutineDay>> routineMap = new HashMap<>();
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject routineObject = response.getJSONObject(i);
                                        String routine_name = routineObject.getString("routine_name");
                                        JSONArray routineDaysArray = routineObject.getJSONArray("routine_days");
                                        for (int j = 0; j < routineDaysArray.length(); j++) {
                                            JSONObject dayObject = routineDaysArray.getJSONObject(j);
                                            String dayName = dayObject.getString("day");
                                            JSONArray musclesArray = dayObject.getJSONArray("muscles");
                                            List<String> musclesList = new ArrayList<>();
                                            for (int k = 0; k < musclesArray.length(); k++) {
                                                musclesList.add(musclesArray.getString(k));
                                            }
                                            WeeklyRoutineDay weeklyRoutineDay = new WeeklyRoutineDay(routine_name, dayName, musclesList);
                                            if (!routineMap.containsKey(routine_name)) {
                                                routineMap.put(routine_name, new ArrayList<>());
                                            }
                                            routineMap.get(routine_name).add(weeklyRoutineDay);
                                        }
                                    }

                                    for (Map.Entry<String, List<WeeklyRoutineDay>> entry : routineMap.entrySet()) {
                                        routineGroups.add(new RoutineGroup(entry.getKey(), entry.getValue()));
                                    }

                                    recyclerView = findViewById(R.id.workout_recycler_view);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    activity_schedule_routine_adapter adapter = new activity_schedule_routine_adapter(routineGroups);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    // If there are no routines, show a message
                                    TextView textViewNoRoutines = findViewById(R.id.text_view_no_routines);
                                    textViewNoRoutines.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressbar.setVisibility(View.INVISIBLE);
                    if (error.networkResponse == null) {
                        Log.e("activity_schedule_routine", "Connection error: " + error.getMessage());
                        Toast.makeText(context, "Connection could not be established", Toast.LENGTH_LONG).show();
                    } else {
                        int serverCode = error.networkResponse.statusCode;
                        Log.e("activity_schedule_routine", "Server error: " + serverCode);
                        Toast.makeText(context, "Server error: " + serverCode, Toast.LENGTH_LONG).show();
                    }
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    // Add authorization header with token
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", token);
                    return headers;
                }};
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);

        } else {
            // If user is not logged in, redirect to sign-in activity
            Intent intent = new Intent(activity_schedule_routine.this, activity_sign_in.class);
            startActivity(intent);
        }
    }
}
