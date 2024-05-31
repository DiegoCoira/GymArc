package com.example.gymarc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class Sign_up extends AppCompatActivity {
    private final Context context = Sign_up.this;
    private EditText editTextUsername, editTextEmail, editTextPassword, editTextRepeatPassword;
    private Button buttonRegister;
    private ProgressBar progressBar;
    private String name, email, password, rpassword;
    private boolean check_info;
    private RequestQueue queue;

    private static final String TAG = "Sign_In";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextUsername = findViewById(R.id.sign_up_edittext_username);
        editTextEmail = findViewById(R.id.sign_up_edittext_email);
        editTextPassword = findViewById(R.id.sign_up_edittext_password);
        editTextRepeatPassword = findViewById(R.id.sign_up_edittext_rpassword);
        buttonRegister = findViewById(R.id.sign_up_register_button);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Inicializar RequestQueue
        queue = Volley.newRequestQueue(context);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_info = true;

                email = editTextEmail.getText().toString();
                name = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                rpassword = editTextRepeatPassword.getText().toString();

                check_info = check_inputs();

                if (check_info) {
                    send_post_register();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean check_inputs() {
        if (name.isEmpty()) {
            Toast.makeText(Sign_up.this, "Introduce un nombre de usuario", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty()) {
            Toast.makeText(Sign_up.this, "Introduce un email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty() || rpassword.isEmpty()) {
            Toast.makeText(Sign_up.this, "Introduce todos los datos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(rpassword)) {
            editTextPassword.setText("");
            editTextRepeatPassword.setText("");
            Toast.makeText(Sign_up.this, "Las contraseñas han de ser iguales", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void send_post_register() {
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
            body.put("email", email);
            body.put("password", password);
            body.put("ip_address", getIPAddress());
            body.put("device_info", getDeviceInfo());
        } catch (JSONException e) {
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/sign-up/", body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Sign_up.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Response: " + response.toString());
                        // send_log_request();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse == null) {
                            Toast.makeText(Sign_up.this, "No se pudo alcanzar al servidor", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String data = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                JSONObject json_error_data = new JSONObject(data);
                                Toast.makeText(Sign_up.this, "Error: " + json_error_data.optString("error"), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Error Response: " + json_error_data.toString());
                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Exception: " + e.getMessage());
                            }
                        }
                    }
                }
        );
        queue.add(request);
    }

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

    private String getDeviceInfo() {
        return "Device: " + android.os.Build.DEVICE + ", Model: " + android.os.Build.MODEL + ", Product: " + android.os.Build.PRODUCT;
    }
}
