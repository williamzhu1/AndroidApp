package be.kuleuven.spot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        tv_username = v.findViewById(R.id.dialog_username);
        tv_distance = v.findViewById(R.id.dialog_distance);
        tv_content = v.findViewById(R.id.dialog_content);
        tv_date = v.findViewById(R.id.dialog_date);
        iv_avatar = v.findViewById(R.id.dialogProfile);
        tv_username.setText(username);
        tv_distance.setText(String.valueOf(distance));
        tv_content.setText(content);
        tv_date.setText(date);
        byte[] imageBytes = Base64.decode(this.image, Base64.DEFAULT );
        Bitmap bitmap2 = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        iv_avatar.setImageBitmap(bitmap2);
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