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

    private ActivitySplashBinding mActivitySplashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySplashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mActivitySplashBinding.getRoot());

        initUi();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::goToNextActivity, 2000);
    }

    private void initUi() {
        mActivitySplashBinding.tvAboutUsTitle.setText(AboutUsConfig.ABOUT_US_TITLE);
        mActivitySplashBinding.tvAboutUsSlogan.setText(AboutUsConfig.ABOUT_US_SLOGAN);
    }

    private void goToNextActivity() {
        if (DataStoreManager.getUser() != null && !StringUtil.isEmpty(DataStoreManager.getUser().getEmail())) {
            GlobalFunction.gotoMainActivity(this);
        } else {
            GlobalFunction.startActivity(this, SignInActivity.class);
        }
        finish();
    }
}