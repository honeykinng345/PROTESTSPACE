package protestspacec.om.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import protestspacec.om.Model.Posts;
import protestspacec.om.PostDetailActivity;
import protestspacec.om.R;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Posts> posModelList;
    SharedPreferences preferences;

    String myUid;
    DatabaseReference mLikesRef, mPostRef;
    boolean mProcessLike = false;

    public MyPostAdapter(Context context, ArrayList<Posts> posModelList) {
        this.context = context;
        this.posModelList = posModelList;

        myUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mLikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        mPostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_posts, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        preferences = context.getSharedPreferences("MyPref", 0);
        String s1 = preferences.getString("AccountType", "");
        Posts model = posModelList.get(position);
        if (Objects.equals(s1, "User")) {

            holder.hire.setVisibility(View.GONE);
            holder.share.setVisibility(View.VISIBLE);

        } else {

            holder.hire.setVisibility(View.GONE);
            holder.share.setVisibility(View.GONE);

        }
        //
        // holder.time.setText(model.getTime());
        holder.description.setText(model.getPostDescription());
        holder.userName.setText(model.getName());
        String timeStamp = model.getTimeStamp();
        // timestamp to proper format
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String formatTime = DateFormat.format("dd/MM/yyyy", calendar).toString();
        holder.date.setText(formatTime);
        holder.likes.setText(model.getLikes() != null ? !model.getLikes().equals("0") ? model.getLikes() + " Likes" : null : null);
        holder.commencCount.setText(model.getComments() != null ? !model.getComments().equals("0") ? model.getComments() + " Comments" : null : null);

        holder.comment.setOnClickListener(v -> openPostDetail(model));
        holder.commencCount.setOnClickListener(v -> openPostDetail(model));

        holder.likeBtn.setOnClickListener(v -> {
            int likes = model.getLikes() == null ? 0 : Integer.parseInt(model.getLikes());
            mProcessLike = true;
            mLikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (mProcessLike){
                        if (dataSnapshot.child(model.getPostid()).hasChild(myUid)){
                            //already like so remove like
                            mPostRef.child(model.getPostid()).child("likes").setValue(""+(likes-1));
                            mLikesRef.child(model.getPostid()).child(myUid).removeValue();
                        }else {
                            //nnot liked yet , like it
                            mPostRef.child(model.getPostid()).child("likes").setValue(""+(likes+1));
                            mLikesRef.child(model.getPostid()).child(myUid).setValue("Liked");
                        }
                        mProcessLike = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });

        // holder.postImage.setImageResource(model.getImage());

        setLikes(holder, model.getPostid());

        if (model.getPostImage().equals("") || model.getPostImage() == null) {
            holder.postImage.setVisibility(View.GONE);
        } else {

            try {
                Picasso.get().load(model.getPostImage()).placeholder(R.drawable.android).into(holder.postImage);

            } catch (Exception e) {

                holder.postImage.setImageResource(R.drawable.android);

            }
        }
    }

    private void openPostDetail(Posts model) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra("postID", model.getPostid());
        intent.putExtra("likes", model.getLikes());
        intent.putExtra("comments", model.getComments());
        context.startActivity(intent);
    }

    private void setLikes(ViewHolder holder, String postid) {
        mLikesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(postid).hasChild(myUid)){
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked, 0, 0, 0);
                    holder.likeBtn.setText("Liked");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return posModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView date, time, description, userName, share, hire, likes, likeBtn, comment, commencCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            postImage = itemView.findViewById(R.id.postImage);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            description = itemView.findViewById(R.id.textDiscription);
            userName = itemView.findViewById(R.id.userName);
            share = itemView.findViewById(R.id.share);
            hire = itemView.findViewById(R.id.hire);
            likes = itemView.findViewById(R.id.likecount);
            likeBtn = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            commencCount = itemView.findViewById(R.id.commentcount);

        }
    }
}
