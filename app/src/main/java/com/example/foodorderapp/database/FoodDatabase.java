package com.example.foodorderapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodorderapp.model.Food;

@Database(entities = {Food.class}, version = 1)
// Người đảm nhận: Đặng Minh Nhật
// Class FoodDatabase dùng để tạo cơ sở dữ liệu Room
// RoomDatabase giúp giảm bớt công việc khi làm việc với SQLite Database
// RoomDatabase xử lý các tác vụ như tạo ra cơ sở dữ liệu nếu nó chưa tồn tại, và mở nó.
public abstract class FoodDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "food.db"; // Tên cơ sở dữ liệu

    private static FoodDatabase instance; // Instance của FoodDatabase

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo FoodDatabase
    public static synchronized FoodDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FoodDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm trả về FoodDAO
    // FoodDAO dùng để thao tác với bảng food trong cơ sở dữ liệu
    public abstract FoodDAO foodDAO();
}
