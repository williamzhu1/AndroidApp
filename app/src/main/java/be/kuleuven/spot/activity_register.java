package be.kuleuven.spot;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class activity_register extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    private RequestQueue requestQueue;
    private static final String insertUrl = "https://studev.groept.be/api/a21pt215/Insert/";
    private static final String checkUsernameExistUrl = "https://studev.groept.be/api/a21pt215/ifUsernameExist/";
    private static final String checkEmailExistUrl = "https://studev.groept.be/api/a21pt215/ifEmailExist/";
    Bundle bundle;
    private manageLocation manageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputUsername = (EditText) findViewById(R.id.inputEmail_login);
        inputEmail = (EditText) findViewById(R.id.inputPassword_login);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputConfirmPassword = (EditText) findViewById(R.id.inputConfirmPassword);
        bundle = getIntent().getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        manageLocation = new manageLocation(this, longitude, latitude);
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

        if (!email.contains("@") || email.indexOf("@") == 0 || email.lastIndexOf(".") < email.indexOf("@") || email.endsWith(".") ||
            email.indexOf("@") != email.lastIndexOf("@") || email.lastIndexOf(".") == (email.indexOf("@") + 1)){
            Toast.makeText(activity_register.this, "Insert a valid email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 8) {
            Toast.makeText(activity_register.this, "Password has to be longer the 8 characters", Toast.LENGTH_LONG).show();
            return;
        }
        if (!(password.equals(confirmPassword))) {
            Toast.makeText(activity_register.this, "Password does not match. Please try again", Toast.LENGTH_LONG).show();
            return;
        }
        if(username.length() <= 0){
            Toast.makeText(activity_register.this, "Please fill username", Toast.LENGTH_LONG).show();
            return;
        }


        //This method checks if the email or username is already existing in the database.
        //In case it is not existing, it passes the parameters to the database with the method passRegistrationData
        checkIfAlreadyExist(username, email, password);
    }

    public void passRegistrationData(String username, String email, String password){
        //Log.d("Database","Creating request");
        String requestURL = insertUrl + username + "/" + email + "/" + password + "/" + username;
        //Log.d("Database","Creating request");

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,
                response -> {},
                error -> Toast.makeText(activity_register.this, "Unable to register", Toast.LENGTH_LONG).show()
        );
        requestQueue.add(submitRequest);
        Intent intent = new Intent(this, activity_login.class);
        bundle.putDouble("latitude", manageLocation.getLatitude());
        bundle.putDouble("longitude",manageLocation.getLongitude());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public void checkIfAlreadyExist(String username, String email, String password) {

        requestQueue = Volley.newRequestQueue(this);
        String usernameUrl = checkUsernameExistUrl + username;
        String emailUrl = checkEmailExistUrl + email;

        //Username existence check
        JsonArrayRequest usernameRequest = new JsonArrayRequest(Request.Method.GET,usernameUrl,null, response -> {
            int info = 0;
                JSONObject o;
                try {
                    o = response.getJSONObject(0);
                    info = parseInt((String) o.get("COUNT(username)"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            if(info>0){
                Toast.makeText(activity_register.this, "This username is already used", Toast.LENGTH_LONG).show();
            }else{
                passRegistrationData(username,email,password);
            }
        }, error -> Toast.makeText(activity_register.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());


        //Email Existence check
        JsonArrayRequest emailRequest = new JsonArrayRequest(Request.Method.GET,emailUrl,null, response -> {

            int info = 0;
            JSONObject o;
            try {
                o = response.getJSONObject(0);
                info = parseInt((String) o.get("COUNT(email)"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(activity_register.this, info , Toast.LENGTH_LONG).show();
            if(info>0){
                Toast.makeText(activity_register.this, "This email is already registered" , Toast.LENGTH_LONG).show();
            }else{
                requestQueue.add(usernameRequest);
            }
        }, error -> Toast.makeText(activity_register.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(emailRequest);

    }

}