package com.example.foodorderapp.prefs;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.example.foodorderapp.model.User;
import com.example.foodorderapp.utils.StringUtil;

// Người đảm nhận: Đặng Phú Quý
// Lớp DataStoreManager quản lý dữ liệu người dùng
// Lưu thông tin người dùng vào SharedPreferences
// Lấy thông tin người dùng từ SharedPreferences
public class DataStoreManager {

    public static final String PREF_USER_INFOR = "PREF_USER_INFOR";

    private static DataStoreManager instance;
    private MySharedPreferences sharedPreferences;

    // Hàm init() khởi tạo DataStoreManager
    public static void init(Context context) {
        instance = new DataStoreManager();
        instance.sharedPreferences = new MySharedPreferences(context);
    }

    // Hàm getInstance() trả về instance của DataStoreManager
    public static DataStoreManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalStateException("Not initialized");
        }
    }

    // Hàm setUser() lưu thông tin người dùng vào SharedPreferences dưới dạng chuỗi JSON
    public static void setUser(@Nullable User user) {
        String jsonUser = "";
        if (user != null) {
            jsonUser = user.toJSon();
        }
        DataStoreManager.getInstance().sharedPreferences.putStringValue(PREF_USER_INFOR, jsonUser);
    }

    // Hàm getUser() lấy thông tin người dùng từ SharedPreferences
    // Nếu không có thông tin người dùng thì trả về một đối tượng User rỗng
    public static User getUser() {
        String jsonUser = DataStoreManager.getInstance().sharedPreferences.getStringValue(PREF_USER_INFOR);
        if (!StringUtil.isEmpty(jsonUser)) {
            return new Gson().fromJson(jsonUser, User.class);
        }
        return new User();
    }
}
