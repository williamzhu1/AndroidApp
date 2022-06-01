package be.kuleuven.spot.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import be.kuleuven.spot.R;
import be.kuleuven.spot.objects.manageLocation;

public class activity_changePassword extends AppCompatActivity {

    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button changePassword;

    String currentPasswordInDatabase;
    String email;
    String username;

    String updatePasswordUrl = "https://studev.groept.be/api/a21pt215/updatePassword/";

    Bundle bundle;
    public be.kuleuven.spot.objects.manageLocation manageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmNewPassword);
        changePassword = (Button) findViewById(R.id.btn_changePassword);

        currentPasswordInDatabase = getIntent().getExtras().getString("password");
        email = getIntent().getExtras().getString("email");
        username = getIntent().getExtras().getString("username");
        bundle = getIntent().getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        manageLocation = new manageLocation(this,longitude,latitude);
    }

    public void onBtn_changePassword_clicked(View caller) {

        String currentPasswordTyped = currentPassword.getText().toString();
        String newPassword = this.newPassword.getText().toString();
        String confirmPassword = this.confirmPassword.getText().toString();

        String updatePasswordUrl_request = updatePasswordUrl + newPassword + "/" + email;

        //If the current password matches.
        if(currentPasswordTyped.equals(currentPasswordInDatabase)){
            //Toast.makeText(activity_changePassword.this, "Current password is correct", Toast.LENGTH_LONG).show();

            //if new password is longer than 8
            if (newPassword.length() < 8) {
                Toast.makeText(activity_changePassword.this, "Password has to be longer the 8 characters", Toast.LENGTH_LONG).show();
                return;
            }
            //if new password and the confirmation matches.
            if (newPassword.equals(confirmPassword)) {
                //updates the database with the new password
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest submitRequest = new StringRequest(Request.Method.GET, updatePasswordUrl_request,
                        response -> {
                            Toast.makeText(activity_changePassword.this, "Password has been updated", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity_changePassword.this, activity_home.class);
                            intent.putExtras(getIntent().getExtras());
                            bundle.putDouble("latitude", manageLocation.getLatitude());
                            bundle.putDouble("longitude",manageLocation.getLongitude());
                            intent.putExtra("openProfile", true);
                            startActivity(intent);
                        },

                        error -> Toast.makeText(activity_changePassword.this, "Unable to update password", Toast.LENGTH_LONG).show()
                );
                requestQueue.add(submitRequest);
            }else{
                Toast.makeText(activity_changePassword.this, "Password confirmation does not match. Please try again", Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(activity_changePassword.this, "Current password is wrong", Toast.LENGTH_LONG).show();
        }

    }

}
