package protestspacec.om;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import protestspacec.om.Model.Posts;
import protestspacec.om.adapters.MyPostAdapter;

public class ProfileFragment extends Fragment {


    @BindView(R.id.mypostreceyclerView)
    RecyclerView mypostreceyclerView;
    @BindView(R.id.UserName)
    TextView UserName;
    @BindView(R.id.UserEmail)
    TextView UserEmail;
    @BindView(R.id.UserPhone)
    TextView UserPhone;
    private ArrayList<Posts> posModelList;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this, view);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Helper.message(getContext(),"DOne");
        LoadMyInfo();

    }

    private void LoadMyInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                   // String profileImage = "" + ds.child("profileImage").getValue();
                    UserName.setText(name);
                    UserEmail.setText(email);
                    UserPhone.setText(phone);
                 /*   try {
                        Picasso.get().load(profileImage).placeholder(R.drawable.ic_store_gray).into(personIV);
                    } catch (Exception e) {
                        personIV.setImageResource(R.drawable.ic_store_gray);
                    }*/

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadMyPost();
    }

    private void LoadMyPost() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mypostreceyclerView.setLayoutManager(linearLayoutManager);
        posModelList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        Posts modelpost = ds.getValue(Posts.class);
                        posModelList.add(modelpost);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                MyPostAdapter myPostAdapter = new MyPostAdapter(getActivity(), posModelList, true);
                mypostreceyclerView.setAdapter(myPostAdapter);
                myPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
