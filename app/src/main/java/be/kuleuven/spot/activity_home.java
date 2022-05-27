package be.kuleuven.spot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import be.kuleuven.spot.databinding.ActivityHomeBinding;
import be.kuleuven.spot.databinding.ActivityMainBinding;

public class activity_home extends AppCompatActivity {

    private LocationManager locationManager;
    private String longitude;
    private String latitude;

    @NonNull ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        Bundle bundle = getIntent().getExtras();

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(activity_home.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity_home.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity_home.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());
                Toast.makeText(activity_home.this, latitude, Toast.LENGTH_LONG).show();
                bundle.putString("longitude",longitude);
                bundle.putString("latitude",latitude);
            }
        });

        if(bundle.getBoolean("openProfile")){
            ProfileFragment profile = new ProfileFragment();
            profile.setArguments(bundle);
            replaceFragment(new ProfileFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            switch (item.getItemId()){
                case R.id.home:

                    HomeFragment home = new HomeFragment();
                    home.setArguments(bundle);
                    replaceFragment(home);
                    break;
                case R.id.map:
                    replaceFragment(new MapFragment());
                    break;
                case R.id.profile:
                    ProfileFragment profile = new ProfileFragment();
                    profile.setArguments(bundle);
                    replaceFragment(profile);
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}