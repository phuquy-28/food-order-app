package com.example.foodorderapp;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.foodorderapp.prefs.DataStoreManager;

public class ControllerApplication extends Application {

    // Đường dẫn Firebase
    private static final String FIREBASE_URL = "https://orderfoodapp-c580b-default-rtdb.firebaseio.com";
    // Biến mFirebaseDatabase chứa đối tượng Firebase Database
    private FirebaseDatabase mFirebaseDatabase;

    // Người đảm nhận: Đặng Phú Quý
    // Hàm get() trả về đối tượng ControllerApplication
    public static ControllerApplication get(Context context) {
        return (ControllerApplication) context.getApplicationContext();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreate() được gọi khi ứng dụng được khởi tạo
    // Khởi tạo Firebase Database
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        DataStoreManager.init(getApplicationContext());
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getFirebaseDatabase() trả về tham chiếu đến nhánh food của Firebase Database
    public DatabaseReference getFoodDatabaseReference() {
        return mFirebaseDatabase.getReference("/food");
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getFeedbackDatabaseReference() trả về tham chiếu đến nhánh feedback của Firebase Database
    public DatabaseReference getFeedbackDatabaseReference() {
        return mFirebaseDatabase.getReference("/feedback");
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getBookingDatabaseReference() trả về tham chiếu đến nhánh booking của Firebase Database
    public DatabaseReference getBookingDatabaseReference() {
        return mFirebaseDatabase.getReference("/order");
    }
}