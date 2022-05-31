package be.kuleuven.spot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    List<Post> postList = new ArrayList<>();

    Button btn_addOne;
    private static final String checkMessages = "https://studev.groept.be/api/a21pt215/message_board";
    double latitude, longitude;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        dbpost();

        recyclerView = (RecyclerView) v.findViewById(R.id.lv_recycle);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Log.d("ListSize", String.valueOf(postList.size()));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manageLocation manageLocation = ((activity_home)requireActivity()).getManageLocation();
        latitude = manageLocation.getLatitude();
        longitude = manageLocation.getLongitude();
        btn_addOne = (Button) requireView().findViewById(R.id.btn_add_one);

        btn_addOne.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), add_one.class);
            Bundle bundle = requireActivity().getIntent().getExtras();
            bundle.putDouble("latitude",manageLocation.getLatitude());
            bundle.putDouble("longitude",manageLocation.getLongitude());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void dbpost(){
        RequestQueue requestQueue = Volley.newRequestQueue(this.requireActivity());
        JsonArrayRequest messageRequest = new JsonArrayRequest(Request.Method.GET,checkMessages,null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd 'at' hh a zzz");
                    Date date = new Date(Long.parseLong(obj.getString("time")) * 1000);
                    Log.d("date", String.valueOf(date));
                    Post p = new Post(obj.getInt("id"), obj.getString("username"), obj.getString("content"),
                            format.format(date),
                            calculateDistance.calculate(obj.getDouble("latitude"), latitude,
                                    obj.getDouble("longitude"), longitude), obj.getString("image"));
                    if(p.getDistance() <= 10){
                        postList.add(p);
                    }
                }
                mAdapter = new RecycleViewAdapter(postList, getActivity());
                recyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> Toast.makeText(getActivity(), "Unable to communicate with the server", Toast.LENGTH_LONG).show());

        requestQueue.add(messageRequest);
    }
}