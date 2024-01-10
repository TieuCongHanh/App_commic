package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.viewpager2.TabsPagerAdapter;
import com.example.myapplication.Adapter.viewpager2.ViewPager2Adapter;
import com.example.myapplication.FRAGMENT.fragment_gt;
import com.example.myapplication.FRAGMENT.fragment_ml;
import com.example.myapplication.Interface.Comics_Interface;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.UserData;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chitiet_Comics extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;
    private Comics comic;
    private UserData userData;
    List<UserData> userDataList = new ArrayList<>();
    private List<Comics> favoriteComicsList = new ArrayList<>();
 ViewPager2Adapter viewPager2Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_comics);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        TextView txtname = findViewById(R.id.name);
        ImageView avt = findViewById(R.id.avt);
        ImageView btnlove = findViewById(R.id.btnlove);
        Button btnread = findViewById(R.id.btnread);
        comic = getIntent().getParcelableExtra("COMIC_EXTRA");
        userData = getIntent().getParcelableExtra("USER_DATA_EXTRA");

        if (comic.isLove()) {
            btnlove.setImageResource(R.drawable.baseline_favorite_24); // Đặt hình ảnh thành đỏ
        } else {
            btnlove.setImageResource(R.drawable.favorite_24); // Đặt hình ảnh thành xám
        }

        ImageView quaylaihome = findViewById(R.id.quaylaihome);
        quaylaihome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Chitiet_Comics.this, Home.class);
                intent.putExtra("USER_DATA_EXTRA", userData);
               startActivity(intent);
            }
        });

        if (comic != null) {

            // Hiển thị chi tiết thông tin Commic
            // Ví dụ: gán giá trị cho các TextView
            txtname.setText(comic.getName());

            Picasso.get().load(comic.getCoverImage()).into(avt);
            Log.d("zzzzzzzz", " aa" + comic.getCoverImage());

        }
        btnread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chitiet_Comics.this, ReadComics.class);
                // Đính kèm thông tin comic vào Intent
                intent.putExtra("COMIC_EXTRA", comic);
                intent.putExtra("USER_DATA_EXTRA", userData);
                startActivity(intent);
            }
        });

        btnlove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đảo ngược giá trị của thuộc tính "love" của truyện
                boolean newLoveStatus = !comic.isLove();
                updateLoveStatus(newLoveStatus);
                if (!comic.isLove()) {
                    btnlove.setImageResource(R.drawable.baseline_favorite_24); // Đặt hình ảnh thành đỏ
                    Toast.makeText(Chitiet_Comics.this, "Đã thêm vào ưa thích", Toast.LENGTH_SHORT).show();
                } else {
                    btnlove.setImageResource(R.drawable.favorite_24); // Đặt hình ảnh thành xám
                    Toast.makeText(Chitiet_Comics.this, "Đã bỏ ưa thích", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        fragment_gt fragmentGT = fragment_gt.newInstance(comic, userData, userDataList); // Truyền Commic vào FragmentGT
        fragment_ml fragmentML = fragment_ml.newInstance(comic, userData);
        tabsPagerAdapter.addFragment(fragmentGT, "Giới Thiệu");
        tabsPagerAdapter.addFragment(fragmentML, "Mục Lục");
        viewPager.setAdapter(tabsPagerAdapter);

        // Kết nối ViewPager với TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }
    private void updateLoveStatus(final boolean newLoveStatus) {
        // Gửi yêu cầu API để cập nhật thuộc tính "love"
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.116:3000/api/") // Thay thế bằng URL của API của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Tạo service từ interface API
        Comics_Interface comicService = retrofit.create(Comics_Interface.class);

        // Gọi API để cập nhật trạng thái "love" của truyện
        Call<Comics> call = comicService.toggleLoveStatus(comic.get_id());
        call.enqueue(new Callback<Comics>() {
            @Override
            public void onResponse(Call<Comics> call, Response<Comics> response) {
                if (response.isSuccessful()) {
                    comic.setLove(newLoveStatus);
                } else {
                    // Xử lý khi gặp lỗi
                    Toast.makeText(Chitiet_Comics.this, "Lỗi khi cập nhật trạng thái yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comics> call, Throwable t) {
                // Xử lý khi gặp lỗi kết nối
                Toast.makeText(Chitiet_Comics.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}