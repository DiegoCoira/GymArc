package com.example.gymarc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class activity_calorie_calculator extends AppCompatActivity {

    private Context context = this;
    private ProgressBar progress_bar;
    private EditText user_birthdate, user_weight, user_height;
    private TextView user_birthdate_data, user_weight_data, user_height_data, user_gender_data, calories_maintenance, calories_surplus, calories_deficit;
    private Spinner spinner_gender, spinner_activity_factor;
    private Button button_calculate_calories;
    private String height, weight, birth_date, gender, activity_factor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            setContentView(R.layout.activity_calorie_calculator);
            progress_bar = findViewById(R.id.progress_bar_profile);
            progress_bar.setVisibility(View.INVISIBLE);
            user_birthdate = findViewById(R.id.edit_text_birth_date);
            user_height = findViewById(R.id.edit_text_height);
            user_weight = findViewById(R.id.edit_text_weight);
            spinner_gender = findViewById(R.id.spinner_gender);
            spinner_activity_factor = findViewById(R.id.spinner_activity_factor);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Intent intent = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_schedule) {
                    intent = new Intent(context, activity_schedule_routine.class);
                } else if (itemId == R.id.nav_workout) {
                    intent = new Intent(context, activity_workout.class);
                } else if (itemId == R.id.nav_calculator) {
                    intent = new Intent(context, activity_calorie_calculator.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    return true;
                }

                return false;
            });
            user_birthdate.setOnClickListener(v -> showDatePickerDialog());

            setupSpinners();
            fetchUserData(token);

            button_calculate_calories = findViewById(R.id.button_calculate_calories);
            button_calculate_calories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Extract user input and calculate calories
                    height = user_height.getText().toString();
                    weight = user_weight.getText().toString();
                    birth_date = user_birthdate.getText().toString();
                    gender = spinner_gender.getSelectedItem().toString();
                    activity_factor = spinner_activity_factor.getSelectedItem().toString();
                    float maintenance_calories = calculate_calories();
                    Toast.makeText(context, "Maintenance calories: " + maintenance_calories, Toast.LENGTH_LONG).show();

                    // Prepare request body
                    JSONObject body = new JSONObject();
                    try {
                        body.put("height", height);
                        body.put("gender", gender);
                        body.put("birth_date", birth_date);
                        body.put("weight", weight);
                        body.put("activity_factor", activity_factor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    // Send request to update user data
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.PUT,
                            "http://10.0.2.2:8000/user-data/",
                            body,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Handle response and update UI
                                    updateViewOnResponse();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle error
                                    progress_bar.setVisibility(View.INVISIBLE);
                                    if (error.networkResponse == null) {
                                        Log.e("activity_workout", "Connection error: " + error.getMessage());
                                        Toast.makeText(context, "Connection could not be established", Toast.LENGTH_LONG).show();
                                    } else {
                                        int serverCode = error.networkResponse.statusCode;
                                        Log.e("activity_workout", "Server error: " + serverCode);
                                        Toast.makeText(context, "Server error: " + serverCode, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            // Include authorization header
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", token);
                            return headers;
                        }
                    };

                    // Add request to queue
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(request);
                }
            });
        } else {
            // If user is not logged in, redirect to sign-in activity
            Intent intent = new Intent(activity_calorie_calculator.this, activity_sign_in.class);
            startActivity(intent);
        }
    }

    // Fetch user data from the server
    private void fetchUserData(String token) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8000/user-data/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse response and update UI
                        try {                            String heightResponse = response.optString("height", null);
                            String weightResponse = response.optString("weight", null);
                            String birthDateResponse = response.optString("birth_date", null);
                            String activityFactorResponse = response.optString("activity_factor", null);
                            if (heightResponse != null && weightResponse != null && birthDateResponse != null && activityFactorResponse != null){
                                height = response.getString("height");
                                weight = response.getString("weight");
                                birth_date = response.getString("birth_date");
                                gender = response.getString("gender");
                                activity_factor = response.getString("activity_factor");
                                updateViewOnResponse();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("activity_calorie_calculator", "Error fetching user data: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                // Include authorization header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    // Update UI with user data
    private void updateViewOnResponse() {
        setContentView(R.layout.activity_calorie_calculator_data);
BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_schedule) {
                intent = new Intent(context, activity_schedule_routine.class);
            } else if (itemId == R.id.nav_workout) {
                intent = new Intent(context, activity_workout.class);
            } else if (itemId == R.id.nav_calculator) {
                intent = new Intent(context, activity_calorie_calculator.class);
            }

            if (intent != null) {
                startActivity(intent);
                return true;
            }

            return false;
        });

        // Initialize views
        user_birthdate_data = findViewById(R.id.text_view_user_birth_date);
        user_weight_data = findViewById(R.id.text_view_user_weight);
        user_height_data = findViewById(R.id.text_view_user_height);
        user_gender_data = findViewById(R.id.text_view_user_gender);
        calories_maintenance = findViewById(R.id.text_view_calories_maintenance);
        calories_deficit = findViewById(R.id.text_view_calories_deficit);
        calories_surplus = findViewById(R.id.text_view_calories_surplus);

        // Set user data
        user_height_data.setText("Height: " + height + "cm");
        user_birthdate_data.setText("Birth Date: " + birth_date);
        user_weight_data.setText("Weight: " + weight + "kg");
        user_gender_data.setText("Gender: " + gender);

        // Calculate and display calories
        float calories = calculate_calories();
        calories_maintenance.setText("Maintenance: " + calories);
        calories_surplus.setText("Surplus: " + (calories + 300));
        calories_deficit.setText("Deficit: " + (calories - 450));
    }

    // Show date picker dialog to select birth date
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear+ "-" + (selectedMonth + 1) + "-" + selectedDay;
                    user_birthdate.setText(selectedDate);
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Initialize spinners with gender and activity factor options
    private void setupSpinners() {
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> activityFactorAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_factor_array, android.R.layout.simple_spinner_item);
        activityFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_activity_factor.setAdapter(activityFactorAdapter);
    }

    // Calculate total calories
    private float calculate_calories() {
        float float_activity_factor = calculate_activity_factor();
        float age = calculate_age();

        float calories_number;
        if (gender.equals("Male")) {
            calories_number = (float) (((10 * Float.valueOf(weight)) + (6.25 * Float.valueOf(height)) - (5 * age) + 5) * float_activity_factor);
        } else {
            calories_number = (float) (((10 * Float.valueOf(weight)) + (6.25 * Float.valueOf(height)) - (5 * age) - 161) * float_activity_factor);
        }
        return calories_number;
    }

    // Calculate age based on birth date
    private float calculate_age() {
        String[] parts = birth_date.split("-");
        float birth_year = Float.valueOf(parts[0]);
        float birth_month = Float.valueOf(parts[1]);
        float birth_day = Float.valueOf(parts[2]);

        float age;
        final Calendar calendar = Calendar.getInstance();
        float current_year = calendar.get(Calendar.YEAR);
        float current_month = calendar.get(Calendar.MONTH);
        float current_day = calendar.get(Calendar.DAY_OF_MONTH);

        age = current_year - birth_year;
        if (current_month < birth_month) {
            return age - 1;
        } else if (current_month == birth_month) {
            if (current_day < birth_day) {
                return age - 1;
            } else {
                return age;
            }
        } else {
            return age;
        }
    }

    // Calculate activity factor based on user selection
    private float calculate_activity_factor() {
        switch (activity_factor) {
            case "Sedentary (little or no exercise)":
                return 1.2F;
            case "Lightly active (light exercise/sports 1-3 days/week)":
                return 1.375F;
            case "Moderately active (moderate exercise/sports 3-5 days/week)":
                return 1.55F;
            case "Very active (hard exercise/sports 6-7 days a week)":
                return 1.725F;
            case "Extra active (very hard exercise/sports and physical job)":
                return 1.9F;
            default:
                Log.e("activity_calorie_calculator", "Unknown activity factor: " + activity_factor);
                return 1.0F;
        }
    }
}
