package be.kuleuven.spot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

        holder.tv_content.setText(postList.get(position).getContent());
        holder.tv_distance.setText(String.valueOf(postList.get(position).getDistance()));
        holder.tv_date.setText(String.valueOf(postList.get(position).getDate()));
        Glide.with(this.context).load("http://goo.gl/gEgYUd").into(holder.iv_avatar);
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
            iv_avatar = itemView.findViewById (R.id.iv_avatar);
            tv_username = itemView.findViewById(R.id.tv_username)
            tv_distance = itemView.findViewById(R.id.tv_distance);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}

