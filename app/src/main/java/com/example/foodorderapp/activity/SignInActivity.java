package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.ActivitySignInBinding;
import com.example.foodorderapp.model.User;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

public class SignInActivity extends BaseActivity {

    private ActivitySignInBinding mActivitySignInBinding;

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    // Khởi tạo giao diện đăng nhập
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());

        mActivitySignInBinding.rdbUser.setChecked(true);

        // Khi click vào layoutSignUp thì chuyển sang màn hình đăng ký
        mActivitySignInBinding.layoutSignUp.setOnClickListener(
                v -> GlobalFunction.startActivity(SignInActivity.this, SignUpActivity.class));
        // Khi click vào btnSignIn thì gọi hàm onClickValidateSignIn()
        mActivitySignInBinding.btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
        // Khi click vào tvForgotPassword thì gọi hàm onClickForgotPassword()
        mActivitySignInBinding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickForgotPassword() chuyển sang màn hình quên mật khẩu
    private void onClickForgotPassword() {
        GlobalFunction.startActivity(this, ForgotPasswordActivity.class);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickValidateSignIn() kiểm tra thông tin đăng nhập
    // Nếu email hoặc password rỗng thì hiển thị thông báo lỗi
    // Nếu email không hợp lệ thì hiển thị thông báo lỗi
    // Nếu email chứa chuỗi "admin" thì kiểm tra email và password
    // Nếu thông tin hợp lệ thì gọi hàm signInUser() để đăng nhập
    private void onClickValidateSignIn() {
        String strEmail = mActivitySignInBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignInBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (mActivitySignInBinding.rdbAdmin.isChecked()) {
                if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid_admin), Toast.LENGTH_SHORT).show();
                } else {
                    signInUser(strEmail, strPassword);
                }
                return;
            }

            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signInUser(strEmail, strPassword);
            }
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm signInUser() đăng nhập người dùng
    // Sử dụng FirebaseAuth để đăng nhập với email và password
    // Nếu đăng nhập thành công thì lưu thông tin người dùng vào DataStoreManager và chuyển sang MainActivity
    // Nếu đăng nhập thất bại thì hiển thị thông báo lỗi
    private void signInUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            if (user.getEmail() != null && user.getEmail().contains(Constant.ADMIN_EMAIL_FORMAT)) {
                                userObject.setAdmin(true);
                            }
                            DataStoreManager.setUser(userObject);
                            GlobalFunction.gotoMainActivity(this);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}