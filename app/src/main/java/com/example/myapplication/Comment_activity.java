package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Adapter.viewpager2.CommentAdapter;
import com.example.myapplication.Interface.CommentInterface;
import com.example.myapplication.Model.Chapter;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.NotifyConfig;
import com.example.myapplication.Model.UserData;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Comment_activity extends AppCompatActivity {
    private Comics commic; // Thêm biến để lưu thông tin Commic
    private UserData userData;
    private List<UserData> userDataList;
    private List<Comment> commentActivityList;
    private CommentAdapter commentAdapter;
    static final String BASE_URL = "http://192.168.0.116:3000/api/";
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://192.168.0.116:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ImageView quaylaichitiet1 = findViewById(R.id.quaylaichitiet1);
        quaylaichitiet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Quay trở lại Fragment trước đó
            }
        });

        mSocket.connect();
        mSocket.on("new msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Comment_activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data_sv_send= (String) args[0];
                       // Toast.makeText(Comment_activity.this, "Server trả về "+data_sv_send, Toast.LENGTH_SHORT).show();
                        // hiển thị noify status
                        postNotify("Thông báo từ server" , data_sv_send);
                    }
                });
            }
        });

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        commic = intent.getParcelableExtra("COMIC_EXTRA"); // Lấy thông tin Commic
        userData = intent.getParcelableExtra("USER_DATA_EXTRA"); // Lấy thông tin UserData
        userDataList = intent.getParcelableArrayListExtra("userDataList"); // Lấy danh sách UserData



        // Khởi tạo RecyclerView và adapter (chắc chắn bạn đã thêm các layout và view cần thiết vào activity_comment.xml)
        RecyclerView  recyclerViewComments = findViewById(R.id.recyclerViewComments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        commentActivityList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentActivityList,userDataList);
        recyclerViewComments.setAdapter(commentAdapter);


        if (commic != null) {
            // Bạn có thể sử dụng commic mà không gây ra lỗi NullPointerException
            getCommentsByComicId(commic.get_id());
        }else {
            Toast.makeText(this, "không có", Toast.LENGTH_SHORT).show();
        }
        ImageView btnAddComment = findViewById(R.id.btncm);
        EditText editTextComment = findViewById(R.id.edtcm);


        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData == null) {
                    // Hiển thị thông báo "Cần đăng nhập mới bình luận được"
                    Toast.makeText(Comment_activity.this, "Cần đăng nhập mới bình luận được", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Thêm bình luận theo id truyện
                    String commentContent = editTextComment.getText().toString().trim();
                    if (commentContent.isEmpty()) {
                        // Gọi API để thêm bình luận mới
                        Toast.makeText(Comment_activity.this, "Cần nhập vào content mới có thể thêm bình luận", Toast.LENGTH_SHORT).show();
                    }else{
                        addComment(commic.get_id(), userData.getId(), commentContent);
                        editTextComment.setText("");
                    }
                }
            }
        });

        // Xử lý sự kiện khi người dùng click vào một item bình luận
        commentAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Comment comment) {
                if (userData == null) {
                    // Hiển thị thông báo "Cần đăng nhập mới sửa/xóa bình luận được"
                    Toast.makeText(Comment_activity.this, "Cần đăng nhập mới sửa/xóa bình luận được", Toast.LENGTH_SHORT).show();
                } else if (!userData.getId().equals(comment.getUserId())) {
                    // Hiển thị thông báo "Bạn không có quyền sửa/xóa bình luận này"
                    Toast.makeText(Comment_activity.this, "Bạn không có quyền sửa/xóa bình luận này", Toast.LENGTH_SHORT).show();
                } else {
                    // Hiển thị dialog cho phép người dùng sửa/xóa bình luận
                    showEditOrDeleteCommentDialog(comment);
                }
            }
        });


    }
    private void getCommentsByComicId(String comicId) {
        // Tạo Retrofit và interface để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommentInterface commentInterface = retrofit.create(CommentInterface.class);

        // Gọi API lấy danh sách comment theo ID truyện
        Call<List<Comment>> call = commentInterface.getCommentsByComicId(comicId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    commentActivityList.clear();
                    List<Comment> comments = response.body();
                    if (comments != null && !comments.isEmpty()) {
                        Collections.reverse(comments); // thằng này là đảo comment mới lên đầu
                        commentActivityList.addAll(comments);
                        commentAdapter.notifyDataSetChanged();
                    } else {
                        // Hiển thị thông báo không có bình luận nào
                    }
                } else {
                    // Hiển thị thông báo lỗi
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Hiển thị thông báo lỗi
            }
        });
    }
    private void addComment(String comicId, String userId, String commentContent) {
        // Tạo Retrofit và interface để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommentInterface commentInterface = retrofit.create(CommentInterface.class);

        // Gọi API để thêm bình luận mới
        Call<Comment> call = commentInterface.addComment(comicId, userId, commentContent);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    // Cập nhật danh sách bình luận sau khi thêm thành công
                    Comment newComment = response.body();
                    List<Comment> newComments = new ArrayList<>(commentActivityList);
                    newComments.add(0,newComment);
                    commentAdapter.updateCommentList(newComments);

                } else {
                    // Hiển thị thông báo lỗi khi thêm bình luận không thành công
                    Toast.makeText(Comment_activity.this, "Lỗi khi thêm bình luận", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Hiển thị thông báo lỗi khi gọi API thêm bình luận thất bại
                Toast.makeText(Comment_activity.this, "Lỗi khi gọi API thêm bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateComment(String commentId, String userId, String newContent) {
        // Tạo Retrofit và interface để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommentInterface commentInterface = retrofit.create(CommentInterface.class);

        // Gọi API để cập nhật bình luận
        Call<Comment> call = commentInterface.updateComment(commentId, userId, newContent);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    // Cập nhật danh sách bình luận sau khi sửa thành công
                    Comment updatedComment = response.body();
                    List<Comment> updatedComments = new ArrayList<>(commentActivityList);
                    int position = getCommentPosition(commentId); // Lấy vị trí của bình luận cần cập nhật
                    if (position != -1) {
                        updatedComments.set(position, updatedComment);
                        commentAdapter.updateCommentList(updatedComments);
                        Toast.makeText(Comment_activity.this, "update thành công", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Hiển thị thông báo lỗi khi sửa bình luận không thành công
                    Toast.makeText(Comment_activity.this, "Lỗi khi sửa bình luận", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Hiển thị thông báo lỗi khi gọi API sửa bình luận thất bại
                Toast.makeText(Comment_activity.this, "Lỗi khi gọi API sửa bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDialogEditComment(Comment comment) {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(Comment_activity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
        builder.setView(dialogView);

        // Khai báo và ánh xạ các view trong dialog
        EditText editTextComment = dialogView.findViewById(R.id.editTextComment);
        editTextComment.setText(comment.getContent());

        // Thiết lập các nút và sự kiện cho dialog
        builder.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lấy nội dung mới từ EditText
                String newContent = editTextComment.getText().toString().trim();

                // Kiểm tra xem người dùng đã nhập nội dung mới hay chưa
                if (!newContent.isEmpty()) {
                    // Gọi API để cập nhật bình luận
                    updateComment(comment.get_id(), userData.getId(), newContent);
                } else {
                    // Hiển thị thông báo "Không thể để trống nội dung bình luận"
                    Toast.makeText(Comment_activity.this, "Không thể để trống nội dung bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng dialog khi người dùng bấm nút Hủy
            }
        });

        // Hiển thị dialog
        Dialog dialog = builder.create();
        dialog.show();
    }
    private void deleteComment(String commentId) {
        // Tạo Retrofit và interface để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CommentInterface commentInterface = retrofit.create(CommentInterface.class);

        // Gọi API để xóa bình luận
        Call<Void> call = commentInterface.deleteComment(commentId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa bình luận thành công
                    // Thực hiện cập nhật danh sách bình luận hoặc thực hiện các tác vụ khác
                    // ...
                    Toast.makeText(Comment_activity.this, "Xóa bình luận thành công", Toast.LENGTH_SHORT).show();
                    getCommentsByComicId(commic.get_id());
                } else {
                    // Hiển thị thông báo lỗi khi xóa bình luận không thành công
                    Toast.makeText(Comment_activity.this, "Lỗi khi xóa bình luận", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Hiển thị thông báo lỗi khi gọi API xóa bình luận thất bại
                Toast.makeText(Comment_activity.this, "Lỗi khi gọi API xóa bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditOrDeleteCommentDialog(Comment comment) {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn hành động");
        String[] options = {"Sửa bình luận", "Xóa bình luận"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Sửa bình luận
                    showDialogEditComment(comment);
                } else if (which == 1) {
                    // Xóa bình luận
                    showDeleteCommentDialog(comment);
                }
            }
        });

        // Hiển thị dialog
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteCommentDialog(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa bình luận");
        builder.setMessage("Bạn có chắc muốn xóa bình luận này?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Gọi phương thức để xóa bình luận
                deleteComment(comment.get_id());
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Hàm lấy vị trí của bình luận trong danh sách commentList dựa vào commentId
    private int getCommentPosition(String commentId) {
        for (int i = 0; i < commentActivityList.size(); i++) {
            if (commentActivityList.get(i).get_id().equals(commentId)) {
                return i;
            }
        }
        return -1; // Không tìm thấy bình luận trong danh sách
    }
    void postNotify(String title, String content){
        // Khởi tạo layout cho Notify
        Notification customNotification = new NotificationCompat.Builder(Comment_activity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(android.R.drawable.ic_delete)
                .setContentTitle( title )
                .setContentText(content)
                .setAutoCancel(true)

                .build();
        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Comment_activity.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(Comment_activity.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(Comment_activity.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(Comment_activity.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy , customNotification);

    }


}