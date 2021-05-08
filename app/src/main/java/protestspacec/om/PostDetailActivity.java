package protestspacec.om;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import protestspacec.om.Model.Comment;
import protestspacec.om.Model.Posts;
import protestspacec.om.adapters.CommentAdapter;

public class PostDetailActivity extends AppCompatActivity {

    @BindView(R.id.userImage)
    ImageView userImage;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.textDiscription)
    TextView textDiscription;
    @BindView(R.id.postImage)
    ImageView postImage;
    @BindView(R.id.likecount)
    TextView likecount;
    @BindView(R.id.commentcount)
    TextView commentcount;
    @BindView(R.id.relavtive2)
    LinearLayout relavtive2;
    @BindView(R.id.like)
    TextView like;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.share)
    TextView share;
    @BindView(R.id.hire)
    TextView hire;
    @BindView(R.id.cimg)
    CircularImageView cimg;
    @BindView(R.id.sendcommentbtn)
    ImageView sendcommentbtn;
    @BindView(R.id.comenttext)
    EditText comenttext;
    @BindView(R.id.rv)
    RecyclerView rv;

    ArrayList<Comment> commentArrayList;

    String postId;
    String myUid;
    String myUname;
    Uri myImg;
    DatabaseReference mLikesRef, mCommentRef, mPostRef;
    boolean mProcessLike = false;
    boolean mProcessComment = false;

    private int totalComments, totalLikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        myUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        myUname = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();
        myImg = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        mLikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        mCommentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        mPostRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postId = getIntent().getStringExtra("postID");
        totalLikes = getIntent().getStringExtra("likes") != null ? Integer.parseInt(getIntent().getStringExtra("likes")) : 0;
        totalComments = getIntent().getStringExtra("comments") != null ? Integer.parseInt(getIntent().getStringExtra("comments")) : 0;


        getPost();
        getComments();
        sendcommentbtn.setOnClickListener(v -> postComment());
        like.setOnClickListener(v -> postLike());

    }

    private void getComments() {
        if (rv.getLayoutManager() == null)
            rv.setLayoutManager(new LinearLayoutManager(this));
        if (rv.getAdapter() == null){
            commentArrayList = new ArrayList<>();
            rv.setAdapter(new CommentAdapter(commentArrayList));
        }
        mCommentRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                commentArrayList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Comment c = ds.getValue(Comment.class);
                    commentArrayList.add(c);
                }
                rv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postLike() {
        mProcessLike = true;
        mLikesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mProcessLike) {
                    if (dataSnapshot.child(postId).hasChild(myUid)) {
                        //already like so remove like
                        --totalLikes;
                        mPostRef.child(postId).child("likes").setValue("" + totalLikes);
                        mLikesRef.child(postId).child(myUid).removeValue();
                        like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24, 0, 0, 0);
                        like.setText("Like");

                    } else {
                        //nnot liked yet , like it
                        ++totalLikes;
                        mPostRef.child(postId).child("likes").setValue("" + totalLikes);
                        mLikesRef.child(postId).child(myUid).setValue("Liked");
                        like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked, 0, 0, 0);
                        like.setText("Liked");
                    }
                    likecount.setText("" + totalLikes + " Likes");
                    mProcessLike = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void postComment() {
        String commentTxt = comenttext.getText().toString().trim();
        if (!TextUtils.isEmpty(commentTxt) && !mProcessComment) {
            mProcessComment = true;

            Comment c = new Comment(String.valueOf(System.currentTimeMillis()), commentTxt, myUid, myUname,
                    myImg == null ? null : myImg.toString());

            mCommentRef.child(postId).child(c.getcId()).setValue(c).addOnSuccessListener(aVoid -> {
                ++totalComments;
                mPostRef.child(postId).child("comments").setValue("" + totalComments);
                comenttext.setText("");
                commentcount.setText("" + totalComments + " Comments");
                mProcessComment = false;
            }).addOnFailureListener(e -> mProcessComment = false);
        }
    }

    private void getPost() {

        Query query = mPostRef.child(postId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Posts model = dataSnapshot.getValue(Posts.class);
                    updateUI(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(Posts model) {
        SharedPreferences preferences = getSharedPreferences("MyPref", 0);
        String s1 = preferences.getString("AccountType", "");
        if (Objects.equals(s1, "User")) {

            hire.setVisibility(View.GONE);
            share.setVisibility(View.VISIBLE);

        } else {

            hire.setVisibility(View.GONE);
            share.setVisibility(View.GONE);

        }

        if (myImg != null) {
            try {
                Picasso.get().load(myImg).into(cimg);
            } catch (Exception e) {
                e.printStackTrace();
                Picasso.get().load(R.drawable.personicon).into(cimg);
            }
        }

        //
        // holder.time.setText(model.getTime());
        textDiscription.setText(model.getPostDescription());
        userName.setText(model.getName());
        String timeStamp = model.getTimeStamp();
        // timestamp to proper format
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String formatTime = DateFormat.format("dd/MM/yyyy", calendar).toString();
        date.setText(formatTime);
        likecount.setText(model.getLikes() != null ? !model.getLikes().equals("0") ? model.getLikes() + " Likes" : null : null);
        commentcount.setText(model.getComments() != null ? !model.getComments().equals("0") ? model.getComments() + " Comments" : null : null);

        setLikes(model.getPostid());

        if (model.getPostImage().equals("") || model.getPostImage() == null) {
            postImage.setVisibility(View.GONE);
        } else {

            try {
                Picasso.get().load(model.getPostImage()).placeholder(R.drawable.android).into(postImage);

            } catch (Exception e) {

                postImage.setImageResource(R.drawable.android);

            }
        }
    }

    private void setLikes(String postid) {
        mLikesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).hasChild(myUid)) {
                    like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.liked, 0, 0, 0);
                    like.setText("Liked");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}