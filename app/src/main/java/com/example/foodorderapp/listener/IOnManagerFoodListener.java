package com.example.foodorderapp.listener;

import com.example.foodorderapp.model.Food;

public interface IOnManagerFoodListener {
    void onClickUpdateFood(Food food);
    void onClickDeleteFood(Food food);
}
