package com.example.foodorderapp;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.foodorderapp.prefs.DataStoreManager;

public class ControllerApplication extends Application {

//    private static final String FIREBASE_URL = "https://food-order-pro-default-rtdb.firebaseio.com";
    private static final String FIREBASE_URL = "https://orderfoodapp-c580b-default-rtdb.firebaseio.com";
    private FirebaseDatabase mFirebaseDatabase;

    public static ControllerApplication get(Context context) {
        return (ControllerApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        DataStoreManager.init(getApplicationContext());
    }

    public DatabaseReference getFoodDatabaseReference() {
        return mFirebaseDatabase.getReference("/food");
    }

    public DatabaseReference getFeedbackDatabaseReference() {
        return mFirebaseDatabase.getReference("/feedback");
    }

    public DatabaseReference getBookingDatabaseReference() {
        return mFirebaseDatabase.getReference("/booking");
    }
}