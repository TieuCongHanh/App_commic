package com.example.myapplication.Interface;

import com.example.myapplication.Model.UserData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {
    @POST("user/login")
    Call<UserData> login(@Body UserData userData);
    @FormUrlEncoded
    @PUT("user/doimk")
    Call<Void> changePassword(
            @Field("userId") String userId,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword
    );
}
