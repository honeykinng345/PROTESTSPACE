package protestspacec.om.adapters;

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
import protestspacec.om.R;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {
    private Context context;
    private ArrayList<Posts> posModelList;
    SharedPreferences preferences ;



    public PostAdapter(Context context, ArrayList<Posts> posModelList) {
        this.context = context;
        this.posModelList = posModelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_posts, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
         preferences=  context.getSharedPreferences("MyPref",0);

      String s1 = preferences.getString("AccountType","");
        if (s1.equals("User")){

            holder.hire.setVisibility(View.GONE);
            holder.share.setVisibility(View.VISIBLE);

        }else{
            holder.share.setVisibility(View.GONE);
            holder.hire.setVisibility(View.VISIBLE);
        }
        Posts model = posModelList.get(position);
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
  /*      Posts model = posModelList.get(position);
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.description.setText(model.getDescription());
        holder.userName.setText(model.getUserName());
        holder.postImage.setImageResource(model.getImage());*/
    }

    @Override
    public int getItemCount() {
        return posModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView date,time,description,userName,share,hire,likes;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
           postImage =  itemView.findViewById(R.id.postImage);
          date =  itemView.findViewById(R.id.date);
           time =  itemView.findViewById(R.id.time);
           description =  itemView.findViewById(R.id.textDiscription);
           userName =  itemView.findViewById(R.id.userName);
           share =  itemView.findViewById(R.id.share);
           hire =  itemView.findViewById(R.id.hire);
            likes =  itemView.findViewById(R.id.likecount);

        }
    }
}
