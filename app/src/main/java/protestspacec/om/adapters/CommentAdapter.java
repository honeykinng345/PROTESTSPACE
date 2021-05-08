package protestspacec.om.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import protestspacec.om.Model.Comment;
import protestspacec.om.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    List<Comment> commentList;

    public CommentAdapter(List<Comment> comments){
        commentList = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.uName.setText(comment.getuName());
        holder.commentText.setText(comment.getComment());
        if (comment.getuImg() != null)
            Picasso.get().load(Uri.parse(comment.getuImg())).into(holder.userImg);

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImg;
        TextView uName, commentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.cimg);
            uName = itemView.findViewById(R.id.nametv);
            commentText = itemView.findViewById(R.id.commenttv);



        }
    }
}
