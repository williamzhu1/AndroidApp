package be.kuleuven.spot.activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import be.kuleuven.spot.R;
import be.kuleuven.spot.objects.manageLocation;

public class MainActivity extends AppCompatActivity {

    private Double longitude = 0.0;
    private Double latitude = 0.0;
    private final Bundle bundle = new Bundle();
    be.kuleuven.spot.objects.manageLocation manageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnLogin);
        findViewById(R.id.btnRegister);


        manageLocation = new manageLocation(this,longitude,latitude);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onBtnLogin_Clicked(View caller) {
        Intent intent = new Intent(this, activity_login.class);
        latitude = manageLocation.getLatitude();
        longitude = manageLocation.getLongitude();
        System.out.println("latitude = " + latitude);
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onBtnRegister_Clicked(View caller) {
        Intent intent = new Intent(this, activity_register.class);
        latitude = manageLocation.getLatitude();
        longitude = manageLocation.getLongitude();
        System.out.println("latitude = " + latitude);
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}