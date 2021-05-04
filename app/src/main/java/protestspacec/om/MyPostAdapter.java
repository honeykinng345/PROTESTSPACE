package protestspacec.om;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import protestspacec.om.Model.Posts;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Posts> posModelList;
    SharedPreferences preferences ;

    public MyPostAdapter(Context context, ArrayList<Posts> posModelList) {
        this.context = context;
        this.posModelList = posModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_posts, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        preferences=  context.getSharedPreferences("MyPref",0);
        String s1 = preferences.getString("AccountType","");
        Posts model = posModelList.get(position);
        if (s1.equals("User")){

            holder.hire.setVisibility(View.GONE);
            holder.share.setVisibility(View.VISIBLE);

        }else{

            holder.hire.setVisibility(View.GONE);
            holder.share.setVisibility(View.GONE);

        }
       //
       // holder.time.setText(model.getTime());
        holder.description.setText(model.getPostDescription());
        holder.userName.setText(model.getName());
        String timeStamp= model.getTimeStamp();
        // timestamp to proper format
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String formatTime = DateFormat.format("dd/MM/yyyy",calendar).toString();
        holder.date.setText(formatTime);

        // holder.postImage.setImageResource(model.getImage());

        if (model.getPostImage().equals("")|| model.getPostImage().equals(null)){
    holder.postImage.setVisibility(View.GONE);
}else{

    try {
        Picasso.get().load(model.getPostImage()).placeholder(R.drawable.android).into(holder.postImage);

    }catch (Exception e){

        holder.postImage.setImageResource(R.drawable.android);

    }
}
    }

    @Override
    public int getItemCount() {
        return posModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView date,time,description,userName,share,hire;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            postImage =  itemView.findViewById(R.id.postImage);
            date =  itemView.findViewById(R.id.date);
            time =  itemView.findViewById(R.id.time);
            description =  itemView.findViewById(R.id.textDiscription);
            userName =  itemView.findViewById(R.id.userName);
            share =  itemView.findViewById(R.id.share);
            hire =  itemView.findViewById(R.id.hire);

        }
    }
}
