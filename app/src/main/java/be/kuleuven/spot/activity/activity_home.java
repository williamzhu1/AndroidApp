package be.kuleuven.spot.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.concurrent.atomic.AtomicInteger;

import be.kuleuven.spot.fragment.HomeFragment;
import be.kuleuven.spot.fragment.MapFragment;
import be.kuleuven.spot.fragment.ProfileFragment;
import be.kuleuven.spot.R;
import be.kuleuven.spot.databinding.ActivityHomeBinding;
import be.kuleuven.spot.objects.manageLocation;

public class activity_home extends AppCompatActivity {

    Bundle bundle;
    public be.kuleuven.spot.objects.manageLocation manageLocation;


    ActivityHomeBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bundle = getIntent().getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        manageLocation = new manageLocation(this,longitude,latitude);
        AtomicInteger currentId = new AtomicInteger();


        if(bundle.getBoolean("openProfile")){
            replaceFragment(new ProfileFragment());
        } else{
            replaceFragment(new HomeFragment());
        }



        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            bundle.putDouble("latitude", manageLocation.getLatitude());
            bundle.putDouble("longitude",manageLocation.getLongitude());
            if(item.getItemId() == R.id.home){
                replaceFragment(new HomeFragment());
                currentId.set(R.id.home);
            }
            if(item.getItemId() == R.id.map && currentId.get() != R.id.map){
                replaceFragment(new MapFragment());
                currentId.set(R.id.map);
            }
            if(item.getItemId() == R.id.imageProfile && currentId.get() != R.id.imageProfile){
                replaceFragment(new ProfileFragment());
                currentId.set(R.id.imageProfile);
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

    private boolean getFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        return fragmentTransaction.equals(fragment);
    }


    public manageLocation getManageLocation(){
        return manageLocation;
    }


}