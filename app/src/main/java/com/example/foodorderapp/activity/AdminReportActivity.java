package com.example.foodorderapp.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.RevenueAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.ActivityAdminReportBinding;
import com.example.foodorderapp.listener.IOnSingleClickListener;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.utils.DateTimeUtils;
import com.example.foodorderapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminReportActivity extends AppCompatActivity {

    private ActivityAdminReportBinding mActivityAdminReportBinding;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để ánh xạ giao diện activity_admin_report.xml bằng thư viện view binding của Android
    // Gọi hàm getListRevenue() để lấy danh sách doanh thu hiển thị lên Recycle View
    // Gọi hàm initToolbar() để khởi tạo Toolbar và gán sự kiện khi người dùng click vào nút back
    // Gọi hàm initListener() để khởi tạo lắng nghe sự kiện khi người dùng click chọn ngày bắt đầu và kết thúc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminReportBinding = ActivityAdminReportBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminReportBinding.getRoot());

        initToolbar();
        initListener();
        getListRevenue();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm khởi tạo Toolbar để hiện thị tiêu đề là "Doanh thu" và nút back
    // Ẩn nút giỏ hàng và gán sự kiện khi người dùng click vào nút back
    private void initToolbar() {
        mActivityAdminReportBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityAdminReportBinding.toolbar.imgCart.setVisibility(View.GONE);
        mActivityAdminReportBinding.toolbar.tvTitle.setText(getString(R.string.revenue));
        mActivityAdminReportBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm khởi tạo lắng nghe sự kiện IOnSingleClickListener khi người dùng click chọn ngày bắt đầu và kết thúc
    // Hiển thị DatePickerDialog để người dùng chọn ngày
    private void initListener() {
        mActivityAdminReportBinding.tvDateFrom.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                GlobalFunction.showDatePicker(AdminReportActivity.this,
                        mActivityAdminReportBinding.tvDateFrom.getText().toString(), date -> {
                            mActivityAdminReportBinding.tvDateFrom.setText(date);
                            getListRevenue();
                });
            }
        });
        mActivityAdminReportBinding.tvDateTo.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                GlobalFunction.showDatePicker(AdminReportActivity.this,
                        mActivityAdminReportBinding.tvDateTo.getText().toString(), date -> {
                            mActivityAdminReportBinding.tvDateTo.setText(date);
                            getListRevenue();
                });
            }
        });
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để lấy danh sách doanh thu từ Firebase Realtime Database
    // Hiển thị danh sách doanh thu lên Recycle View
    private void getListRevenue() {
        ControllerApplication.get(this).getBookingDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Order> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (canAddOrder(order)) {
                        list.add(0, order);
                    }
                }
                handleDataHistories(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để kiểm tra xem hóa đơn có thể thêm vào danh sách doanh thu không
    // Dựa vào ngày bắt đầu và kết thúc mà người dùng chọn để lấy danh sách doanh thu
    // Nếu người dùng chỉ chọn ngày bắt đầu thì lấy hóa đơn từ ngày bắt đầu đến hôm nay
    // Nếu người dùng chỉ chọn ngày kết thúc thì lấy hết hóa đơn đến ngày kết thúc
    // Nếu hóa đơn nằm trong khoảng ngày bắt đầu và kết thúc thì lấy hóa đơn đó, ngược lại không lấy
    private boolean canAddOrder(@Nullable Order order) {
        if (order == null) {
            return false;
        }
        if (!order.isCompleted()) {
            return false;
        }
        String strDateFrom = mActivityAdminReportBinding.tvDateFrom.getText().toString();
        String strDateTo = mActivityAdminReportBinding.tvDateTo.getText().toString();
        if (StringUtil.isEmpty(strDateFrom) && StringUtil.isEmpty(strDateTo)) {
            return true;
        }
        String strDateOrder = DateTimeUtils.convertTimeStampToDate_2(order.getId());
        long longOrder = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateOrder));

        if (StringUtil.isEmpty(strDateFrom) && !StringUtil.isEmpty(strDateTo)) {
            long longDateTo = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateTo));
            return longOrder <= longDateTo;
        }
        if (!StringUtil.isEmpty(strDateFrom) && StringUtil.isEmpty(strDateTo)) {
            long longDateFrom = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateFrom));
            return longOrder >= longDateFrom;
        }
        long longDateTo = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateTo));
        long longDateFrom = Long.parseLong(DateTimeUtils.convertDate2ToTimeStamp(strDateFrom));
        return longOrder >= longDateFrom && longOrder <= longDateTo;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để hiển thị danh sách doanh thu lên Recycle View
    // Tính tổng doanh thu bằng cách gọi hàm getTotalValues() và gán giá trị vào textView tvTotalValue
    private void handleDataHistories(List<Order> list) {
        if (list == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityAdminReportBinding.rcvOrderHistory.setLayoutManager(linearLayoutManager);
        RevenueAdapter revenueAdapter = new RevenueAdapter(list);
        mActivityAdminReportBinding.rcvOrderHistory.setAdapter(revenueAdapter);

        String strTotalValue = getTotalValues(list) + Constant.CURRENCY;
        mActivityAdminReportBinding.tvTotalValue.setText(strTotalValue);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để tính tổng doanh thu
    private int getTotalValues(List<Order> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Order order : list) {
            total += order.getAmount();
        }
        return total;
    }
}