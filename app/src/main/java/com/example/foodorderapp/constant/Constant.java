package com.example.foodorderapp.constant;

public interface Constant {
    // Người đảm nhận: Đặng Minh Nhật
    // Các hằng số dùng chung trong ứng dụng
    String CURRENCY = " 000 VNĐ"; // Đơn vị tiền tệ
    int TYPE_PAYMENT_CASH = 1; // Loại thanh toán tiền mặt
    int TYPE_PAYMENT_ZALO = 2; // Loại thanh toán ZaloPay
    String PAYMENT_METHOD_CASH = "Tiền mặt"; // Hình thức thanh toán tiền mặt
    String PAYMENT_METHOD_ZALO = "Thanh toán Zalo Pay"; // Hình thức thanh toán ZaloPay
    String ADMIN_EMAIL_FORMAT = "@admin.com"; // Định dạng email của admin

    // Key Intent
    String KEY_INTENT_FOOD_OBJECT = "food_object"; // Key truyền dữ liệu món ăn

    int REQUEST_CODE_PAYMENT = 10; // Request code khi chuyển sang màn hình thanh toán
}
