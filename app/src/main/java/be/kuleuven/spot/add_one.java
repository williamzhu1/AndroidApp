package be.kuleuven.spot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class add_one extends AppCompatActivity {

    Button btn_publish, btn_cancel;
    double longitude, latitude;
    private RequestQueue requestQueue;
    String username;
    EditText content;
    private final static String insertUrl = "https://studev.groept.be/api/a21pt215/insertMessage/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one);

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");

        btn_publish = findViewById(R.id.btn_publish);
        btn_cancel = findViewById(R.id.btn_cancel);
        content = (EditText) findViewById(R.id.et_content);

        //username = getIntent().getExtras().getString("username");
        //Log.d("username",username);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = Double.valueOf(location.getLongitude());
                latitude = Double.valueOf(location.getLatitude());
            }
        });

        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentTyped = content.getText().toString();
                Log.d("test", contentTyped);
                try {
                    publish(contentTyped);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                pass();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass();
            }
        });
    }
    private void pass(){
        Intent intent = new Intent(add_one.this, activity_home.class );
        Bundle bundle = getIntent().getExtras();
        bundle.putBoolean("openProfile", false);
        bundle.putDouble("latitude",latitude);
        bundle.putDouble("longitude",longitude);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void publish(String contentTyped) throws UnsupportedEncodingException {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        int time = Integer.parseInt(ts);
        username = getIntent().getExtras().getString("username");
        String requestURL = insertUrl + username + "/" + URLEncoder.encode(contentTyped, "UTF-8") + "/" + time + "/" + latitude + "/" + longitude;
        Log.d("requestURL", requestURL);
        requestQueue = Volley.newRequestQueue(this);

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                    }
                },

                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(submitRequest);
    }
}