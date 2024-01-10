package com.example.myapplication.Adapter.viewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.FRAGMENT.FavoriteFragment;
import com.example.myapplication.FRAGMENT.Home_Fragment;
import com.example.myapplication.FRAGMENT.Profile_Fragment;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.UserData;

import java.util.ArrayList;
import java.util.List;


public class ViewPager2Adapter extends FragmentStateAdapter {
    private UserData userData; // Biến để lưu trữ thông tin người dùng

    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity, UserData userData) {
        super(fragmentActivity);
        this.userData = userData;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return Home_Fragment.newInstance(userData); // Truyền thông tin userData vào Home_Fragment
            case 1:
                return FavoriteFragment.newInstance(userData);
            case 2:
                return Profile_Fragment.newInstance(userData); // Truyền thông tin userData vào Profile_Fragment
            default:
                return Home_Fragment.newInstance(userData); // Truyền thông tin userData vào mặc định fragment
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Vì chỉ có 3 fragment nên chỉ trả về 3
    }
}
