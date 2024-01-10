package com.example.myapplication.FRAGMENT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Chitiet_User;
import com.example.myapplication.Interface.ApiService;
import com.example.myapplication.Login;
import com.example.myapplication.Logup;
import com.example.myapplication.Model.RetrofitClient;
import com.example.myapplication.Model.UserData;
import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_Fragment extends Fragment {
    private UserData userData;
    LinearLayout btninfo, btndoipass, btnlogout;
    private Button btn_login;
    private Button btn_sigup;
    private TextView txtname,txtid;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_, container, false);

        // Lấy dữ liệu truyền vào từ ViewPager2Adapter
        if (getArguments() != null) {
            userData = getArguments().getParcelable("USER_DATA_EXTRA");
            if (userData != null) {
                String username = userData.getUsername();
                String userId = userData.getId();
                // Tiếp tục xử lý dữ liệu username và userId theo ý muốn

            }
        }

        // Tiếp tục cài đặt Fragment theo ý muốn
        anhXa();
        return view;
    }

    public static Profile_Fragment newInstance(UserData userData) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putParcelable("USER_DATA_EXTRA", userData);
        fragment.setArguments(args);
        return fragment;
    }

    private void anhXa() {
        //btn
        btn_login = view.findViewById(R.id.btn_login);

        txtname = view.findViewById(R.id.nameText);
        txtid = view.findViewById(R.id.idUser);
        btn_sigup = view.findViewById(R.id.btn_signup);
        btninfo = view.findViewById(R.id.ln_inforAccount);
        btndoipass = view.findViewById(R.id.ln_changePass);
        btnlogout = view.findViewById(R.id.ln_logOut);

        // Kiểm tra nếu userData không null thì hiển thị chào mừng và ẩn nút đăng nhập và đăng ký
        if (userData != null && userData.getUsername() != null) {
            txtname.setText("Xin chào " + userData.getUsername());
            btn_login.setVisibility(View.GONE);
            btn_sigup.setVisibility(View.GONE);
        } else {
            txtname.setText("Bạn chưa đăng nhập");
            btnlogout.setVisibility(View.GONE);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        btn_sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Logup.class));
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userData != null) {
                    // Tạo Intent để chuyển sang Activity mới
                    Intent intent = new Intent(getActivity(), Chitiet_User.class);
                    // Truyền dữ liệu userData vào Intent
                    intent.putExtra("USER_DATA_EXTRA", userData);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(), "Bạn chưa có tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btndoipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userData != null){
                showChangePasswordDialog();
            }else{
                    Toast.makeText(getActivity(), "Chưa có tài khoản đòi đổi mật khẩu há há há", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.change_password_dialog, null);
        builder.setView(dialogView);

        EditText oldPasswordEditText = dialogView.findViewById(R.id.old_password_edit_text);
        EditText newPasswordEditText = dialogView.findViewById(R.id.new_password_edit_text);
        EditText newPasswordEditText1 = dialogView.findViewById(R.id.new_password_edit_text1);
        builder.setPositiveButton("Đổi mật khẩu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String newPassword1 = newPasswordEditText1.getText().toString();
                if(!newPassword.equals(newPassword1)){
                    Toast.makeText(getActivity(), "mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                }

               else if (!oldPassword.isEmpty() && !newPassword.isEmpty()) {
                    changePassword(userData.getId(), oldPassword, newPassword);
                }
               else{
                    Toast.makeText(getActivity(), "Cần nhập vào đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }
    private void changePassword(String userId, String oldPassword, String newPassword ) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<Void> call = apiService.changePassword(userId, oldPassword, newPassword );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Đổi mật khẩu thành công
                    Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Đổi mật khẩu thất bại
                    Toast.makeText(getActivity(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc lỗi trong quá trình gửi yêu cầu
                Toast.makeText(getActivity(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
}