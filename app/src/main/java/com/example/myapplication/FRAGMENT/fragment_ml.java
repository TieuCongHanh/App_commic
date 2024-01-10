package com.example.myapplication.FRAGMENT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.Adapter.viewpager2.ChaptersAdapter;
import com.example.myapplication.Adapter.viewpager2.CommentAdapter;
import com.example.myapplication.Comment_activity;
import com.example.myapplication.Interface.Comics_Interface;
import com.example.myapplication.Interface.CommentInterface;
import com.example.myapplication.Logup;
import com.example.myapplication.Model.Chapter;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.NotifyConfig;
import com.example.myapplication.Model.UserData;
import com.example.myapplication.R;
import com.example.myapplication.Read_Chapter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragment_ml extends Fragment {
    private Comics comic;
    private UserData userData;
    private View view;
    private RecyclerView recyclerView;
    private List<Chapter> chapterList;
    private ChaptersAdapter chaptersAdapter;
    static final String BASE_URL = "http://192.168.0.116:3000/api/";
    private int selectedChapterPosition = -1; // Khởi tạo bằng một giá trị không hợp lệ

    public static fragment_ml newInstance(Comics comic, UserData userData) {
        fragment_ml fragment = new fragment_ml();
        Bundle args = new Bundle();
        args.putParcelable("COMIC_EXTRA", comic);
        args.putParcelable("USER_DATA_EXTRA", userData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comic = getArguments().getParcelable("COMIC_EXTRA");
            userData = getArguments().getParcelable("USER_DATA_EXTRA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ml, container, false);

        recyclerView = view.findViewById(R.id.chapters); // Thay R.id.recyclerView bằng ID thực tế của RecyclerView trong layout
        chapterList = new ArrayList<>();
        chaptersAdapter = new ChaptersAdapter(getActivity(), chapterList, new ChaptersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chapter chapter) {
                // Xử lý sự kiện khi người dùng click vào một item chapter
                selectedChapterPosition = chapterList.indexOf(chapter);
                chaptersAdapter.setSelectedChapterPosition(selectedChapterPosition);

                ArrayList<Chapter> chapterArrayList = new ArrayList<>(chapterList);
                Intent intent = new Intent(getActivity(), Read_Chapter.class);
                intent.putExtra("CHAPTER_EXTRA", chapter);
                intent.putParcelableArrayListExtra("CHAPTER_LIST_EXTRA", chapterArrayList);
                intent.putExtra("SELECTED_CHAPTER_POSITION", selectedChapterPosition);
                intent.putExtra("COMIC_EXTRA", comic);
                intent.putExtra("USER_DATA_EXTRA", userData);
                startActivity(intent);
            }
        });

        // Khởi tạo và cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(chaptersAdapter);

        if (comic != null) {
            // Xử lý thông tin về truyện (nếu cần)
        }

        // Gọi API để lấy danh sách chapter
        getChaptersByComicId(comic.get_id());


        return view;
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
