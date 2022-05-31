package be.kuleuven.spot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import be.kuleuven.spot.databinding.ActivityHomeBinding;

public class activity_home extends AppCompatActivity {

    Bundle bundle;
    public manageLocation manageLocation;


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


        if(bundle.getBoolean("openProfile")){
            replaceFragment(new ProfileFragment());
        } else{
            replaceFragment(new HomeFragment());
        }



        binding.bottomNavigationView.setOnItemSelectedListener(item ->{

            bundle.putDouble("latitude", manageLocation.getLatitude());
            bundle.putDouble("longitude",manageLocation.getLongitude());
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.map:
                    replaceFragment(new MapFragment());
                    break;
                case R.id.imageProfile:
                    replaceFragment(new ProfileFragment());
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

    public manageLocation getManageLocation(){
        return manageLocation;
    }


}