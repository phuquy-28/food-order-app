package com.example.foodorderapp.fragment.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.AdminMainActivity;
import com.example.foodorderapp.adapter.AdminOrderAdapter;
import com.example.foodorderapp.databinding.FragmentAdminOrderBinding;
import com.example.foodorderapp.fragment.BaseFragment;
import com.example.foodorderapp.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderFragment extends BaseFragment {

    private FragmentAdminOrderBinding mFragmentAdminOrderBinding;
    private List<Order> mListOrder;
    private AdminOrderAdapter mAdminOrderAdapter;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm liên kết và trả về giao diện fragment_admin_order.xml
    // Gọi hàm initToolbar để khởi tạo tiêu đề ban đầu cho Toolbar
    // Gọi hàm getListOrders để lấy danh sách hóa đơn từ Firebase Realtime Database để load lên RecyclerView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentAdminOrderBinding = FragmentAdminOrderBinding.inflate(inflater, container, false);
        initView();
        getListOrders();
        return mFragmentAdminOrderBinding.getRoot();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar bằng hàm setToolBar của AdminMainActivity
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.order));
        }
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo RecyclerView và gắn Adapter vào để hiển thị danh sách hóa đơn ở chế độ dọc (LinearLayoutManager)
    // Khởi tạo danh sách hóa đơn rỗng và Adapter AdminOrderAdapter
    // Gắn cho adapter sự kiện handleUpdateStatusOrder khi người dùng click vào checkbox trạng thái để cập nhật trạng thái hóa đơn
    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminOrderBinding.rcvOrder.setLayoutManager(linearLayoutManager);
        mListOrder = new ArrayList<>();
        mAdminOrderAdapter = new AdminOrderAdapter(getActivity(), mListOrder,
                this::handleUpdateStatusOrder);
        mFragmentAdminOrderBinding.rcvOrder.setAdapter(mAdminOrderAdapter);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để lấy danh sách hóa đơn từ Firebase Realtime Database để load lên RecyclerView
    public void getListOrders() {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    // Hàm dùng để thêm hóa đơn vào danh sách hóa đơn
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null || mAdminOrderAdapter == null) {
                            return;
                        }
                        mListOrder.add(0, order);
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }
                    // Hàm dùng để cập nhật hóa đơn trong danh sách hóa đơn
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mAdminOrderAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListOrder.size(); i++) {
                            if (order.getId() == mListOrder.get(i).getId()) {
                                mListOrder.set(i, order);
                                break;
                            }
                        }
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }
                    // Hàm dùng để xóa hóa đơn khỏi danh sách hóa đơn
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mAdminOrderAdapter == null) {
                            return;
                        }
                        for (Order orderObject : mListOrder) {
                            if (order.getId() == orderObject.getId()) {
                                mListOrder.remove(orderObject);
                                break;
                            }
                        }
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để cập nhật trạng thái hóa đơn lên Firebase Realtime Database
    private void handleUpdateStatusOrder(Order order) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                .child(String.valueOf(order.getId())).child("completed").setValue(!order.isCompleted());
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để giải phóng bộ nhớ khi Fragment bị hủy
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdminOrderAdapter != null) {
            mAdminOrderAdapter.release();
        }
    }
}
