package be.kuleuven.spot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import java.lang.*;

public class activity_changeProfile extends AppCompatActivity {

    private ImageView image;
    private Button setNewProfile;
    private RequestQueue requestQueue;
    private static String POST_URL = "https://studev.groept.be/api/a21pt215/insertImage/";
    private static final String GET_IMAGE_URL = "";
    private int PICK_IMAGE_REQUEST = 111;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        image = (ImageView)findViewById(R.id.imageNewProfile);
        setNewProfile = (Button)findViewById(R.id.setNewProfile);
        requestQueue = Volley.newRequestQueue(this);
        setNewProfile.setEnabled(false);
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

        //Execute the Volley call. Note that we are not appending the image string to the URL, that happens further below
        StringRequest submitRequest = new StringRequest (Request.Method.POST, POST_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Turn the progress widget off
                progressDialog.dismiss();
                Toast.makeText(activity_changeProfile.this, "Post request executed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_changeProfile.this, activity_home.class);
                Bundle bundle = new Bundle();
                bundle = getIntent().getExtras();
                bundle.putBoolean("openProfile",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity_changeProfile.this, "Post request failed", Toast.LENGTH_LONG).show();
            }
        }) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String username = getIntent().getExtras().getString("username");
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("image", imageString);

                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

}