package com.example.foodorderapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodorderapp.fragment.admin.AdminAccountFragment;
import com.example.foodorderapp.fragment.admin.AdminFeedbackFragment;
import com.example.foodorderapp.fragment.admin.AdminFoodFragment;
import com.example.foodorderapp.fragment.admin.AdminOrderFragment;

public class AdminViewPagerAdapter extends FragmentStateAdapter {

    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để tạo ra các trang con của ViewPager2 và trả về Fragment tương ứng với vị trí trang con của ViewPager2
    // Theo thứ tự từ trái qua phải: tab quản lý món ăn, tab quản lý đánh giá, tab quản lý đơn hàng, tab tài khoản admin
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:  // Trang quản lý món ăn
                return new AdminFoodFragment();
            case 1:  // Trang quản lý phản hồi đánh giá
                return new AdminFeedbackFragment();
            case 2:  // Trang quản lý đơn hàng
                return new AdminOrderFragment();
            case 3:  // Trang tài khoản admin
                return new AdminAccountFragment();
            default: // Trang quản lý món ăn
                return new AdminFoodFragment();
        }
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để trả về số lượng trang con của ViewPager2
    @Override
    public int getItemCount() {
        return 4;
    }
}
