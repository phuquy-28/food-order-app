package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityChangePasswordBinding;
import com.example.foodorderapp.model.User;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding mActivityChangePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityChangePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(mActivityChangePasswordBinding.getRoot());

        // Khi click vào imgBack thì thoát Activity
        mActivityChangePasswordBinding.imgBack.setOnClickListener(v -> onBackPressed());
        // Khi click vào btnChangePassword thì gọi hàm onClickValidateChangePassword()
        mActivityChangePasswordBinding.btnChangePassword.setOnClickListener(v -> onClickValidateChangePassword());
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickValidateChangePassword() kiểm tra thông tin đổi mật khẩu
    // Nếu mật khẩu cũ, mật khẩu mới hoặc xác nhận mật khẩu rỗng thì hiển thị thông báo lỗi
    // Nếu mật khẩu cũ không đúng thì hiển thị thông báo lỗi
    // Nếu mật khẩu mới và xác nhận mật khẩu không trùng nhau thì hiển thị thông báo lỗi
    // Nếu mật khẩu cũ và mật khẩu mới trùng nhau thì hiển thị thông báo lỗi
    // Nếu thông tin hợp lệ thì gọi hàm changePassword() để đổi mật khẩu
    private void onClickValidateChangePassword() {
        String strOldPassword = mActivityChangePasswordBinding.edtOldPassword.getText().toString().trim();
        String strNewPassword = mActivityChangePasswordBinding.edtNewPassword.getText().toString().trim();
        String strConfirmPassword = mActivityChangePasswordBinding.edtConfirmPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strOldPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show();
        } else if (!DataStoreManager.getUser().getPassword().equals(strOldPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (!strNewPassword.equals(strConfirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (strOldPassword.equals(strNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show();
        } else {
            changePassword(strNewPassword);
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm changePassword() đổi mật khẩu
    // Đổi mật khẩu của tài khoản hiện tại
    // Hiển thị thông báo đổi mật khẩu thành công
    // Lưu mật khẩu mới vào SharedPreferences
    private void changePassword(String newPassword) {
        showProgressDialog(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this,
                                getString(R.string.msg_change_password_successfully), Toast.LENGTH_SHORT).show();
                        User userLogin = DataStoreManager.getUser();
                        userLogin.setPassword(newPassword);
                        DataStoreManager.setUser(userLogin);
                        mActivityChangePasswordBinding.edtOldPassword.setText("");
                        mActivityChangePasswordBinding.edtNewPassword.setText("");
                        mActivityChangePasswordBinding.edtConfirmPassword.setText("");
                    }
                });
    }
}