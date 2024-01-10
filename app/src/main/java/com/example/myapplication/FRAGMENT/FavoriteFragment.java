package com.example.myapplication.FRAGMENT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.viewpager2.ComicsAdapter;
import com.example.myapplication.Adapter.viewpager2.FavoriteListAdapter;
import com.example.myapplication.Adapter.viewpager2.SlideAdapterHome;
import com.example.myapplication.Adapter.viewpager2.ViewPagerAdapter;
import com.example.myapplication.Chitiet_Comics;
import com.example.myapplication.Interface.Comics_Interface;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.Photo;
import com.example.myapplication.Model.UserData;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FavoriteFragment extends Fragment {
    UserData userData;
    private RecyclerView rcvComic;
    private FavoriteListAdapter favoriteListAdapter;
    private String selectedItemId;
    List<Comics> list_comic;
    Comics comic;
    static final  String BASE_URL="http://192.168.0.116:3000/api/";
    View view;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(UserData userData) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();

        args.putParcelable("USER_DATA_EXTRA", userData);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_favorite, container, false);
        if (getArguments() != null) {
            userData = getArguments().getParcelable("USER_DATA_EXTRA");
            if (userData != null) {
                String username = userData.getUsername();
                String userId = userData.getId();
                // Tiếp tục xử lý dữ liệu username và userId theo ý muốn
            }
        }

        anhXa();
        getComicLove();

        // Inflate the layout for this fragment
        return view;
    }
    private void anhXa(){
        // list comic
        rcvComic=view.findViewById(R.id.rcv_ComicList);
        list_comic= new ArrayList<Comics>();
        favoriteListAdapter = new FavoriteListAdapter(getActivity(), list_comic, new FavoriteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Comics comic) {
                // Lấy thông tin chi tiết của item được bấm và chuyển sang Activity mới
                String selectedItemId = comic.get_id();
                Intent intent = new Intent(getActivity(), Chitiet_Comics.class);
                intent.putExtra("COMIC_EXTRA", comic);
                intent.putExtra("USER_DATA_EXTRA", userData); // Truyền userData qua Intent
                Log.d("zzz"," " + comic);
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcvComic.setLayoutManager(gridLayoutManager);
        rcvComic.setAdapter(favoriteListAdapter);


    }
    private void getComicLove(){
        // tạo gson
        Gson gson= new GsonBuilder().setLenient().create();

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // sử dụng interface

        Comics_Interface comicInterface=retrofit.create(Comics_Interface.class);

        //tạo đối tượng

        Call<List<Comics>> objCall= comicInterface.danh_sach_comic();
        objCall.enqueue(new Callback<List<Comics>>() {
            @Override
            public void onResponse(Call<List<Comics>> call, Response<List<Comics>> response) {
                if (response.isSuccessful()) {
                    List<Comics> favoriteComicsList = response.body();
                    favoriteListAdapter.setFavoriteComicsList(favoriteComicsList);
                    Log.d("zzzzzzzz", "onResponse: " + favoriteComicsList);
                } else {
                    Toast.makeText(getActivity(), "Không lấy được dữ liệu" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comics>> call, Throwable t) {

                Log.e("RetrofitError", "onFailure: ", t);
                Toast.makeText(getActivity(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}