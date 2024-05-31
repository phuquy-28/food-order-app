package com.example.foodorderapp.fragment;

import androidx.fragment.app.Fragment;

// Người đảm nhận: Đặng Minh Nhật
// BaseFragment là lớp cơ sở cho các Fragment khác
public abstract class BaseFragment extends Fragment {

    @Override
    // Hàm onResume() được gọi khi Fragment hiển thị trên màn hình
    // Gọi hàm initToolbar để khởi tạo tiêu đề ban đầu cho Toolbar
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar
    protected abstract void initToolbar();
}
