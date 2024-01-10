package com.example.myapplication.Adapter.viewpager2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Comics;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder> {
    private Context context;
    private List<Comics> list = new ArrayList<>();
    private OnItemClickListener listener;

    public ComicsAdapter(Context context, List<Comics> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
    public void setfilterliss(List<Comics> fiteliss) {
        this.list = fiteliss;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comics comics = list.get(position);

        if (comics == null)
            return;

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


    public class ComicViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgComic;
        private TextView tvName;

        public ComicViewHolder(@NonNull View itemView) {
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
