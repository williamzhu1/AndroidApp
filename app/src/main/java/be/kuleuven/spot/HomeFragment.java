package be.kuleuven.spot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RequestQueue requestQueue;
    private double latitude;
    private double longitude;

    FusedLocationProviderClient client;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<post> postList = new ArrayList<post>();
    Button btn_addOne;
    private static final String checkMessages = "https://studev.groept.be/api/a21pt215/message_board";

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        dbpost();
        recyclerView = (RecyclerView) v.findViewById(R.id.lv_recycle);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecycleViewAdapter(postList, this.getActivity());
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    private void dbpost() {
        requestQueue = Volley.newRequestQueue(this.getActivity());

        JsonArrayRequest messageRequest = new JsonArrayRequest(Request.Method.GET, checkMessages, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                latitude = getActivity().getIntent().getExtras().getDouble("latitude");
                longitude = getActivity().getIntent().getExtras().getDouble("longitude");
                System.out.println("latitude : " + latitude);
                System.out.println("longitude : " + longitude);

                JSONObject o = null;
                try {
                    o = response.getJSONObject(0);
                    for (int i = 0; i < o.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        post p = new post(obj.getInt("id"), obj.getString("username"), obj.getString("content"), obj.getInt("time"),
                                calculateDistance(obj.getDouble("latitude"), latitude,
                                        obj.getDouble("longitude"), longitude), null);
                        postList.add(p);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(messageRequest);
    }

    private double calculateDistance(double lat1, double lat2, double lon1, double lon2) {
        return lat1;
    }

}