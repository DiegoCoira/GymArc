package com.example.gymarc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class activity_sign_up extends AppCompatActivity {
    private final Context context = activity_sign_up.this;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextRepeatPassword;
    private Button buttonRegister, buttonLogin;
    private ProgressBar progressBar;
    private String name, email, password, rpassword;
    private boolean check_info;
    private RequestQueue queue;

    private static final String TAG = "Sign_Up";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout
        setContentView(R.layout.activity_sign_up);

        // Retrieve token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        // If token exists, redirect to schedule routine activity
        if(token != null){
            Intent intent = new Intent(activity_sign_up.this, activity_schedule_routine.class);
            startActivity(intent);
        }

        // Initialize views
        editTextUsername = findViewById(R.id.sign_up_edittext_username);
        editTextEmail = findViewById(R.id.sign_up_edittext_email);
        editTextPassword = findViewById(R.id.sign_up_edittext_password);
        editTextRepeatPassword = findViewById(R.id.sign_up_edittext_rpassword);
        buttonRegister = findViewById(R.id.sign_up_register_button);
        buttonLogin = findViewById(R.id.sign_up_login_button);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Initialize RequestQueue
        queue = Volley.newRequestQueue(context);

        // Register button click listener
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate input fields
                check_info = true;
                email = editTextEmail.getText().toString();
                name = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                rpassword = editTextRepeatPassword.getText().toString();
                check_info = check_inputs();

                // If input fields are valid, send registration request
                if (check_info) {
                    send_post_register();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        // Login button click listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to sign-in activity
                Intent intent = new Intent(activity_sign_up.this, activity_sign_in.class);
                startActivity(intent);
            }
        });
    }

    // Method to check input fields for validity
    private boolean check_inputs() {
        if (name.isEmpty()) {
            Toast.makeText(activity_sign_up.this, "Enter a username", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(activity_sign_up.this, "Enter an email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty() || rpassword.isEmpty()) {
            Toast.makeText(activity_sign_up.this, "Enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(rpassword)) {
            editTextPassword.setText("");
            editTextRepeatPassword.setText("");
            Toast.makeText(activity_sign_up.this, "Passwords must match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Method to send POST request for registration
    private void send_post_register() {
        JSONObject body = new JSONObject();
        try {
            body.put("username", name);
            body.put("email", email);
            body.put("password", password);
            body.put("ip_address", getIPAddress());
            body.put("device_info", getDeviceInfo());
            Log.e(TAG, "Body: " + body.toString());
        } catch (JSONException e) {
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/sign-up/", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Hide progress bar
                        progressBar.setVisibility(View.INVISIBLE);
                        String receivedToken;
                        try {
                            // Retrieve token from response
                            receivedToken = response.getString("token");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Store token in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", receivedToken);
                        editor.apply();

                        // Redirect to schedule routine activity and finish current activity
                        Intent intent = new Intent(activity_sign_up.this, activity_schedule_routine.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Hide progress bar
                        progressBar.setVisibility(View.INVISIBLE);

                        // Handle error responses
                        if (error.networkResponse == null) {
                            Log.e(TAG, "Error Response: " + error.toString());
                            Toast.makeText(activity_sign_up.this, "Could not reach server", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                // Parse error data and display error message
                                String data = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                JSONObject json_error_data = new JSONObject(data);
                                Toast.makeText(activity_sign_up.this, "Error: " + json_error_data.optString("error"), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Error Response: " + json_error_data.toString());
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Exception: " + e.getMessage());
                            }
                        }
                    }
                }
        );
        // Add request to the RequestQueue
        queue.add(request);
    }

    // Method to get device's IP address
    private String getIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (isIPv4) {
                            return sAddr;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    // Method to get device's information
    private String getDeviceInfo() {
        return "Device: " + android.os.Build.DEVICE + ", Model: " + android.os.Build.MODEL + ", Product: " + android.os.Build.PRODUCT;
    }
}
