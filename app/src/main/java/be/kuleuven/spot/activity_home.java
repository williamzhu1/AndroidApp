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

    @NonNull ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();



        if(bundle.getBoolean("openProfile")){
            ProfileFragment profile = new ProfileFragment();
            profile.setArguments(bundle);
            replaceFragment(profile);
        }else{
            replaceFragment(new HomeFragment());
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