package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.myapplication.Adapter.viewpager2.ViewPager2Adapter;
import com.example.myapplication.Model.Comics;
import com.example.myapplication.Model.UserData;

import java.util.List;

public class Home extends AppCompatActivity {
    private Toolbar Tbr;
    private ViewPager2 viewPager2;
    private String username;
    private String password;
    UserData userData;
    private String userId; // Biến để lưu thông tin người dùng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Lấy dữ liệu username từ Intent
        Intent intent = getIntent();
        if (intent != null) {
             userData = intent.getParcelableExtra("USER_DATA_EXTRA");
            if (userData != null) {
                username = userData.getUsername();
                password = userData.getPassword();
                userId = userData.getId();
            }
        }


        Tbr = findViewById(R.id.id_tollBar);
        viewPager2 = findViewById(R.id.view_pager2);

        // Thiết lập Toolbar
        setSupportActionBar(Tbr);
        Tbr.setTitle("Comic Manager");
        Tbr.setTitleTextColor(Color.WHITE);

        // Thiết lập ViewPager
        ViewPager2Adapter adapter = new ViewPager2Adapter(this, userData);
        viewPager2.setAdapter(adapter);

        // Thiết lập Bottom Navigation
        AHBottomNavigation bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.home_item, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.baseline_favorite_24, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.presonal_info, R.color.color_tab_3);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);


//// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);


// Enable / disable item & set disable color
        bottomNavigation.enableItemAtPosition(3);
        bottomNavigation.disableItemAtPosition(3);
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                Fragment fragment;
                switch (position){
                    case 0:
                        viewPager2.setCurrentItem(0);
                        Tbr.setTitle("Comic Manager");
                        Tbr.setTitleTextColor(Color.WHITE);
                        Tbr.setNavigationIcon(R.drawable.home_item);

                        break;
                    case 1:
                        viewPager2.setCurrentItem(1);
                        Tbr.setTitle("Yêu Thích");
                        Tbr.setTitleTextColor(Color.WHITE);
                        Tbr.setNavigationIcon(R.drawable.baseline_favorite_24);
                        break;
                    case 2:
                        viewPager2.setCurrentItem(2);
                        Tbr.setTitle("Tài Khoản");
                        Tbr.setTitleTextColor(Color.WHITE);
                        Tbr.setNavigationIcon(R.drawable.account_item);
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }
}