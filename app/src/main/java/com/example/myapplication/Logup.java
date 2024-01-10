package com.example.myapplication;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.Model.NotifyConfig;

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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class Logup extends AppCompatActivity {
    private EditText edtFullName, edtUserName, edtPassWord, edtEmail;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private Button btndk;
    TextView btndn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassWord = findViewById(R.id.edtPassWord);
        edtUserName = findViewById(R.id.edtUserName);
        btndk = findViewById(R.id.Logup);
        btndn = findViewById(R.id.Login);
        mSocket.connect();
        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logup.this, Login.class);
                startActivity(intent);
            }
        });
        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register("http://192.168.0.116:3000/api/user/dk");
            }
        });
        mSocket.on("new msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Logup.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data_sv_send= (String) args[0];
                        Toast.makeText(Logup.this, "Server trả về "+data_sv_send, Toast.LENGTH_SHORT).show();
                        // hiển thị noify status
                        postNotify("Thông báo từ server" , data_sv_send);
                    }
                });
            }
        });

    }

    private void register(String link) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        String urlLink = link;

        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlLink);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");

                    String username = edtUserName.getText().toString();
                    String password = edtPassWord.getText().toString();
                    String email = edtEmail.getText().toString();
                    String fullname = edtFullName.getText().toString();

                    if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullname.isEmpty()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Logup.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    if (username.length() < 6) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Logup.this, "Username phải có ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[@#$%^&+=].*")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Logup.this, "Password phải có ít nhất 6 kí tự, 1 chữ hoa và 1 kí tự đặc biệt", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!email.matches(emailPattern)) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Logup.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                    jsonObject.put("email", email);
                    jsonObject.put("fullname", fullname);

                    http.setRequestProperty("Content-Type", "application/json");
                    OutputStream outputStream = http.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bufferedWriter.append(jsonObject.toString());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    int responseCode = http.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line).append("\n");
                        }
                        reader.close();
                        inputStream.close();
                        http.disconnect();

                        String response = builder.toString();
                        if (response.equals("username_exists")) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Logup.this, "tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Logup.this, "ok", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
    void postNotify(String title, String content){
        // Khởi tạo layout cho Notify
        Notification customNotification = new NotificationCompat.Builder(Logup.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(android.R.drawable.ic_delete)
                .setContentTitle( title )
                .setContentText(content)
                .setAutoCancel(true)

                .build();
        // Khởi tạo Manager để quản lý notify
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Logup.this);

        // Cần kiểm tra quyền trước khi hiển thị notify
        if (ActivityCompat.checkSelfPermission(Logup.this,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            // Gọi hộp thoại hiển thị xin quyền người dùng
            ActivityCompat.requestPermissions(Logup.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 999999);
            Toast.makeText(Logup.this, "Chưa cấp quyền", Toast.LENGTH_SHORT).show();
            return; // thoát khỏi hàm nếu chưa được cấp quyền
        }
        // nếu đã cấp quyền rồi thì sẽ vượt qua lệnh if trên và đến đây thì hiển thị notify
        // mỗi khi hiển thị thông báo cần tạo 1 cái ID cho thông báo riêng
        int id_notiy = (int) new Date().getTime();// lấy chuỗi time là phù hợp
        //lệnh hiển thị notify
        notificationManagerCompat.notify(id_notiy , customNotification);

    }


}