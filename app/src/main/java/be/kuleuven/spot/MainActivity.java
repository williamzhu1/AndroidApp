package be.kuleuven.spot;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
    }

    public void onBtnLogin_Clicked(View caller) {
        Intent intent = new Intent(this, activity_login.class);
        startActivity(intent);
    }

    public void onBtnRegister_Clicked(View caller) {
        Intent intent = new Intent(this, activity_register.class);
        startActivity(intent);
    }
}