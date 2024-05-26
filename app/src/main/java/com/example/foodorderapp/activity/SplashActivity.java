package com.example.foodorderapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.foodorderapp.constant.AboutUsConfig;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.ActivitySplashBinding;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    // Biến mActivitySplashBinding chứa giao diện của Activity
    private ActivitySplashBinding mActivitySplashBinding;

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    // Hiển thị tiêu đề và slogan của ứng dụng
    // Sau 2 giây chuyển sang màn hình chính
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mActivitySplashBinding.getRoot());

        initUi();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::goToNextActivity, 2000);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm initUi() khởi tạo giao diện
    // Hiển thị tiêu đề và slogan của ứng dụng
    private void initUi() {
        mActivitySplashBinding.tvAboutUsTitle.setText(AboutUsConfig.ABOUT_US_TITLE);
        mActivitySplashBinding.tvAboutUsSlogan.setText(AboutUsConfig.ABOUT_US_SLOGAN);
    }


    // Người đảm nhận: Đặng Phú Quý
    // Hàm goToNextActivity() chuyển sang màn hình chính
    // Nếu người dùng đã đăng nhập thì chuyển sang MainActivity
    // Ngược lại chuyển sang SignInActivity
    private void goToNextActivity() {
        if (DataStoreManager.getUser() != null && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())) {
            GlobalFunction.gotoMainActivity(this);
        } else {
            GlobalFunction.startActivity(this, SignInActivity.class);
        }
        finish();
    }
}