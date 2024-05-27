package com.example.foodorderapp.prefs;

import android.content.Context;

// Người đảm nhận: Đặng Phú Quý
// Lớp MySharedPreferences quản lý SharedPreferences
// Lưu giá trị vào SharedPreferences
// Lấy giá trị từ SharedPreferences
public class MySharedPreferences {
    private static final String FRUITY_DROID_PREFERENCES = "MY_PREFERENCES";
    private Context mContext;

    private MySharedPreferences() {
    }

    // Hàm khởi tạo MySharedPreferences
    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    // Hàm putStringValue() lưu giá trị vào SharedPreferences
    public void putStringValue(String key, String s) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        android.content.SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, s);
        editor.apply();
    }

    // Hàm getStringValue() lấy giá trị từ SharedPreferences
    public String getStringValue(String key) {
        android.content.SharedPreferences pref = mContext.getSharedPreferences(
                FRUITY_DROID_PREFERENCES, 0);
        return pref.getString(key, "");
    }
}
