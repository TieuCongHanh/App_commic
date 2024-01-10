package com.example.myapplication.Adapter.viewpager2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Chapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.CahpterViewHolder>{
    private Context context;
    private List<Chapter> list = new ArrayList<>();
    private OnItemClickListener listener;
    private int selectedChapterPosition = -1; // Mặc định không có item nào được chọn

    // Tạo một setter cho selectedChapterPosition
    public void setSelectedChapterPosition(int position) {
        selectedChapterPosition = position;
        notifyDataSetChanged(); // Cập nhật lại giao diện khi item được chọn thay đổi màu
    }
    public ChaptersAdapter(Context context, List<Chapter> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    public void setfilterliss(List<Chapter> fiteliss) {
        this.list = fiteliss;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CahpterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);
        return new CahpterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CahpterViewHolder holder, int position) {
        Chapter comics = list.get(position);

        if (comics == null)
            return;
        if (position == selectedChapterPosition) {
            // Đổi màu của item chapter khi được chọn
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_tab_1));
        } else {
            // Màu bình thường của item chapter
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.tvName.setText(comics.getChapNumber());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }




    public class CahpterViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        public CahpterViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.nameChapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Chapter chapter = list.get(position);
                            listener.onItemClick(chapter);
                        }
                    }
                }
            });
        }
    }

    // Interface để xử lý sự kiện click vào item
    public interface OnItemClickListener {
        void onItemClick(Chapter chapter);
    }
}
