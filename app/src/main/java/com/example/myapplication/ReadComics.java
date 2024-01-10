package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.myapplication.Adapter.viewpager2.ReadComicsAdapter;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.UserData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReadComics extends AppCompatActivity {
    private Comics comic;
    private UserData userData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comics);
        // Nhận thông tin comic từ Intent
        Intent intent = getIntent();
        comic = intent.getParcelableExtra("COMIC_EXTRA");
        userData = intent.getParcelableExtra("USER_DATA_EXTRA");
        if (comic != null) {
            // Hiển thị hình ảnh coverImage


            // Lấy danh sách các hình ảnh từ comic
            List<String> images = comic.getImages();

            // Hiển thị danh sách các hình ảnh bằng RecyclerView
            RecyclerView recyclerView = findViewById(R.id.readrec);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ReadComicsAdapter imageAdapter = new ReadComicsAdapter(images);
            recyclerView.setAdapter(imageAdapter);
            Log.d("TAG", "Danh sách images: " + images.toString());
            // ... (thực hiện các chức năng khác với thông tin comic)
        } else {
            // Xử lý trường hợp comic là null
        }
    }
}