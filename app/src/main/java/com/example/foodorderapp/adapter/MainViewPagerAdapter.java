package com.example.foodorderapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.foodorderapp.fragment.AccountFragment;
import com.example.foodorderapp.fragment.CartFragment;
import com.example.foodorderapp.fragment.FeedbackFragment;
import com.example.foodorderapp.fragment.HomeFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo Adapter
    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để tạo ra các trang con của ViewPager2 và trả về Fragment tương ứng với vị trí trang con của ViewPager2
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();

            case 1:
                return new CartFragment();

            case 2:
                return new FeedbackFragment();

            case 3:
                return new AccountFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để trả về số lượng trang con của ViewPager2
    public int getItemCount() {
        return 4;
    }
}
