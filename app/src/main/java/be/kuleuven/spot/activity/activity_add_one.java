package be.kuleuven.spot.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import be.kuleuven.spot.R;
import be.kuleuven.spot.objects.manageLocation;

public class activity_add_one extends AppCompatActivity {

    Button btn_publish, btn_cancel;

    String username;
    EditText content;
    private final static String insertUrl = "https://studev.groept.be/api/a21pt215/insertMessage/";
    Bundle bundle;
    private be.kuleuven.spot.objects.manageLocation manageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one);

        btn_publish = findViewById(R.id.btn_publish);
        btn_cancel = findViewById(R.id.btn_cancel);
        content = (EditText) findViewById(R.id.et_content);
        bundle = getIntent().getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");

        manageLocation = new manageLocation(this,longitude,latitude);


        btn_publish.setOnClickListener(view -> {
            String contentTyped = content.getText().toString();
            Log.d("test", contentTyped);
            try {
                publish(contentTyped);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pass();
        });
        btn_cancel.setOnClickListener(view -> pass());
    }
    private void pass(){
        Intent intent = new Intent(activity_add_one.this, activity_home.class );
        Bundle bundle = getIntent().getExtras();
        bundle.putBoolean("openProfile", false);
        bundle.putDouble("latitude",manageLocation.getLatitude());
        bundle.putDouble("longitude",manageLocation.getLongitude());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void publish(String contentTyped) throws UnsupportedEncodingException {
        long tsLong = System.currentTimeMillis()/1000;
        String ts = Long.toString(tsLong);
        int time = Integer.parseInt(ts);
        username = getIntent().getExtras().getString("username");
        if(contentTyped.length() >= 255){
            Toast.makeText(this, "Type at most 255 characters" , Toast.LENGTH_LONG).show();
        }
        if(contentTyped.length() == 0){
            Toast.makeText(this, "Please type something" , Toast.LENGTH_LONG).show();
        }
        String requestURL = insertUrl + username + "/" + URLEncoder.encode(contentTyped, "UTF-8") + "/" +
                time + "/" + manageLocation.getLatitude() + "/" + manageLocation.getLongitude();
        Log.d("requestURL", requestURL);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL,
                response -> {
                },

                error -> {
                }
        );
        requestQueue.add(submitRequest);
    }
}