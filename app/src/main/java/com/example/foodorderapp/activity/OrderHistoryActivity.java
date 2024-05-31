package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorderapp.constant.GlobalFunction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.OrderAdapter;
import com.example.foodorderapp.databinding.ActivityOrderHistoryBinding;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.prefs.DataStoreManager;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends BaseActivity {

    private ActivityOrderHistoryBinding mActivityOrderHistoryBinding;
    private List<Order> mListOrder;
    private OrderAdapter mOrderAdapter;

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ánh xạ giao diện activity_order_history.xml
        mActivityOrderHistoryBinding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(mActivityOrderHistoryBinding.getRoot());

        initToolbar(); // Khởi tạo toolbar
        initView(); // Khởi tạo RecyclerView để hiển thị danh sách hóa đơn
        getListOrders(); // Lấy danh sách hóa đơn từ Firebase Realtime Database
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để khởi tạo toolbar (bao gồm nút back, tiêu đề và nút giỏ hàng)
    // Gắn sự kiện khi click vào nút back, sẽ thoát khỏi Activity hiện tại
    private void initToolbar() {
        mActivityOrderHistoryBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityOrderHistoryBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityOrderHistoryBinding.toolbar.tvTitle.setText(getString(R.string.order_history));

        mActivityOrderHistoryBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để khởi tạo RecyclerView để hiển thị danh sách hóa đơn ở chế độ dọc (LinearLayoutManager)
    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityOrderHistoryBinding.rcvOrderHistory.setLayoutManager(linearLayoutManager);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm getListOrders() lấy danh sách hóa đơn từ Firebase Realtime Database
    // Nếu danh sách hóa đơn có chứa hóa đơn thì hiển thị lên RecyclerView
    public void getListOrders() {
        // Lấy danh sách hóa đơn từ Firebase Realtime Database
        ControllerApplication.get(this).getBookingDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mListOrder != null) {
                            mListOrder.clear();
                        } else {
                            mListOrder = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Order order = dataSnapshot.getValue(Order.class);
                            if (order != null) {
                                String strEmail = DataStoreManager.getUser().getEmail();
                                if (strEmail.equalsIgnoreCase(order.getEmail())) {
                                    mListOrder.add(0, order);
                                }
                            }
                        }
                        mOrderAdapter = new OrderAdapter(OrderHistoryActivity.this, mListOrder);
                        mActivityOrderHistoryBinding.rcvOrderHistory.setAdapter(mOrderAdapter);
                    }

                    // Nếu có lỗi xảy ra thì hiển thị thông báo lỗi
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        GlobalFunction.showToastMessage(OrderHistoryActivity.this, error.getMessage());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOrderAdapter != null) {
            mOrderAdapter.release();
        }
    }
}