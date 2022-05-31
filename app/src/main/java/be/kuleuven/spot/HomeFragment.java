package be.kuleuven.spot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<post> postList = new ArrayList<post>();
    Button btn_addOne;
    private static final String checkMessages = "https://studev.groept.be/api/a21pt215/message_board";
    double longitude, latitude;
    String username;

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        dbpost();

        recyclerView = (RecyclerView) v.findViewById(R.id.lv_recycle);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Log.d("ListSize", String.valueOf(postList.size()));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_addOne =(Button) getView().findViewById(R.id.btn_add_one);

        btn_addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), add_one.class);
                Bundle bundle = getActivity().getIntent().getExtras();
                bundle.putDouble("latitude",latitude);
                bundle.putDouble("longitude",longitude);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void dbpost(){
        requestQueue = Volley.newRequestQueue(this.getActivity());
        activity_home activity = (activity_home) getActivity();
        JsonArrayRequest messageRequest = new JsonArrayRequest(Request.Method.GET,checkMessages,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject o = null;
                latitude = getActivity().getIntent().getExtras().getDouble("latitude");
                longitude = getActivity().getIntent().getExtras().getDouble("longitude");
                Log.d("latitude", String.valueOf(latitude));
                try {
                    o = response.getJSONObject(0);
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        Log.d("tag",o.toString(4));
                        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd 'at' hh a zzz");
                        Date date = new Date(Long.parseLong(obj.getString("time")) * 1000);
                        Log.d("date", String.valueOf(date));
                        post p = new post(obj.getInt("id"), obj.getString("username"), obj.getString("content"),
                                format.format(date),
                                calculateDistance(obj.getDouble("latitude"), latitude,
                                        obj.getDouble("longitude"), longitude), null);
                        if(p.getDistance() <= 10){
                            postList.add(p);
                        }
                    }
                    mAdapter = new RecycleViewAdapter(postList, getActivity());
                    recyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

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