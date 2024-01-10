package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Adapter.viewpager2.ChaptersAdapter;
import com.example.myapplication.Adapter.viewpager2.ReadComicsAdapter;
import com.example.myapplication.Interface.Comics_Interface;
import com.example.myapplication.Model.Chapter;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.UserData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Read_Chapter extends AppCompatActivity {
    private Chapter chapter;
    private int selectedChapterPosition = -1;
    private List<Chapter> chapterList;
    private Comics comic;
    private UserData userData;
    static final String BASE_URL = "http://192.168.0.116:3000/api/";
    ChaptersAdapter chaptersAdapter;
    private boolean isshowchap = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);

        // Nhận thông tin chapter từ Intent
        Intent intent = getIntent();
        chapter = intent.getParcelableExtra("CHAPTER_EXTRA");
        chapterList = intent.getParcelableArrayListExtra("CHAPTER_LIST_EXTRA");
        selectedChapterPosition = intent.getIntExtra("SELECTED_CHAPTER_POSITION", -1);
        comic = getIntent().getParcelableExtra("COMIC_EXTRA");
        userData = getIntent().getParcelableExtra("USER_DATA_EXTRA");

        if (chapter != null) {
            // Hiển thị nội dung chapter

            // Lấy danh sách hình ảnh từ chapter
            List<String> images = chapter.getContent();

            // Hiển thị danh sách hình ảnh bằng RecyclerView
            RecyclerView recyclerView = findViewById(R.id.readChapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ReadComicsAdapter imageAdapter = new ReadComicsAdapter(images);
            recyclerView.setAdapter(imageAdapter);

            LinearLayout showtren,showduoi;
            showtren = findViewById(R.id.showtren);
            showduoi = findViewById(R.id.showduoi);

            // code chức năng bấm 2 lần vào màn hình
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                private long lastTouchTime = 0;
                private boolean isToggled = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastTouchTime < 500) {
                            // Đã chạm nhanh vào màn hình một lần nữa trong vòng 500ms, coi đây là lần chạm thứ 2
                            if (isToggled) {
                                // Ẩn showtren và showduoi nếu đã hiển thị
                                showtren.setVisibility(View.GONE);
                                showduoi.setVisibility(View.GONE);
                            } else {
                                // Hiển thị showtren và showduoi nếu chưa hiển thị
                                showtren.setVisibility(View.VISIBLE);
                                showduoi.setVisibility(View.VISIBLE);
                            }
                            isToggled = !isToggled;
                        }
                        lastTouchTime = currentTime;
                    }
                    return false; // Đảm bảo sự kiện chạm vẫn được truyền xuống để xử lý lướt lên/xuống.
                }
            });



        } else {
            // Xử lý khi thông tin chapter là null
        }

        // Xử lý nút "Previous" và "Next"
        ImageView btnPreviousChapter = findViewById(R.id.btnPrevious);
        ImageView btnNextChapter = findViewById(R.id.btnNext);

        btnPreviousChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedChapterPosition > 0) {
                    // Chuyển đến chapter trước đó
                    Chapter previousChapter = chapterList.get(selectedChapterPosition - 1);
                    navigateToChapter(previousChapter, selectedChapterPosition - 1);
                } else {
                    // Đã ở chapter đầu tiên
                    Toast.makeText(Read_Chapter.this, "Đây là chapter đầu tiên", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedChapterPosition < chapterList.size() - 1) {
                    // Chuyển đến chapter tiếp theo
                    Chapter nextChapter = chapterList.get(selectedChapterPosition + 1);
                    navigateToChapter(nextChapter, selectedChapterPosition + 1);
                } else {
                    // Đã ở chapter cuối cùng
                    Toast.makeText(Read_Chapter.this, "Đây là chapter cuối cùng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView quaylaichitiet = findViewById(R.id.quaylaichitiet);
        quaylaichitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Read_Chapter.this, Chitiet_Comics.class);
                intent.putExtra("COMIC_EXTRA", comic);
                intent.putExtra("USER_DATA_EXTRA", userData); // Truyền userData qua Intent
                startActivity(intent);
            }
        });


        ImageView showcommic, showcomment;
        RecyclerView recychapter;

        recychapter = findViewById(R.id.recychapter);
        showcommic = findViewById(R.id.showcomic);
        showcomment = findViewById(R.id.btncm);

        chapterList = new ArrayList<>();

        recychapter.setVisibility(View.GONE);
        showcommic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isshowchap){
                    recychapter.setVisibility(View.VISIBLE);
                    isshowchap = true;
                }else{
                    recychapter.setVisibility(View.GONE);
                    isshowchap = false;
                }
            }
        });

        showcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Read_Chapter.this, Comment_activity.class);
                intent.putExtra("COMIC_EXTRA", comic);
                intent.putExtra("USER_DATA_EXTRA", userData); // Truyền userData qua Intent
                startActivity(intent);
            }
        });


         chaptersAdapter = new ChaptersAdapter(this, chapterList, new ChaptersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chapter chapter) {
                // Xử lý sự kiện khi người dùng click vào một item chapter
                selectedChapterPosition = chapterList.indexOf(chapter);
                chaptersAdapter.setSelectedChapterPosition(selectedChapterPosition);

                ArrayList<Chapter> chapterArrayList = new ArrayList<>(chapterList);
                Intent intent = new Intent(Read_Chapter.this, Read_Chapter.class);
                intent.putExtra("CHAPTER_EXTRA", chapter);
                intent.putParcelableArrayListExtra("CHAPTER_LIST_EXTRA", chapterArrayList);
                intent.putExtra("SELECTED_CHAPTER_POSITION", selectedChapterPosition);
                intent.putExtra("COMIC_EXTRA", comic);
                intent.putExtra("USER_DATA_EXTRA", userData);
                startActivity(intent);
            }
        });

        // Khởi tạo và cấu hình RecyclerView
        recychapter.setLayoutManager(new LinearLayoutManager(this));
        recychapter.setAdapter(chaptersAdapter);

        getChaptersByComicId(comic.get_id());
    }

    private void navigateToChapter(Chapter chapter, int newPosition) {
        Intent intent = new Intent(Read_Chapter.this, Read_Chapter.class);
        intent.putExtra("CHAPTER_EXTRA", chapter);
        intent.putParcelableArrayListExtra("CHAPTER_LIST_EXTRA", (ArrayList<Chapter>) chapterList);
        intent.putExtra("SELECTED_CHAPTER_POSITION", newPosition);
        intent.putExtra("COMIC_EXTRA", comic);
        intent.putExtra("USER_DATA_EXTRA", userData);
        startActivity(intent);
    }

    private void getChaptersByComicId(String comicId) {
        // Tạo Retrofit và interface để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Comics_Interface comicsInterface = retrofit.create(Comics_Interface.class);

        // Gọi API để lấy danh sách chapter dựa trên comicId
        Call<List<Chapter>> call = comicsInterface.getChaptersByComicId(comicId);
        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful()) {
                    List<Chapter> chapters = response.body();
                    if (chapters != null && !chapters.isEmpty()) {
                        chapterList.clear();
                        chapterList.addAll(chapters);
                        chaptersAdapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị thông báo nếu không có chapter nào
                    }
                } else {
                    // Xử lý lỗi khi gọi API không thành công
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
            }
        });
    }
}
