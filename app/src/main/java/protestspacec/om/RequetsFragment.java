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

import protestspacec.om.Model.Qoutation;
import protestspacec.om.adapters.RequestAdapter;

public class RequetsFragment extends Fragment {

    ArrayList<Qoutation> qoutations = new ArrayList<>();
    DatabaseReference mQoutationRef;
    RecyclerView recyclerView;

    String postId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment, container, false);

        postId = getArguments().getString("postId");

        if (postId == null)
            Toast.makeText(requireContext(), "postid ull", Toast.LENGTH_LONG).show();

        recyclerView = view.findViewById(R.id.recyclerView);

        loadData();

        return view;
    }

    private void loadData() {

        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(new RequestAdapter(requireContext(), qoutations, postId));

        mQoutationRef = FirebaseDatabase.getInstance().getReference().child("Qoutation").child(postId);
        mQoutationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                qoutations.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Qoutation qoutation = ds.getValue(Qoutation.class);
                    qoutations.add(qoutation);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
