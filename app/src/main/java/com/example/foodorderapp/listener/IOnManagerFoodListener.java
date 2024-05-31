package com.example.foodorderapp.listener;

import com.example.foodorderapp.model.Food;

// Người đảm nhận: Đặng Minh Nhật
// Interface IOnManagerFoodListener để bắt sự kiện click vào nút cập nhật và xóa món ăn
public interface IOnManagerFoodListener {
    void onClickUpdateFood(Food food);
    void onClickDeleteFood(Food food);
}
