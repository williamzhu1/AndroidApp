package be.kuleuven.spot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapFragment extends Fragment {
    double latitude, longitude;
    private RequestQueue requestQueue;
    private static final String checkMessages = "https://studev.groept.be/api/a21pt215/message_board";
    List<post> postList = new ArrayList<>();
    private Marker marker;
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
        supportMapFragment.getMapAsync(googleMap -> {
            //Map loaded
            latitude = getActivity().getIntent().getExtras().getDouble("latitude");
            longitude = getActivity().getIntent().getExtras().getDouble("longitude");
            LatLng here = new LatLng(latitude,longitude);
            googleMap.addMarker(new MarkerOptions().position(here).title("You are here").zIndex(999));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(here,12));
            dbpost(googleMap);
            googleMap.setOnMarkerClickListener(marker -> {
                if(marker.getTitle().equals("You are here")){
                    return false;
                }
                deleteFragment();
                for(int i=0; i<postList.size(); i++){
                    if(postList.get(i).getId() == Integer.parseInt(marker.getTitle())){
                        popUp = new DialogFragment(postList.get(i).getId(),postList.get(i).getUsername(),postList.get(i).getContent()
                                ,postList.get(i).getDate(),postList.get(i).getDistance(),postList.get(i).getImage());
                        showFragment(popUp);
                    }
                }
                return true;
            });
            googleMap.setOnMapClickListener(latLng -> deleteFragment());
        });
        return view;
    }

    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.dialog_container,fragment);
        fragmentTransaction.commit();
    }

    private void deleteFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(popUp != null){
            fragmentTransaction.remove(popUp);
            fragmentTransaction.commit();
        }
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void dbpost(GoogleMap googleMap){
        requestQueue = Volley.newRequestQueue(this.getActivity());
        activity_home activity = (activity_home) getActivity();
        JsonArrayRequest messageRequest = new JsonArrayRequest(Request.Method.GET,checkMessages,null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd 'at' hh a zzz");
                    Date date = new Date(Long.parseLong(obj.getString("time")) * 1000);
                    LatLng location = new LatLng(obj.getDouble("latitude"),obj.getDouble("longitude"));
                    post p = new post(obj.getInt("id"), obj.getString("username"), obj.getString("content"),
                            format.format(date),
                            calculateDistance(obj.getDouble("latitude"), latitude,
                                    obj.getDouble("longitude"), longitude), obj.getString("image"));
                    if(p.getDistance() <= 10){
                        postList.add(p);
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

    private double calculateDistance(double lat1, double lat2, double lon1, double lon2){
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat/2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon/2), 2);
        double c = (2 * Math.asin(Math.sqrt(a))) * 6371;
        BigDecimal bd = new BigDecimal(c).setScale(2,RoundingMode.HALF_UP);
        return (bd.doubleValue());
    }
}