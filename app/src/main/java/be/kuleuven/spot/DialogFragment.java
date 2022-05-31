package be.kuleuven.spot;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class DialogFragment extends Fragment {
    private int id;
    private String username;
    private String content;
    private String date;
    private double distance;
    private String image;
    ImageView iv_avatar;
    TextView tv_username;
    TextView tv_distance;
    TextView tv_content;
    TextView tv_date;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.fragment_map);
        View v = inflater.inflate(R.layout.fragment_dialog,container,false);
        return v;
    }
    public DialogFragment(int id, String username, String content, String date, double distance, String image) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.date = date;
        this.distance = distance;
        this.image = image;
    }
}