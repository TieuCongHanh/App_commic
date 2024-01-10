package com.example.myapplication.FRAGMENT;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Adapter.viewpager2.CommentAdapter;
import com.example.myapplication.Comment_activity;
import com.example.myapplication.Interface.CommentInterface;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.Comment;
import com.example.myapplication.Model.UserData;
import com.example.myapplication.R;



import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class fragment_gt extends Fragment {
    private Comics commic; // Thêm biến để lưu thông tin Commic
    private UserData userData;
    private List<UserData> userDataList;
    private View view; // Khai báo biến view ở đây

    public fragment_gt() {
        // Required empty public constructor
    }

    public static fragment_gt newInstance(Comics commic, UserData userData, List<UserData> userDataList) {
        fragment_gt fragment = new fragment_gt();
        Bundle args = new Bundle();
        args.putParcelable("COMIC_EXTRA",  commic); // Đặt Commic vào arguments
        args.putParcelable("USER_DATA_EXTRA",  userData);
        // Chuyển userDataList thành mảng Parcelable[]
        Parcelable[] userDataArray = new Parcelable[userDataList.size()];
        userDataList.toArray(userDataArray);

        args.putParcelableArray("userDataList", userDataArray);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            commic = getArguments().getParcelable("COMIC_EXTRA"); // Nhận Commic từ arguments
            userData = getArguments().getParcelable("USER_DATA_EXTRA");
            userDataList = getArguments().getParcelableArrayList("userDataList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_gt, container, false);
        TextView txtDescription = view.findViewById(R.id.txtDescription);
        TextView txtAuther = view.findViewById(R.id.txtAuther);
        TextView txtYear = view.findViewById(R.id.txtYear);
        Button btnshow = view.findViewById(R.id.showcm);



        if (commic != null) {
            // Hiển thị thông tin Commic lên TextView
            txtAuther.setText(" Được dịch bởi:  " + commic.getAuthor());
            txtYear.setText(" Năm xuất bản:  " + commic.getYear() );
            txtDescription.setText(" Thông tin: "+ commic.getDescription());
        }
        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khi nút bấm được nhấn, chuyển sang CommentActivity và truyền dữ liệu
                Intent intent = new Intent(getActivity(), Comment_activity.class);
                intent.putExtra("COMIC_EXTRA", commic); // Truyền thông tin Commic
                intent.putExtra("USER_DATA_EXTRA", userData); // Truyền thông tin UserData
                if (userDataList != null && userDataList.size() > 0) {
                    intent.putParcelableArrayListExtra("userDataList", new ArrayList<>(userDataList));
                } else {
                    // Xử lý trường hợp userDataList là null hoặc không có phần tử
                    // Có thể thông báo lỗi hoặc xử lý khác tùy theo logic của bạn
                }
                startActivity(intent);
            }
        });

        return view;
    }


}