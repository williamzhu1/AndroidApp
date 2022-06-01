package be.kuleuven.spot.objects;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class manageLocation {
    private Double lon;
    private Double lat;

    public manageLocation(Activity activity, Double longitude, Double latitude){
        lon = longitude;
        lat = latitude;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, location -> {
            lon = location.getLongitude();
            lat = location.getLatitude();
        });
    }

    public Double getLongitude() {
        System.out.println("longitude" + lon);
        return lon;
    }

    public Double getLatitude() {
        Log.d("latitude", String.valueOf(lat));
        return lat;
    }
}