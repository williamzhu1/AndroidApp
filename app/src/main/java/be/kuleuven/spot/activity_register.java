package be.kuleuven.spot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class activity_register extends AppCompatActivity {

    private TextView inputUsername;
    private TextView inputEmail;
    private TextView inputPassword;
    private TextView confirmPassword;

    private TextView AlreadyHaveAccount;
    private RequestQueue requestQueue;
    private static final String insertUrl = "https://studev.groept.be/api/a21pt215/Insert/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AlreadyHaveAccount = (TextView)findViewById(R.id.AlreadyHaveAccount);
        
    }

    public void onAlreadyHaveAccount_Clicked(View caller) {

        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
    }

    public void onRegister_Clicked(View caller) {

        passRegistrationData("Shusaku","12345");
        Intent intent = new Intent(this, activity_post.class);
        startActivity(intent);
    }

    public void passRegistrationData(String username, String password){
        //Log.d("Database","Creating request");
        requestQueue = Volley.newRequestQueue(this);
        String requestURL = insertUrl + username + "/" + password;
        //Log.d("Database","Creating request");

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(activity_register.this, "Sccucess", Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity_register.this, "Unable to place the order", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(submitRequest);
    }
}