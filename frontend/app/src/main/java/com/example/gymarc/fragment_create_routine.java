package com.example.gymarc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class fragment_create_routine extends Fragment {

    private EditText edit_text_routine;
    private Button button_create_routine;
    private TextView musclesEditTextMonday,musclesEditTextTuesday, musclesEditTextWednesday, musclesEditTextThursday, musclesEditTextFriday, musclesEditTextSaturday, MusclesEditTextSunday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_routine, container, false);

        // Retrieving token from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MiSharedPreferences", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        // Initializing views
        musclesEditTextMonday = view.findViewById(R.id.text_view_muscles_monday);
        musclesEditTextTuesday = view.findViewById(R.id.text_view_muscles_tuesday);
        musclesEditTextWednesday = view.findViewById(R.id.text_view_muscles_wednesday);
        musclesEditTextThursday = view.findViewById(R.id.text_view_muscles_Thursday);
        musclesEditTextFriday = view.findViewById(R.id.text_view_muscles_friday);
        musclesEditTextSaturday = view.findViewById(R.id.text_view_muscles_saturday);
        MusclesEditTextSunday = view.findViewById(R.id.text_view_muscles_sunday);
        button_create_routine = view.findViewById(R.id.button_create_routine);
        edit_text_routine = view.findViewById(R.id.edit_text_routine);

        // Setting click listeners for TextViews to select muscles
        musclesEditTextMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Monday", musclesEditTextMonday);
            }
        });

        // Similar onClickListeners for other days...

        // Button click listener for creating a routine
        button_create_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtaining routine name from EditText
                    String routineName = edit_text_routine.getText().toString();

                    // Building the JSON object with routine name and days
                    JSONObject body = new JSONObject();
                    body.put("routineName", routineName);

                    JSONArray routineArray = new JSONArray();

                    // Adding details for each day of the week
                    // Similar block for other days...

                    // Adding days array to the body JSON object
                    body.put("days", routineArray);

                    // Creating a JsonObjectRequest to send the routine data to the server
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            "http://10.0.2.2:8000/create-routine/",
                            body,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Handling successful response
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handling error response
                            if (error.networkResponse == null) {
                                Log.e("activity_schedule_routine", "Connection error: " + error.getMessage());
                                Toast.makeText(getContext(), "Connection could not be established", Toast.LENGTH_LONG).show();
                            } else {
                                int serverCode = error.networkResponse.statusCode;
                                Log.e("activity_schedule_routine", "Server error: " + serverCode);
                                Toast.makeText(getContext(), "Server error: " + serverCode, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() {
                            // Adding token to the request headers
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", token);
                            return headers;
                        }
                    };

                    // Adding the request to the request queue
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handling JSON exception
                }
            }
        });

        return view;
    }

    // Method to display a dialog for selecting muscles
    private void showMusclesDialog(final String day, final TextView musclesTextView) {
        // Retrieving muscles array from resources
        final String[] musclesArray = getResources().getStringArray(R.array.muscles_array);
        final boolean[] checkedItems = new boolean[musclesArray.length];

        // Building the dialog for muscle selection
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Muscles for " + day);
        builder.setMultiChoiceItems(musclesArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Handling selection/deselection of muscles
                checkedItems[which] = isChecked;
            }
        });

        // Confirm button action
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Building the string of selected muscles
                StringBuilder selectedMuscles = new StringBuilder();
                for (int i = 0; i < musclesArray.length; i++) {
                    if (checkedItems[i]) {
                        if (selectedMuscles.length() > 0) {
                            selectedMuscles.append(", ");
                        }
                        selectedMuscles.append(musclesArray[i]);
                    }
                }

                // Updating the TextView with selected muscles
                musclesTextView.setText(selectedMuscles.toString());
            }
        });

        // Cancel button action
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
