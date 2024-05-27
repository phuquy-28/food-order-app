package com.example.foodorderapp.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.foodorderapp.R;

public abstract class BaseActivity extends AppCompatActivity {

    // Biến progressDialog chứa dialog progress
    protected MaterialDialog progressDialog, alertDialog;

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    // Khởi tạo dialog progress
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createProgressDialog();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm createProgressDialog() tạo dialog progress
    // Hiển thị dialog progress khi thực hiện các tác vụ cần thời gian xử lý
    public void createProgressDialog() {
        progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.waiting_message)
                .progress(true, 0)
                .build();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm showProgressDialog() hiển thị hoặc ẩn dialog progress
    // Nếu value = true thì hiển thị dialog progress
    // Nếu value = false thì ẩn dialog progress
    public void showProgressDialog(boolean value) {
        if (value) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroy();
    }
}
