package com.example.myapplication.Adapter.viewpager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Model.Photo;
import com.example.myapplication.R;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {
    private List<Photo> photoList;

    public ViewPagerAdapter(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        // Thiết lập ViewPager với dữ liệu từ photoList và CircleIndicator
        SlideAdapterHome slideAdapter = new SlideAdapterHome(holder.itemView.getContext(), photoList);
        holder.viewPager.setAdapter(slideAdapter);
        holder.circleIndicator.setViewPager(holder.viewPager);
        slideAdapter.registerDataSetObserver(holder.circleIndicator.getDataSetObserver());
    }

    @Override
    public int getItemCount() {
        return 1; // Chỉ có một ViewPager trong RecyclerView
    }
    public class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        private CircleIndicator circleIndicator;

        public ViewPagerViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPagerInRecyclerView);
            circleIndicator = itemView.findViewById(R.id.circleIndicatorInRecyclerView);
        }
    }
}
