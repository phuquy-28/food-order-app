package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.AdminViewPagerAdapter;
import com.example.foodorderapp.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends BaseActivity {

    private ActivityAdminMainBinding mActivityAdminMainBinding;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để ánh xạ giao diện activity_admin_main.xml bằng thư viện view binding của Android
    // khởi tạo các thành phần của giao diện là ViewPager2 và BottomNavigationView
    // Tắt tính năng người dùng có thể trượt qua các trang ViewPager2 bằng cách gọi hàm setUserInputEnabled(false)
    // Khởi tạo adapter AdminViewPagerAdapter và gán cho ViewPager2
    // Gắn sự kiện khi chọn tab trên BottomNavigationView, ViewPager2 chuyển trang tương ứng
    // Gắn sự kiện khi ViewPager2 chuyển trang, BottomNavigationView chuyển trạng thái tương ứng cho tab
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminMainBinding.getRoot());

        mActivityAdminMainBinding.viewpager2.setUserInputEnabled(false);

        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(this);
        mActivityAdminMainBinding.viewpager2.setAdapter(adminViewPagerAdapter);

        mActivityAdminMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_food).setChecked(true);
                        break;
                    case 1:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;
                    case 2:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_order).setChecked(true);
                        break;
                    case 3:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;
                }
            }
        });

        mActivityAdminMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_food) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_feedback) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_order) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(2);
            }  else if (id == R.id.nav_account) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(3);
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

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để hiện và gán nội dung cho tiêu đề toolbar từ chuỗi title truyền vào
    public void setToolBar(String title) {
        mActivityAdminMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityAdminMainBinding.toolbar.tvTitle.setText(title);
    }
}