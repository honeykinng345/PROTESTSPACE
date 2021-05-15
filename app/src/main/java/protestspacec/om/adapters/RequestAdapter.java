package protestspacec.om.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import protestspacec.om.Model.Qoutation;
import protestspacec.om.R;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    List<Qoutation> qoutations;
    String postId;

    DatabaseReference mQoutationRef;

    public RequestAdapter(Context context, List<Qoutation> qoutations, String postId) {
        this.context = context;
        this.qoutations = qoutations;
        this.postId = postId;

        mQoutationRef = FirebaseDatabase.getInstance().getReference().child("Qoutation");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_qoutation, parent, false);
        return new RequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Qoutation qoutation = qoutations.get(position);

        holder.uName.setText(qoutation.getUname());
        holder.descriptionText.setText(qoutation.getDescription());
        holder.priceText.setText(qoutation.getPrice());

        holder.acceptBtn.setOnClickListener(v -> requestAccepted(qoutation));
        holder.rejectBtn.setOnClickListener(v -> requestRejected(qoutation));
    }

    private void requestAccepted(Qoutation qoutation) {
        //payment work

    }

    private void requestRejected(Qoutation qoutation) {
        mQoutationRef.child(postId).child(qoutation.getUid()).removeValue().addOnSuccessListener(aVoid -> {
            Toast.makeText(context, "Rejected", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return qoutations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImg;
        TextView uName, descriptionText, priceText;
        Button acceptBtn, rejectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.cimg);
            uName = itemView.findViewById(R.id.nametv);
            descriptionText = itemView.findViewById(R.id.commenttv);
            priceText = itemView.findViewById(R.id.priceText);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);

        }
    }
}
