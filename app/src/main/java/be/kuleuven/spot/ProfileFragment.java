package be.kuleuven.spot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private String username;
    private ImageView imageProfile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView pf_username_top = (TextView) requireView().findViewById(R.id.pf_username_top);
        TextView pf_username = (TextView) requireView().findViewById(R.id.pf_username);
        TextView pf_email = (TextView) requireView().findViewById(R.id.pf_email);
        Button btn_changePassword = (Button) requireView().findViewById(R.id.btn_changePassword);
        btn_changePassword.setOnClickListener(this);

        Button btn_EditProfile = (Button) requireView().findViewById(R.id.btn_EditProfile);
        btn_EditProfile.setOnClickListener(this::onBtnChangeProfileClicked);

        imageProfile = (ImageView)requireView().findViewById(R.id.imageProfile);

        String email = requireActivity().getIntent().getExtras().getString("email");
        username = requireActivity().getIntent().getExtras().getString("username");

        pf_email.setText(email);
        pf_username.setText(username);
        pf_username_top.setText(username);

        setProfilePhoto();
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(requireActivity(), activity_changePassword.class);
        intent.putExtras(requireActivity().getIntent().getExtras());
        startActivity(intent);
    }

    public void onBtnChangeProfileClicked(View view) {
        Intent intent = new Intent(requireActivity(), activity_changeProfile.class);
        intent.putExtras(requireActivity().getIntent().getExtras());
        startActivity(intent);
    }

    public void setProfilePhoto(){

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this.requireActivity());
        String requestUrl = "https://studev.groept.be/api/a21pt215/selectImage/" + username;

        JsonArrayRequest retrieveImageRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    try
                    {
                        //Check if the DB actually contains an image
                        if( response.length() > 0 ) {
                            JSONObject o = response.getJSONObject(0);

                            //converting base64 string to image
                            String b64String = o.getString("image");
                            byte[] imageBytes = Base64.decode( b64String, Base64.DEFAULT );
                            Bitmap bitmap2 = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );

                            //Link the bitmap to the ImageView, so it's visible on screen
                            imageProfile.setImageBitmap( bitmap2 );

                        }
                    }
                    catch( JSONException e )
                    {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getActivity(), "Unable to find a profile photo", Toast.LENGTH_LONG).show()
        );

        requestQueue.add(retrieveImageRequest);
    }

}