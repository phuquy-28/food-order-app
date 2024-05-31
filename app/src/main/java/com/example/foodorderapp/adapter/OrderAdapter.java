package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemOrderBinding;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.utils.DateTimeUtils;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder; // Danh sách hóa đơn

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo Adapter quản lý danh sách hóa đơn
    public OrderAdapter(Context mContext, List<Order> mListOrder) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
    }

    @NonNull
    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Tạo view holder từ layout item_order
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new OrderViewHolder(itemOrderBinding);
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Hiển thị dữ liệu hóa đơn lên view holder
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Lấy ra hóa đơn tại vị trí position
        Order order = mListOrder.get(position);
        if (order == null) {
            // Nếu hóa đơn không tồn tại thì không làm gì cả
            return;
        }
        if (order.isCompleted()) {
            // Nếu hóa đơn đã hoàn thành thì hiển thị màu đen
            holder.mItemOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
        } else {
            // Nếu hóa đơn chưa hoàn thành thì hiển thị màu trắng
            holder.mItemOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        // Hiển thị thông tin hóa đơn lên view holder
        holder.mItemOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemOrderBinding.tvName.setText(order.getName());
        holder.mItemOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));
        // Hiển thị tổng tiền hóa đơn
        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            // Nếu hình thức thanh toán là tiền mặt thì hiển thị "Tiền mặt"
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        else if (Constant.TYPE_PAYMENT_ZALO == order.getPayment()) {
            // Nếu hình thức thanh toán là ZaloPay thì hiển thị "ZaloPay"
            paymentMethod = Constant.PAYMENT_METHOD_ZALO;
        }
        // Hiển thị hình thức thanh toán lên view holder
        holder.mItemOrderBinding.tvPayment.setText(paymentMethod);
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Trả về số lượng hóa đơn
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để giải phóng bộ nhớ khi không sử dụng nữa
    public void release() {
        mContext = null;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Class ViewHolder dùng để ánh xạ giao diện item_order.xml
    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemOrderBinding mItemOrderBinding;

        public OrderViewHolder(@NonNull ItemOrderBinding itemOrderBinding) {
            super(itemOrderBinding.getRoot());
            this.mItemOrderBinding = itemOrderBinding;
        }
    }
}
