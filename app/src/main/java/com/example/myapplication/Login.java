package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView btn_dangky;
    private Button btn_dangnhap;
    private ImageView showPasswordCheckBox;
    private boolean isPasswordVisible = false;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.username = findViewById(R.id.ed_userName);
        this.password = findViewById(R.id.ed_passWord);
        this.btn_dangky = findViewById(R.id.btn_dangky);
        this.btn_dangnhap = findViewById(R.id.btn_dangnhap);
        showPasswordCheckBox = findViewById(R.id.showpassword);
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        checkBox = findViewById(R.id.checkBox);
        btn_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Logup.class);
                startActivity(intent);
            }
        });

        btn_dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login("http://192.168.0.116:3000/api/user/login");
            }
        });
        showPasswordCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPasswordVisible = false;
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPasswordVisible = true;
                }
                password.setSelection(password.getText().length()); // Để di chuyển con trỏ về cuối
            }
        });

                loadSavedLogin();
    }


    private void login(String link) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        String urlLink = link;

        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlLink);
                    //mã kết nối
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    //THiết lập phương thức POST , mặc định sẽ là GET
                    http.setRequestMethod("POST");
                    //Tạo đối tượng dữ liệu gửi lên server
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", username.getText().toString());
                    jsonObject.put("password", password.getText().toString());


                    http.setRequestProperty("Content-Type", "application/json");
                    //Tạo đối tượng out dữ liệu ra khỏi ứng dụng để gửi lên server
                    OutputStream outputStream = http.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bufferedWriter.append(jsonObject.toString());
                    //Xóa bộ đệm
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    //Nhận lại dữ liệu phản hồi
                    int responseCode = http.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Xử lý dữ liệu phản hồi từ server
                        InputStream inputStream = http.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        // Giải mã dữ liệu JSON phản hồi từ server để lấy thông tin người dùng
                        JSONObject responseJson = new JSONObject(response.toString());
                        String userId = responseJson.optString("userId");
                        String returnedUsername = responseJson.optString("username");
                        String returnedPassword = responseJson.optString("password");
                        String returnedEmail = responseJson.optString("email");
                        String returnedFullname= responseJson.optString("fullname");

                        // Tạo đối tượng UserData để truyền sang màn hình Home
                        UserData userData = new UserData(returnedUsername, returnedPassword, userId,returnedFullname,returnedEmail);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Chào mừng " + returnedUsername + " đã đến với thế giới truyện tranh", Toast.LENGTH_SHORT).show();
                                saveLogin(returnedUsername, returnedPassword);
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("USER_DATA_EXTRA", userData);
                                startActivity(intent);
                                finish(); // Đóng màn hình đăng nhập
                            }
                        });
                    } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Vui lòng điền đầy đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void loadSavedLogin() {
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        username.setText(savedUsername);
        password.setText(savedPassword);
    }

    private void saveLogin(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}