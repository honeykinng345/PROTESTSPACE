package protestspacec.om;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import protestspacec.om.Model.Posts;
import protestspacec.om.adapters.MyPostAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView courseRV;

    // Arraylist for storing data
    private ArrayList<Posts> postsModelArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        courseRV = view.findViewById(R.id.idRVCourse);


        loadData();
        return view;
    }

    private void loadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        courseRV.setLayoutManager(linearLayoutManager);
        postsModelArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postsModelArrayList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Posts model = ds.getValue(Posts.class);
                    postsModelArrayList.add(model);

                }
                //PostAdapter Adapter = new PostAdapter(getContext(),postsModelArrayList);
                MyPostAdapter Adapter = new MyPostAdapter(getContext(), postsModelArrayList);

                // below line is for setting a layout manager for our recycler view.

                courseRV.setAdapter(Adapter);
                Adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // we are initializing our adapter class and passing our arraylist to it.

    }

}
