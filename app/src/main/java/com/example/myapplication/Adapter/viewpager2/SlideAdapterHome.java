package com.example.myapplication.Adapter.viewpager2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.Photo;
import com.example.myapplication.R;


import java.util.List;

public class SlideAdapterHome extends PagerAdapter {
    private Context context;
    private List<Photo> photoList;

    public SlideAdapterHome(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slideshow_home, container, false);
        ImageView imgSlider = view.findViewById(R.id.img_slider);
        Photo photo = photoList.get(position);
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_down);
        if (photo != null) {
            Glide.with(context).load(photo.getResourceId()).into(imgSlider);
        }
        container.addView(view);
        view.startAnimation(animation);
        return view;
    }

    @Override
    public int getCount() {
        if (photoList != null){
            return photoList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
