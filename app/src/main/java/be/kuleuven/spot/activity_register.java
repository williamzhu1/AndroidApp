package be.kuleuven.spot;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class activity_register extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button btnRegister;

    private TextView AlreadyHaveAccount;
    private RequestQueue requestQueue;
    private static final String insertUrl = "https://studev.groept.be/api/a21pt215/Insert/";
    private static final String checkUsernameExistUrl = "https://studev.groept.be/api/a21pt215/ifUsernameExist/";
    private static final String checkEmailExistUrl = "https://studev.groept.be/api/a21pt215/ifEmailExist/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AlreadyHaveAccount = (TextView) findViewById(R.id.AlreadyHaveAccount);
        inputUsername = (EditText) findViewById(R.id.inputEmail_login);
        inputEmail = (EditText) findViewById(R.id.inputPassword_login);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

    }

    public void onAlreadyHaveAccount_Clicked(View caller) {

        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
    }

    public void onRegister_Clicked(View caller) {

        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();
        //Toast.makeText(activity_register.this, password + ", " + confirmPassword , Toast.LENGTH_LONG).show()

        if(username == null || email == null || password == null || confirmPassword == null){
            Toast.makeText(activity_register.this, "Please fill all", Toast.LENGTH_LONG).show();
        }

        if (password.length() < 8) {
            Toast.makeText(activity_register.this, "Password has to be longer the 8 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(password.equals(confirmPassword))) {
            Toast.makeText(activity_register.this, "Password does not match. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
        //Toast.makeText(activity_register.this, username, Toast.LENGTH_LONG).show();

        //This method checks if the email or username is already existing in the database.
        //In case it is not existing, it passes the parameters to the database with the method passRegistrationData
        checkIfAlreadyExist(username, email, password, confirmPassword);
    }

    public void passRegistrationData(String username, String email, String password, String confirmPassword){
        //Log.d("Database","Creating request");
        String requestURL = insertUrl + username + "/" + email + "/" + password;
        //Log.d("Database","Creating request");

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                    }
                },

                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity_register.this, "Unable to register", Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(submitRequest);
        Intent intent = new Intent(this, activity_post.class);
        startActivity(intent);
    }


    public void checkIfAlreadyExist(String username, String email, String password, String confirmPassword) {

        requestQueue = Volley.newRequestQueue(this);
        String usernameUrl = checkUsernameExistUrl + username;
        String emailUrl = checkEmailExistUrl + email;

        //Username existence check
        JsonArrayRequest usernameRequest = new JsonArrayRequest(Request.Method.GET,usernameUrl,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int info = 0;

                    JSONObject o = null;
                    try {
                        o = response.getJSONObject(0);
                        info = parseInt((String) o.get("COUNT(username)"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                if(info>0){
                    Toast.makeText(activity_register.this, "This username is already used", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    passRegistrationData(username,email,password,confirmPassword);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_register.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });


        //Email Existence check
        JsonArrayRequest emailRequest = new JsonArrayRequest(Request.Method.GET,emailUrl,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int info = 0;
                JSONObject o = null;
                try {
                    o = response.getJSONObject(0);
                    info = parseInt((String) o.get("COUNT(email)"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(activity_register.this, info , Toast.LENGTH_LONG).show();
                if(info>0){
                    Toast.makeText(activity_register.this, "This email is already registered" , Toast.LENGTH_LONG).show();
                    return;
                }else{
                    requestQueue.add(usernameRequest);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_register.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(emailRequest);

    }

}