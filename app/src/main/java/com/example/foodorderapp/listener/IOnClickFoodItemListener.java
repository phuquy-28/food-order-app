package com.example.foodorderapp.listener;

import com.example.foodorderapp.model.Food;

// Người đảm nhận: Đặng Minh Nhật
// Interface IOnClickFoodItemListener để bắt sự kiện click vào món ăn
public interface IOnClickFoodItemListener {
    void onClickItemFood(Food food);
}
