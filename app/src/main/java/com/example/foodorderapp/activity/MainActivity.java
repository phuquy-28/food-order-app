package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MainViewPagerAdapter;
import com.example.foodorderapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mActivityMainBinding;

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ánh xạ giao diện activity_main.xml
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        // Khởi tạo ViewPager2 và BottomNavigationView
        mActivityMainBinding.viewpager2.setUserInputEnabled(false);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mActivityMainBinding.viewpager2.setAdapter(mainViewPagerAdapter);

        // Gắn sự kiện khi chọn tab trên BottomNavigationView, ViewPager2 chuyển trang tương ứng
        mActivityMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        // Chọn tab Food
                        mActivityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_food).setChecked(true);
                        break;

                    case 1:
                        // Chọn tab Cart
                        mActivityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_cart).setChecked(true);
                        break;

                    case 2:
                        // Chọn tab Feedback
                        mActivityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;

                    case 3:
                        // Chọn tab Account
                        mActivityMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;
                }
            }
        });

        // Gắn sự kiện khi chọn tab trên BottomNavigationView, ViewPager2 chuyển trang tương ứng
        mActivityMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_food) {
                // Chọn tab Food
                mActivityMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_cart) {
                // Chọn tab Cart
                mActivityMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_feedback) {
                // Chọn tab Feedback
                mActivityMainBinding.viewpager2.setCurrentItem(2);
            } else if (id == R.id.nav_account) {
                // Chọn tab Account
                mActivityMainBinding.viewpager2.setCurrentItem(3);
            }
            return true;
        });
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để gọi hàm showConfirmExitApp() khi người dùng nhấn nút back trên thiết bị
    @Override
    public void onBackPressed() {
        showConfirmExitApp();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để hiển thị thông báo xác nhận thoát ứng dụng
    // Nếu người dùng chọn "Đồng ý" thì ứng dụng sẽ thoát
    // Nếu người dùng chọn "Hủy bỏ" thì ứng dụng sẽ không làm gì cả
    private void showConfirmExitApp() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive((dialog, which) -> finishAffinity())
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để hiển thị Toolbar
    // Nếu isHome = true thì ẩn Toolbar
    // Nếu isHome = false thì hiển thị Toolbar với tiêu đề title
    public void setToolBar(boolean isHome, String title) {
        if (isHome) {
            mActivityMainBinding.toolbar.layoutToolbar.setVisibility(View.GONE);
            return;
        }
        mActivityMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityMainBinding.toolbar.tvTitle.setText(title);
    }
}