package be.kuleuven.spot.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import be.kuleuven.spot.objects.Post;
import be.kuleuven.spot.R;
import be.kuleuven.spot.objects.calculateDistance;

public class MapFragment extends Fragment {
    double latitude, longitude;
    private static final String checkMessages = "https://studev.groept.be/api/a21pt215/message_board";
    HashMap<Integer, Post> postMap = new HashMap<>();
    DialogFragment popUp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        //Initialize map
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        //Async map
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            //Map loaded
            latitude = requireActivity().getIntent().getExtras().getDouble("latitude");
            longitude = requireActivity().getIntent().getExtras().getDouble("longitude");
            LatLng here = new LatLng(latitude,longitude);
            googleMap.addMarker(new MarkerOptions().position(here).title("You are here").zIndex(999));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(here,12));
            dbpost(googleMap);
            googleMap.setOnMarkerClickListener(marker -> {
                if(Objects.equals(marker.getTitle(), "You are here")){
                    return false;
                }
                deleteFragment();
                Post post;
                post = postMap.get(Integer.parseInt(marker.getTitle()));
                assert post != null;
                popUp = new DialogFragment(post.getId(),post.getUsername(),post.getContent()
                        ,post.getDate(),post.getDistance(),post.getImage());
                showFragment(popUp);
                return true;
            });
            googleMap.setOnMapClickListener(latLng -> deleteFragment());
        });
        return view;
    }

    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.dialog_container,fragment);
        fragmentTransaction.commit();
    }

    private void deleteFragment(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(popUp != null){
            fragmentTransaction.remove(popUp);
            fragmentTransaction.commit();
        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void dbpost(GoogleMap googleMap){
        RequestQueue requestQueue = Volley.newRequestQueue(this.requireActivity());
        JsonArrayRequest messageRequest = new JsonArrayRequest(Request.Method.GET,checkMessages,null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd 'at' hh a zzz");
                    Date date = new Date(Long.parseLong(obj.getString("time")) * 1000);
                    LatLng location = new LatLng(obj.getDouble("latitude"),obj.getDouble("longitude"));
                    Post p = new Post(obj.getInt("id"), obj.getString("username"), obj.getString("content"),
                            format.format(date),
                            calculateDistance.calculate(obj.getDouble("latitude"), latitude,
                                    obj.getDouble("longitude"), longitude), obj.getString("image"));
                    if(p.getDistance() <= 10){
                        postMap.put(p.getId(),p);
                        googleMap.addMarker(new MarkerOptions().position(location)
                                .title(obj.getString("id"))
                                .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_speech_bubble)));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> Toast.makeText(getActivity(), "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(messageRequest);
    }
}