package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemRevenueBinding;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.utils.DateTimeUtils;

import java.util.List;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder> {

    private final List<Order> mListOrder; // Danh sách hóa đơn

    // Người đảm nhận: Trần Quốc Phương
    // Hàm khởi tạo Adapter quản lý danh sách hóa đơn
    public RevenueAdapter(List<Order> mListOrder) {
        this.mListOrder = mListOrder;
    }

    @NonNull
    @Override
    // Người đảm nhận: Trần Quốc Phương
    // Tạo view holder từ layout item_revenue
    public RevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRevenueBinding itemRevenueBinding = ItemRevenueBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new RevenueViewHolder(itemRevenueBinding);
    }

    @Override
    // Người đảm nhận: Trần Quốc Phương
    // Hiển thị dữ liệu hóa đơn lên view holder
    public void onBindViewHolder(@NonNull RevenueViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        holder.mItemRevenueBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemRevenueBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate_2(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemRevenueBinding.tvTotalAmount.setText(strAmount);
    }

    @Override
    // Người đảm nhận: Trần Quốc Phương
    // Trả về số lượng hóa đơn
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Class RevenueViewHolder dùng để chứa view của item_revenue
    public static class RevenueViewHolder extends RecyclerView.ViewHolder {

        private final ItemRevenueBinding mItemRevenueBinding;

        public RevenueViewHolder(@NonNull ItemRevenueBinding itemRevenueBinding) {
            super(itemRevenueBinding.getRoot());
            this.mItemRevenueBinding = itemRevenueBinding;
        }
    }
}
