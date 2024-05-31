package com.example.foodorderapp.event;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

// Người đảm nhận: Đặng Minh Nhật
// Sự kiện cập nhật danh sách món ăn trong giỏ hàng
public class ReloadListCartEvent {
    // EventBus là thư viện giúp đơn giản hóa việc truyền thông tin giữa các thành phần
    // Hoạt động theo mô hình Publish/Subscribe
    // Một thành phần có thể gửi sự kiện và các thành phần khác sẽ lắng nghe sự kiện đó
    // EventBus sử dụng các annotation để đăng ký và hủy đăng ký sự kiện
    // Ví dụ trong file CartFragement.java
    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onMessageEvent(ReloadListCartEvent event) {
    //        displayListFoodInCart();
    //    }
    // Khi sự kiện ReloadListCartEvent được gửi, hàm onMessageEvent sẽ được gọi
    // Hàm onMessageEvent sẽ gọi hàm displayListFoodInCart() để cập nhật danh sách món ăn trong giỏ hàng
}
