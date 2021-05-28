package protestspacec.om;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import protestspacec.om.Model.Qoutation;
import protestspacec.om.adapters.RequestAdapter;

public class RequetsFragment extends Fragment {

    ArrayList<Qoutation> qoutations = new ArrayList<>();
    DatabaseReference mQoutationRef;
    RecyclerView recyclerView;

    String postId;
    String lawyerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment, container, false);

        if (getArguments() != null) {
            postId = getArguments().getString("postId");
            loadData();
        }
        else {
            lawyerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            loadLawyerRequests();
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        init();

        return view;
    }

    private void init() {
        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        if (recyclerView.getAdapter() == null)
            recyclerView.setAdapter(new RequestAdapter(requireContext(), qoutations, postId));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    private void loadLawyerRequests() {
        mQoutationRef = FirebaseDatabase.getInstance().getReference().child("Qoutation");
        mQoutationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                qoutations.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.hasChild(lawyerId)) {
                        Qoutation qoutation = ds.child(lawyerId).getValue(Qoutation.class);
                        qoutations.add(qoutation);
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadData() {

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

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
         if (resultCode == RESULT_OK){
             Helper.message(requireContext(), "success");

             String lawyerID = data.getStringExtra("lawyerId");
             mQoutationRef.child("Qoutation").child(postId).child(lawyerID).child("isAccepted").setValue(true);

         }else Helper.message(requireContext(), "transcation failed");
        }
    }*/
}
