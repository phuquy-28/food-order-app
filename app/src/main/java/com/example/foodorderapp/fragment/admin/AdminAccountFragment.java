package com.example.foodorderapp.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.AdminMainActivity;
import com.example.foodorderapp.activity.AdminReportActivity;
import com.example.foodorderapp.activity.ChangePasswordActivity;
import com.example.foodorderapp.activity.SignInActivity;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentAdminAccountBinding;
import com.example.foodorderapp.fragment.BaseFragment;
import com.example.foodorderapp.prefs.DataStoreManager;

public class AdminAccountFragment extends BaseFragment {

    // Người đảm nhận: Trần Quốc Phương
    // Hàm liên kết và trả về giao diện fragment_admin_account.xml
    // Gắn xử lý sự kiện onClickReport() khi người dùng click vào layout báo cáo
    // Gắn xử lý sự kiện onClickSignOut() khi người dùng click vào layout đăng xuất
    // Gắn xử lý sự kiện onClickChangePassword() khi người dùng click vào layout đổi mật khẩu
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentAdminAccountBinding fragmentAdminAccountBinding = FragmentAdminAccountBinding.inflate(inflater, container, false);
        fragmentAdminAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());
        fragmentAdminAccountBinding.layoutReport.setOnClickListener(v -> onClickReport());
        fragmentAdminAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        fragmentAdminAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        return fragmentAdminAccountBinding.getRoot();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar bằng hàm setToolBar của AdminMainActivity
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.account));
        }
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm xử lý sự kiện khi người dùng click vào layout báo cáo
    // Chuyển sang activity AdminReportActivity báo cáo doanh thu
    private void onClickReport() {
        GlobalFunction.startActivity(getActivity(), AdminReportActivity.class);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm xử lý sự kiện khi người dùng click vào layout đổi mật khẩu
    // Chuyển sang activity ChangePasswordActivity để đổi mật khẩu
    private void onClickChangePassword() {
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm xử lý sự kiện khi người dùng click vào layout đăng xuất
    // Đăng xuất tài khoản và chuyển sang activity SignInActivity
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
