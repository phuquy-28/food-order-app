package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.ActivitySignUpBinding;
import com.example.foodorderapp.model.User;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding mActivitySignUpBinding;

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignUpBinding.getRoot());

        // Khi click vào imgBack thì thoát Activity
        mActivitySignUpBinding.imgBack.setOnClickListener(v -> onBackPressed());
        // Khi click vào layoutSignIn thì finish Activity dể quay lại màn hình đăng nhập
        mActivitySignUpBinding.layoutSignIn.setOnClickListener(v -> finish());
        // Khi click vào btnSignUp thì gọi hàm onClickValidateSignUp()
        mActivitySignUpBinding.btnSignUp.setOnClickListener(v -> onClickValidateSignUp());
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onClickValidateSignUp() kiểm tra thông tin đăng ký
    // Nếu email hoặc password rỗng thì hiển thị thông báo lỗi
    // Nếu email không hợp lệ thì hiển thị thông báo lỗi
    // Nếu email chứa chuỗi "admin" thì hiển thị thông báo lỗi
    // Nếu thông tin hợp lệ thì gọi hàm signUpUser() để đăng ký
    private void onClickValidateSignUp() {
        String strEmail = mActivitySignUpBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignUpBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(SignUpActivity.this, getString(R.string.msg_email_invalid_user), Toast.LENGTH_SHORT).show();
            } else {
                signUpUser(strEmail, strPassword);
            }
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm signUpUser() đăng ký người dùng
    // Sử dụng FirebaseAuth để tạo tài khoản mới với email và password
    // Nếu đăng ký thành công thì lưu thông tin người dùng vào DataStoreManager và chuyển sang MainActivity
    // Nếu đăng ký thất bại thì hiển thị thông báo lỗi
    private void signUpUser(String email, String password) {
        showProgressDialog(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            User userObject = new User(user.getEmail(), password);
                            DataStoreManager.setUser(userObject);
                            GlobalFunction.gotoMainActivity(this);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, getString(R.string.msg_sign_up_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}