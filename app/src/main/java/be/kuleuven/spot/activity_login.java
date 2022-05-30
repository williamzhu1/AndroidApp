package be.kuleuven.spot;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.lang.Thread;

import kotlin.reflect.KAnnotatedElement;

public class activity_login extends AppCompatActivity {

    private EditText inputEmail_login;
    private EditText inputPassword_login;
    private TextView DoNotHaveAccount;

    private double latitude;
    private double longitude;
    private LocationManager locationManager;

    private RequestQueue requestQueue;
    private static final String checkEmailExistUrl = "https://studev.groept.be/api/a21pt215/ifEmailExist/";
    private static final String checkPasswordUrl = "https://studev.groept.be/api/a21pt215/passwordWithEmail/";
    private static final String getUsernameUrl = "https://studev.groept.be/api/a21pt215/getUsername/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail_login = (EditText) findViewById(R.id.inputEmail_login);
        inputPassword_login = (EditText) findViewById(R.id.inputPassword_login);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(activity_login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity_login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity_login.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = Double.valueOf(location.getLongitude());
                latitude = Double.valueOf(location.getLatitude());
            }
        });

    }

    public void onLogin_Clicked(View caller) {

        String email = inputEmail_login.getText().toString();
        String password = inputPassword_login.getText().toString();

        //Toast.makeText(activity_register.this, password + ", " + confirmPassword , Toast.LENGTH_LONG).show();
        checkIfEmailPassword(email,password);
    }

    public void checkIfEmailPassword(String email, String password){
        requestQueue = Volley.newRequestQueue(this);
        String emailUrl = checkEmailExistUrl + email;
        String passwordUrl = checkPasswordUrl + email;
        String usernameUrl = getUsernameUrl + email;

        JsonArrayRequest getUsernameRequest = new JsonArrayRequest(Request.Method.GET,usernameUrl,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                JSONObject o = null;
                try {
                    o = response.getJSONObject(0);
                    info = (String)o.get("username");
                    //System.out.println(info);
                    //System.out.println(password + " ahoaho");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String username = info;
                Intent intent = new Intent(activity_login.this, activity_home.class);
                Bundle bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login.this, "Username not found", Toast.LENGTH_LONG).show();
            }
        });

        JsonArrayRequest passwordRequest = new JsonArrayRequest(Request.Method.GET,passwordUrl,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String info = "";
                JSONObject o = null;
                try {
                    o = response.getJSONObject(0);
                    info = (String)o.get("password");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(info.equals(password)){


                    requestQueue.add(getUsernameRequest);

                }else{
                    Toast.makeText(activity_login.this, "Password is not correct" , Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        //Email existence check
        JsonArrayRequest emailRequest = new JsonArrayRequest(Request.Method.GET,emailUrl,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int info = 0;
                JSONObject o = null;
                try {
                    o = response.getJSONObject(0);
                    info = parseInt((String) o.get("COUNT(email)"));
                    //System.out.println(info + " is the value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(info == 0){
                    Toast.makeText(activity_login.this, "This email is not registered" , Toast.LENGTH_LONG).show();

                }else{
                    requestQueue.add(passwordRequest);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_login.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(emailRequest);

    }

    public void onDoNotHaveAccount_Clicked(View caller) {

        Intent intent = new Intent(this, activity_register.class);
        startActivity(intent);
    }

}