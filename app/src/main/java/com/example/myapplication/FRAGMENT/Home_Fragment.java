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
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.example.myapplication.Adapter.viewpager2.ComicsAdapter;
import com.example.myapplication.Adapter.viewpager2.SlideAdapterHome;
import com.example.myapplication.Adapter.viewpager2.ViewPagerAdapter;
import com.example.myapplication.Chitiet_Comics;
import com.example.myapplication.Interface.Comics_Interface;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.Photo;
import com.example.myapplication.Model.UserData;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import me.relex.circleindicator.CircleIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Home_Fragment extends Fragment {

    UserData userData;
    SearchView timkiem;
    private ViewPager vpr;
    private RecyclerView rcvComic;
    private ComicsAdapter comicsAdapter;
    private CircleIndicator circleIndicator;
    private SlideAdapterHome slideAdapter;
    private Timer timer;
    private List<Photo> photoList;
    private Animation animation;
    private String selectedItemId;
    private Handler handler = new Handler(Looper.getMainLooper());
    List<Comics> list_comic;
    static final  String BASE_URL="http://192.168.0.116:3000/api/";

    View view;

    public Home_Fragment() {
        // Required empty public constructor
    }
    public static Home_Fragment newInstance(UserData userData) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putParcelable("USER_DATA_EXTRA", userData);
        fragment.setArguments(args);
        return fragment;
    }


     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home_, container, false);
         if (getArguments() != null) {
             userData = getArguments().getParcelable("USER_DATA_EXTRA");
             if (userData != null) {
                 String username = userData.getUsername();
                 String userId = userData.getId();
                 // Tiếp tục xử lý dữ liệu username và userId theo ý muốn
             }
         }

        anhXa();
        getComic();

        // Inflate the layout for this fragment
        return view;
    }

    private void anhXa(){
        //slide
        timkiem = view.findViewById(R.id.seachView);
        vpr = (ViewPager) view.findViewById(R.id.vpr);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        photoList = getListPhoto();
        slideAdapter = new SlideAdapterHome(getContext(), photoList);
        vpr.setAdapter(slideAdapter);
        circleIndicator.setViewPager(vpr);
        slideAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlide();
        // list comic
        rcvComic=view.findViewById(R.id.rcv_ComicList);
        //
        list_comic= new ArrayList<Comics>();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(photoList);
        rcvComic.setAdapter(viewPagerAdapter);

        comicsAdapter = new ComicsAdapter(getActivity(), list_comic, new ComicsAdapter.OnItemClickListener() {
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
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        rcvComic.setLayoutManager(gridLayoutManager);
        rcvComic.setAdapter(comicsAdapter);

        timkiem.clearFocus();
        searchComic();
    }
    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.banner4));
        list.add(new Photo(R.drawable.banner3));
        list.add(new Photo(R.drawable.banner2));
        list.add(new Photo(R.drawable.banner1));
        list.add(new Photo(R.drawable.banner));
        return list;
    }
    private void autoSlide() {
        if (photoList == null || photoList.isEmpty() || vpr == null) {
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int curentItem = vpr.getCurrentItem();
                        int toltalItem = photoList.size() - 1;
                        if (curentItem < toltalItem) {
                            curentItem++;
                            vpr.setCurrentItem(curentItem);
                        } else {
                            vpr.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 3000, 4000);
    }


    private void getComic(){
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
                if(response.isSuccessful()){
                    list_comic.clear();
                    list_comic.addAll(response.body());
                    Log.d("zzzzzzzz", "onResponse: "+list_comic);
                    Log.d("zzzzzzzzzzzz", "onResponse: "+ response.body());
                    comicsAdapter.notifyDataSetChanged();

                    //tạo adapter đổ lên listview
                }
                else {
                    Toast.makeText(getActivity(), "Không lấy được dữ liệu" +response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Comics>> call, Throwable t) {

                Log.e("RetrofitError", "onFailure: ", t);
                Toast.makeText(getActivity(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void searchComic(){
        timkiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterliss(newText);
                return true;
            }
            private void filterliss(String Text) {
                List<Comics> fiteliss = new ArrayList<>();
                for (Comics comic : list_comic) {
                    if (comic.getName().toLowerCase().contains(Text.toLowerCase())) {
                        fiteliss.add(comic);
                    }
                }
                if (fiteliss.isEmpty()) {

                } else {
                    comicsAdapter.setfilterliss(fiteliss);
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
    }



}