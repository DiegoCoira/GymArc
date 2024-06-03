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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gymarc.model.RoutineGroup;
import com.example.gymarc.model.WeeklyRoutineDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_workout extends AppCompatActivity {
    private Context context = this;
    private static String user_token;
    private RecyclerView recyclerView;
    private ProgressBar progressbar;
    private TextView text_view_routine_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Verifica si el usuario ya ha iniciado sesión
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            JSONObject body = new JSONObject();
            setContentView(R.layout.activity_workouts);
            progressbar = findViewById(R.id.progress_bar_workout);
            List<WeeklyRoutineDay> weeklyRoutineDays = new ArrayList<>();
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
                                    activity_workout_adapter adapter = new activity_workout_adapter(routineGroups);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    Log.e("activity_workout", "Empty response or response is null");
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
                        Log.e("activity_workout", "Connection error: " + error.getMessage());
                        Toast.makeText(context, "Connection could not be established", Toast.LENGTH_LONG).show();
                    } else {
                        int serverCode = error.networkResponse.statusCode;
                        Log.e("activity_workout", "Server error: " + serverCode);
                        Toast.makeText(context, "Server error: " + serverCode, Toast.LENGTH_LONG).show();
                    }
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "f38d0fdac58bf6a6ba72d6531ba4cc23");
                    return headers;
                }};
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);



        } else {
            Intent intent = new Intent(activity_workout.this, activity_sign_in.class);
            startActivity(intent);
        }
    }
}