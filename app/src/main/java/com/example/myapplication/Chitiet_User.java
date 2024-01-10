package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Model.UserData;

public class Chitiet_User extends AppCompatActivity {
ImageView out;
TextView txtusername,txtpassword,txtemail,txtfullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_user);
        out = findViewById(R.id.btnql);
        txtemail = findViewById(R.id.txtemail);
        txtfullname = findViewById(R.id.txtfullname);
        txtusername = findViewById(R.id.txtusername);
        // Nhận dữ liệu userData từ Intent
        UserData userData = getIntent().getParcelableExtra("USER_DATA_EXTRA");

        // Tiếp tục xử lý dữ liệu userData theo ý muốn
        if (userData != null) {
            // Sử dụng dữ liệu userData ở đây
            String username = userData.getUsername();
            String password = userData.getPassword();
            String userId = userData.getId();
            String email = userData.getEmail();
            String fullname = userData.getFullname();

            txtusername.setText(username);
            txtfullname.setText(fullname);
            txtemail.setText(email);
            // ...
            out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed(); // Quay trở lại Fragment trước đó
                }
            });
        }
    }
}