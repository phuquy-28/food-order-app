package com.example.foodorderapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodorderapp.model.Food;

import java.util.List;

@Dao
// Người đảm nhận: Đặng Minh Nhật
// Interface FoodDAO dùng để thao tác với bảng food trong cơ sở dữ liệu
public interface FoodDAO {

    @Insert
    // Thêm một món ăn vào giỏ hàng
    void insertFood(Food food);

    @Query("SELECT * FROM food")
    // Lấy danh sách món ăn trong giỏ hàng
    List<Food> getListFoodCart();

    @Query("SELECT * FROM food WHERE id=:id")
    // Kiểm tra món ăn có trong giỏ hàng hay không
    List<Food> checkFoodInCart(long id);

    @Delete
    // Xóa một món ăn khỏi giỏ hàng
    void deleteFood(Food food);

    @Update
    // Cập nhật thông tin món ăn trong giỏ hàng
    void updateFood(Food food);

    @Query("DELETE from food")
    // Xóa tất cả món ăn trong giỏ hàng
    void deleteAllFood();
}
