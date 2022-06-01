package be.kuleuven.spot.activity;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.spot.R;
import be.kuleuven.spot.objects.manageLocation;

public class activity_login extends AppCompatActivity {

    private EditText inputEmail_login;
    private EditText inputPassword_login;

    private RequestQueue requestQueue;
    private static final String checkEmailExistUrl = "https://studev.groept.be/api/a21pt215/ifEmailExist/";
    private static final String checkPasswordUrl = "https://studev.groept.be/api/a21pt215/passwordWithEmail/";
    private static final String getUsernameUrl = "https://studev.groept.be/api/a21pt215/getUsername/";
    Bundle bundle;
    private be.kuleuven.spot.objects.manageLocation manageLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail_login = (EditText) findViewById(R.id.inputEmail_login);
        inputPassword_login = (EditText) findViewById(R.id.inputPassword_login);
        bundle = getIntent().getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        manageLocation = new manageLocation(this,longitude,latitude);
    }

    public void onLogin_Clicked(View caller) {

        String email = inputEmail_login.getText().toString();
        String password = inputPassword_login.getText().toString();
        checkIfEmailPassword(email,password);
    }

    public void checkIfEmailPassword(String email, String password){
        requestQueue = Volley.newRequestQueue(this);
        String emailUrl = checkEmailExistUrl + email;
        String passwordUrl = checkPasswordUrl + email;
        String usernameUrl = getUsernameUrl + email;

        JsonArrayRequest getUsernameRequest = new JsonArrayRequest(Request.Method.GET,usernameUrl,null, response -> {
            String info = "";
            JSONObject o;
            try {
                o = response.getJSONObject(0);
                info = (String)o.get("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String username = info;
            Intent intent = new Intent(activity_login.this, activity_home.class);
            Bundle bundle = getIntent().getExtras();
            bundle.putString("email",email);
            bundle.putString("username", username);
            bundle.putString("password", password);
            bundle.putDouble("latitude", manageLocation.getLatitude());
            bundle.putDouble("longitude", manageLocation.getLongitude());
            intent.putExtras(bundle);
            startActivity(intent);

        }, error -> Toast.makeText(activity_login.this, "Username not found", Toast.LENGTH_LONG).show());

        JsonArrayRequest passwordRequest = new JsonArrayRequest(Request.Method.GET,passwordUrl,null, response -> {
            String info = "";
            JSONObject o;
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

        }, error -> Toast.makeText(activity_login.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        //Email existence check
        JsonArrayRequest emailRequest = new JsonArrayRequest(Request.Method.GET,emailUrl,null, response -> {

            int info = 0;
            JSONObject o;
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
        }, error -> Toast.makeText(activity_login.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(emailRequest);

    }

    public void onDoNotHaveAccount_Clicked(View caller) {
        Intent intent = new Intent(this, activity_register.class);
        bundle.putDouble("latitude", manageLocation.getLatitude());
        bundle.putDouble("longitude",manageLocation.getLongitude());
        intent.putExtras(bundle);
        startActivity(intent);
    }

}