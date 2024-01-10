package com.example.myapplication.Adapter.viewpager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.UserData;
import com.example.myapplication.R;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> commentList;
    private List<UserData> userDataList;


    public CommentAdapter(List<Comment> commentList, List<UserData> userDataList) {
        this.commentList = commentList;
        this.userDataList = userDataList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.txtCommentContent.setText(comment.getContent());
        holder.txttime.setText(limitTo(comment.getTimestamp(), 10));

        // Tìm username tương ứng với userId trong danh sách userDataList
        String userId = comment.getUserId();
        String username = findUsernameByUserId(userId);
        holder.txtid.setText(userId);
    }

    @Override
    public int getItemCount() {
        if (commentList == null) {
            return 0; // Hoặc bạn có thể trả về giá trị khác tùy theo trường hợp cụ thể.
        }
        return commentList.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtCommentContent, txtid, txttime;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.txtid);
            txtCommentContent = itemView.findViewById(R.id.txtCommentContent);
            txttime = itemView.findViewById(R.id.txttime);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            // Gọi sự kiện clickItemComment khi người dùng bấm vào một item
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                clickItemComment(position);
            }
        }
        public void deleteComment(int position) {
            if (position >= 0 && position < commentList.size()) {
                commentList.remove(position);
                notifyItemRemoved(position);
            }
        }


    }

    public void updateCommentList(List<Comment> newComments) {
        commentList.clear();
        commentList.addAll(newComments);
        notifyDataSetChanged();
    }

    // Phương thức để tìm username theo userId
    private String findUsernameByUserId(String userId) {
        if (userDataList != null) {
            for (UserData userData : userDataList) {
                if (userData.getId().equals(userId)) {
                    return userData.getUsername();
                }
            }
        }
        return "Người dùng không xác định"; // Trả về giá trị mặc định nếu không tìm thấy
    }


    // ... Các phần còn lại của adapter

    // Tạo interface để lắng nghe sự kiện click vào item comment
    public interface OnItemClickListener {
        void onItemClick(Comment comment);
    }

    // Khai báo biến để lưu trữ listener
    private OnItemClickListener onItemClickListener;

    // Setter cho listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    // Gọi sự kiện clickItemComment khi người dùng bấm vào một item
    private void clickItemComment(int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(commentList.get(position));
        }
    }

        public String limitTo(String input, int length) {
            if (input.length() > length) {
                return input.substring(0, length) + "...";
            } else {
                return input;
            }
        }

}
