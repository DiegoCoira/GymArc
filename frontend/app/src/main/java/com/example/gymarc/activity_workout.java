package com.example.gymarc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymarc.model.Exercise;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_workout extends AppCompatActivity {
    private Context context = this;
    private RecyclerView recyclerView;

    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        // Redirect to sign-in activity if token is null
        if (token == null){
            Intent intent = new Intent(activity_workout.this, activity_sign_in.class);
            startActivity(intent);
        }

        // Set the layout
        setContentView(R.layout.activity_workout);
        progressbar = findViewById(R.id.progress_bar_workout);

        // Initialize and setup bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_schedule) {
                intent = new Intent(activity_workout.this, activity_schedule_routine.class);
            } else if (itemId == R.id.nav_workout) {
                intent = new Intent(activity_workout.this, activity_workout.class);
            } else if (itemId == R.id.nav_calculator) {
                intent = new Intent(activity_workout.this, activity_calorie_calculator.class);
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Initialize list to store exercises
        List<Exercise> exercises_list = new ArrayList<>();

        // Create JSON request to fetch exercises
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "http://10.0.2.2:8000/get-exercises/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Hide progress bar
                        progressbar.setVisibility(View.INVISIBLE);

                        // Iterate through JSON array and parse exercises
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject exerciseJson = response.getJSONObject(i);
                                // Retrieve exercise name from JSON object
                                String name = exerciseJson.optString("name", "Unknown Exercise"); // Use optString to avoid exceptions
                                // Create Exercise object and add to list
                                Exercise exercise = new Exercise(name);
                                exercises_list.add(exercise);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        // Set up RecyclerView with LinearLayoutManager and custom adapter
                        recyclerView = findViewById(R.id.workout_recycler_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        activity_workout_adapter adapter = new activity_workout_adapter(exercises_list);
                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hide progress bar
                        progressbar.setVisibility(View.INVISIBLE);

                        // Handle Volley error responses
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
            // Override method to include Authorization header with token
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                return headers;
            }
        };

        // Add the JSON request to the Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
