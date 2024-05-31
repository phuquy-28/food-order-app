package com.example.foodorderapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.ChangePasswordActivity;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.activity.OrderHistoryActivity;
import com.example.foodorderapp.activity.SignInActivity;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentAccountBinding;
import com.example.foodorderapp.prefs.DataStoreManager;

public class AccountFragment extends BaseFragment {

    @Nullable
    @Override
    // Người đảm nhận: Đặng Phú Quý
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ánh xạ và trả về giao diện fragment_account.xml
        FragmentAccountBinding fragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false);
        // Hiển thị email người dùng
        fragmentAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());
        // Gắn sự kiện onClickSignOut() khi người dùng click vào layout đăng xuất
        fragmentAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        // Gắn sự kiện onClickChangePassword() khi người dùng click vào layout đổi mật khẩu
        fragmentAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        // Gắn sự kiện onClickOrderHistory() khi người dùng click vào layout lịch sử đơn hàng
        fragmentAccountBinding.layoutOrderHistory.setOnClickListener(v -> onClickOrderHistory());

        return fragmentAccountBinding.getRoot();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar bằng hàm setToolBar của MainActivity
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.account));
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickOrderHistory() chuyển sang màn hình lịch sử đơn hàng
    private void onClickOrderHistory() {
        GlobalFunction.startActivity(getActivity(), OrderHistoryActivity.class);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickChangePassword() chuyển sang màn hình đổi mật khẩu
    private void onClickChangePassword() {
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickSignOut() đăng xuất tài khoản
    // Đăng xuất tài khoản và chuyển sang màn hình đăng nhập
    private void onClickSignOut() {
        if (getActivity() == null) {
            return;
        }
        FirebaseAuth.getInstance().signOut();
        DataStoreManager.setUser(null);
        GlobalFunction.startActivity(getActivity(), SignInActivity.class);
        getActivity().finishAffinity();
    }
}
