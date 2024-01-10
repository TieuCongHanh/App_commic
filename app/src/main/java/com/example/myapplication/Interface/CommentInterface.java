package com.example.myapplication.Interface;

import com.example.myapplication.Model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentInterface {
    @GET("comment") // Thay "comment" bằng đường dẫn API lấy danh sách comment theo id truyện
    Call<List<Comment>> getCommentsByComicId(@Query("comicId") String comicId);
    // Định nghĩa phương thức POST để thêm bình luận mới
    @FormUrlEncoded
    @POST("comment/add") // Điền đúng đường dẫn API để thêm bình luận
    Call<Comment> addComment(
            @Field("comicId") String comicId,
            @Field("userId") String userId,
            @Field("content") String commentContent
    );
    // API sửa bình luận
    @PUT("comment/edit/{commentId}")
    @FormUrlEncoded
    Call<Comment> updateComment(
            @Path("commentId") String commentId,
            @Field("userId") String userId,
            @Field("content") String newContent);

    // API xóa bình luận
    // Phương thức gọi API để xóa bình luận
    @DELETE("comment/delete/{commentId}")
    Call<Void> deleteComment(@Path("commentId") String commentId);
}
