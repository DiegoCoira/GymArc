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
        View view = inflater.inflate(R.layout.fragment_create_routine, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MiSharedPreferences", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);


        musclesEditTextMonday = view.findViewById(R.id.text_view_muscles_monday);
        musclesEditTextTuesday = view.findViewById(R.id.text_view_muscles_tuesday);
        musclesEditTextWednesday = view.findViewById(R.id.text_view_muscles_wednesday);
        musclesEditTextThursday = view.findViewById(R.id.text_view_muscles_Thursday);
        musclesEditTextFriday = view.findViewById(R.id.text_view_muscles_friday);
        musclesEditTextSaturday = view.findViewById(R.id.text_view_muscles_saturday);
        MusclesEditTextSunday = view.findViewById(R.id.text_view_muscles_sunday);
        button_create_routine = view.findViewById(R.id.button_create_routine);
        edit_text_routine = view.findViewById(R.id.edit_text_routine);

        musclesEditTextMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Monday", musclesEditTextMonday);
            }
        });

        musclesEditTextTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Tuesday", musclesEditTextTuesday);
            }
        });
        musclesEditTextWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Wednesday", musclesEditTextWednesday);
            }
        });
        musclesEditTextThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Thursday", musclesEditTextThursday);
            }
        });
        musclesEditTextFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Friday", musclesEditTextFriday);
            }
        });
        musclesEditTextSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Saturday", musclesEditTextSaturday);
            }
        });
        MusclesEditTextSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMusclesDialog("Sunday", MusclesEditTextSunday);
            }
        });

        button_create_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Obtener el nombre de la rutina
                    String routineName = edit_text_routine.getText().toString();

                    // Construir el JSONObject con el nombre de la rutina y el JSONArray de los días
                    JSONObject body = new JSONObject();
                    body.put("routineName", routineName);

                    JSONArray routineArray = new JSONArray();

                    JSONObject monday = new JSONObject();
                    monday.put("day", "Monday");
                    monday.put("muscles", musclesEditTextMonday.getText().toString());
                    routineArray.put(monday);

                    JSONObject tuesday = new JSONObject();
                    tuesday.put("day", "Tuesday");
                    tuesday.put("muscles", musclesEditTextTuesday.getText().toString());
                    routineArray.put(tuesday);

                    JSONObject wednesday = new JSONObject();
                    wednesday.put("day", "Wednesday");
                    wednesday.put("muscles", musclesEditTextWednesday.getText().toString());
                    routineArray.put(wednesday);

                    JSONObject thursday = new JSONObject();
                    thursday.put("day", "Thursday");
                    thursday.put("muscles", musclesEditTextThursday.getText().toString());
                    routineArray.put(thursday);

                    JSONObject friday = new JSONObject();
                    friday.put("day", "Friday");
                    friday.put("muscles", musclesEditTextFriday.getText().toString());
                    routineArray.put(friday);

                    JSONObject saturday = new JSONObject();
                    saturday.put("day", "Saturday");
                    saturday.put("muscles", musclesEditTextSaturday.getText().toString());
                    routineArray.put(saturday);

                    JSONObject sunday = new JSONObject();
                    sunday.put("day", "Sunday");
                    sunday.put("muscles", MusclesEditTextSunday.getText().toString());
                    routineArray.put(sunday);

                    body.put("days", routineArray);
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            "http://10.0.2.2:8000/create-routine/",
                            body,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
                    ){
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", token);
                            return headers;
                        }};

                    // Agregar la solicitud a la cola
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                    // Manejar la excepción
                }
            }
        });

        return view;
    }

    private void showMusclesDialog(final String day, final TextView musclesTextView) {

        final String[] musclesArray = getResources().getStringArray(R.array.muscles_array);
        final boolean[] checkedItems = new boolean[musclesArray.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Muscles for " + day);
        builder.setMultiChoiceItems(musclesArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Marca/desmarca los elementos seleccionados en el array
                checkedItems[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Construye la cadena de músculos seleccionados
                StringBuilder selectedMuscles = new StringBuilder();
                for (int i = 0; i < musclesArray.length; i++) {
                    if (checkedItems[i]) {
                        if (selectedMuscles.length() > 0) {
                            selectedMuscles.append(", ");
                        }
                        selectedMuscles.append(musclesArray[i]);
                    }
                }

                // Actualiza el TextView con los músculos seleccionados
                musclesTextView.setText(selectedMuscles.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
