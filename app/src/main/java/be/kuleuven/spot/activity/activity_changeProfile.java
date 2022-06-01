package be.kuleuven.spot.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import java.lang.*;

import be.kuleuven.spot.R;
import be.kuleuven.spot.objects.manageLocation;

public class activity_changeProfile extends AppCompatActivity {

    private ImageView image;
    private Button setNewProfile;
    private RequestQueue requestQueue;
    private static final String POST_URL = "https://studev.groept.be/api/a21pt215/insertImage/";
    private final int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    Bundle bundle;
    public be.kuleuven.spot.objects.manageLocation manageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        image = (ImageView)findViewById(R.id.imageNewProfile);
        setNewProfile = (Button)findViewById(R.id.setNewProfile);
        requestQueue = Volley.newRequestQueue(this);
        setNewProfile.setEnabled(false);
        bundle = getIntent().getExtras();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        manageLocation = new manageLocation(this,longitude,latitude);
    }
    public void onBtnPickClicked(View caller)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        //this line will start the new activity and will automatically run the callback method below when the user has picked an image
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Rescale the bitmap to 400px wide (avoid storing large images!)

                bitmap = centerCrop(bitmap);
                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                //Setting image to ImageView
                image.setImageBitmap(bitmap);
                setNewProfile.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static Bitmap centerCrop(Bitmap srcBmp) {
        Bitmap dstBmp;//from  w  w  w .  j  a  v  a2s.  c  o  m
        if (srcBmp.getWidth() >= srcBmp.getHeight()) {

            dstBmp = Bitmap.createBitmap(srcBmp, srcBmp.getWidth() / 2
                            - srcBmp.getHeight() / 2, 0, srcBmp.getHeight(),
                    srcBmp.getHeight());

        } else {

            dstBmp = Bitmap.createBitmap(srcBmp, 0, srcBmp.getHeight() / 2
                            - srcBmp.getWidth() / 2, srcBmp.getWidth(),
                    srcBmp.getWidth());
        }
        return dstBmp;
    }

    public void onBtnSetNewProfile(View caller)
    {
        //Start an animating progress widget
        progressDialog = new ProgressDialog(activity_changeProfile.this);
        progressDialog.setMessage("Uploading, please wait...");
        progressDialog.show();

        //convert image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        System.out.println("success" + imageString);
        //POST_URL = POST_URL + getIntent().getExtras().getString("username") + "/";

        StringRequest submitRequest = new StringRequest (Request.Method.POST, POST_URL, response -> {
            //Turn the progress widget off
            progressDialog.dismiss();
            Toast.makeText(activity_changeProfile.this, "Post request executed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity_changeProfile.this, activity_home.class);
            bundle = getIntent().getExtras();
            bundle.putBoolean("openProfile",true);
            bundle.putDouble("latitude", manageLocation.getLatitude());
            bundle.putDouble("longitude",manageLocation.getLongitude());
            intent.putExtras(bundle);
            startActivity(intent);
        }, error -> Toast.makeText(activity_changeProfile.this, "Post request failed", Toast.LENGTH_LONG).show()) {

            @Override
            protected Map<String, String> getParams() {

                String username = getIntent().getExtras().getString("username");
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("image", imageString);

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

}