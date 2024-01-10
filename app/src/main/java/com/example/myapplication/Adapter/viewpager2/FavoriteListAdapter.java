package com.example.myapplication.Adapter.viewpager2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Comics;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListAdapter  extends RecyclerView.Adapter<FavoriteListAdapter.loveViewHolder> {
    private Context context;
    private List<Comics> list = new ArrayList<>();
    private OnItemClickListener listener;

    public FavoriteListAdapter(Context context, List<Comics> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    public void setFavoriteComicsList(List<Comics> fiteliss) {
        List<Comics> filteredList = new ArrayList<>();
        for (Comics comic : fiteliss) {
            if (comic.isLove()) {
                filteredList.add(comic);
            }
        }
        this.list = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public loveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new loveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull loveViewHolder holder, int position) {
        Comics comics = list.get(position);

        if (comics == null || !comics.isLove()) {
            // Nếu thuộc tính "love" là false hoặc comic là null, thì không hiển thị
            holder.itemView.setVisibility(View.GONE);
            return;
        }

        // Nếu thuộc tính "love" là true, hiển thị truyện
        holder.itemView.setVisibility(View.VISIBLE);

        Log.d("ddddddddd", "linkkkkkkkkkk: " + comics.getCoverImage());
        Picasso.get().load(comics.getCoverImage()).into(holder.imgComic);
        holder.tvName.setText(comics.getName());
    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


    public class loveViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgComic;
        private TextView tvName;

        public loveViewHolder(@NonNull View itemView) {
            super(itemView);

            imgComic = itemView.findViewById(R.id.imgComic);
            tvName = itemView.findViewById(R.id.item_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Comics comic = list.get(position);
                            listener.onItemClick(comic);
                        }
                    }
                }
            });
        }
    }

    // Interface để xử lý sự kiện click vào item
    public interface OnItemClickListener {
        void onItemClick(Comics comic);
    }
}
