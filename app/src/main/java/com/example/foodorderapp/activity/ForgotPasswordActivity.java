package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityForgotPasswordBinding;
import com.example.foodorderapp.utils.StringUtil;

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding mActivityForgotPasswordBinding;

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    // Khởi tạo giao diện quên mật khẩu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(mActivityForgotPasswordBinding.getRoot());
        // Khi click vào imgBack thì thoát Activity
        mActivityForgotPasswordBinding.imgBack.setOnClickListener(v -> onBackPressed());
        // Khi click vào btnResetPassword thì gọi hàm onClickValidateResetPassword()
        mActivityForgotPasswordBinding.btnResetPassword.setOnClickListener(v -> onClickValidateResetPassword());
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickValidateResetPassword() kiểm tra thông tin email
    // Nếu email rỗng thì hiển thị thông báo lỗi
    // Nếu email không hợp lệ thì hiển thị thông báo lỗi
    // Nếu thông tin hợp lệ thì gọi hàm resetPassword() để reset mật khẩu
    private void onClickValidateResetPassword() {
        String strEmail = mActivityForgotPasswordBinding.edtEmail.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            resetPassword(strEmail);
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm resetPassword() reset mật khẩu
    // Gửi email reset mật khẩu đến email đã nhập
    private void resetPassword(String email) {
        showProgressDialog(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this,
                                getString(R.string.msg_reset_password_successfully),
                                Toast.LENGTH_SHORT).show();
                        mActivityForgotPasswordBinding.edtEmail.setText("");
                    }
                });
    }
}