package be.kuleuven.spot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    List<post> postList;
    Context context;

    public RecycleViewAdapter(List<post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_post, parent, false);
        MyViewHolder holder = new MyViewHolder((view));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        System.out.println("ahoahoaho" + postList.get(position).getImage());
        byte[] imageBytes = Base64.decode( postList.get(position).getImage(), Base64.DEFAULT );
        Bitmap bitmap2 = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        holder.iv_avatar.setImageBitmap(bitmap2);
        holder.tv_username.setText(postList.get(position).getUsername());
        holder.tv_content.setText(postList.get(position).getContent());
        holder.tv_distance.setText("Distance: "+ String.valueOf(postList.get(position).getDistance()) + "km");
        holder.tv_date.setText(String.valueOf(postList.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_avatar;
        TextView tv_username;
        TextView tv_distance;
        TextView tv_content;
        TextView tv_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById (R.id.dialogProfile);
            tv_username = itemView.findViewById(R.id.dialog_username);
            tv_distance = itemView.findViewById(R.id.dialog_distance);
            tv_content = itemView.findViewById(R.id.dialog_content);
            tv_date = itemView.findViewById(R.id.dialog_date);
        }
    }
}

