package com.example.foodorderapp.utils;

public class StringUtil {

    // Người đảm nhận: Đặng Phú Quý
    // Hàm isValidEmail() kiểm tra email hợp lệ
    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm isEmpty() kiểm tra chuỗi rỗng
    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    public static String getDoubleNumber(int number) {
        if (number < 10) {
            return "0" + number;
        } else return "" + number;
    }
}
