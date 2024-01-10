package com.example.myapplication.Interface;


import com.example.myapplication.Model.Chapter;
import com.example.myapplication.Model.Comics;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Comics_Interface {

    @GET("commic/list")
    Call<List<Comics>> danh_sach_comic();
    @GET("comics/list")
    Call<List<Chapter>> getChaptersByComicId(@Query("comicId") String comicId);
    @PUT("commic/{comicId}/toggleLove")
    Call<Comics> toggleLoveStatus(@Path("comicId") String comicId);
}
