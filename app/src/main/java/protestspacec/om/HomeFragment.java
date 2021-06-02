package protestspacec.om;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

import butterknife.BindView;
import protestspacec.om.Model.Posts;
import protestspacec.om.adapters.MyPostAdapter;

public class HomeFragment extends Fragment {


    EditText searchProductEt;
    private RecyclerView courseRV;
    MyPostAdapter Adapter;
    // Arraylist for storing data
    private ArrayList<Posts> postsModelArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        courseRV = view.findViewById(R.id.idRVCourse);
        searchProductEt = view.findViewById(R.id.searchProductEt);
        loadData();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });
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
                    try {
                        Posts model = ds.getValue(Posts.class);
                        postsModelArrayList.add(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //PostAdapter Adapter = new PostAdapter(getContext(),postsModelArrayList);
            Adapter = new MyPostAdapter(getActivity(), postsModelArrayList, false);
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
    private void filter(String text) {



        ArrayList<Posts> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Posts s : postsModelArrayList) {
            //if the existing elements contains the search input
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }

        }

        //calling a method of the adapter class and passing the filtered list
        Adapter.filterList(filterdNames);
    }
}
