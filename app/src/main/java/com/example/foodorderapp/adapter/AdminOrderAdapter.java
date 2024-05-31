package com.example.foodorderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemAdminOrderBinding;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.utils.DateTimeUtils;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder;
    private final IUpdateStatusListener mIUpdateStatusListener;

    // Người đảm nhận: Trần Quốc Phương
    // Interface để xử lý sự kiện khi người dùng click vào checkbox trạng thái
    public interface IUpdateStatusListener {
        void updateStatus(Order order);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm khởi tạo Adapter quản lý danh sách hóa đơn
    // Nhận vào Context, danh sách hóa đơn và listener để xử lý sự kiện khi người dùng click vào checkbox trạng thái
    public AdminOrderAdapter(Context mContext, List<Order> mListOrder, IUpdateStatusListener listener) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        this.mIUpdateStatusListener = listener;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để tạo ra ViewHolder để hiển thị dữ liệu hóa đơn lên RecycleView và trả về ViewHolder đó
    // Sử dụng data binding để ánh xạ giao diện item_admin_order.xml
    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOrderBinding itemAdminOrderBinding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminOrderViewHolder(itemAdminOrderBinding);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để hiển thị các thông tin của 1 item hóa đơn lên RecycleView
    // và gắn xử lý sự kiện khi người dùng click vào checkbox trạng thái
    // Nếu hóa đơn đã hoàn thành thì hiển thị màu đen, ngược lại hiển thị màu trắng
    // Gắn xử lý sự kiện updateStatus(order) khi người dùng click vào checkbox trạng thái để cập nhật trạng thái hóa đơn
    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }
        if (order.isCompleted()) {
            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
        } else {
            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        holder.mItemAdminOrderBinding.chbStatus.setChecked(order.isCompleted());
        holder.mItemAdminOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemAdminOrderBinding.tvEmail.setText(order.getEmail());
        holder.mItemAdminOrderBinding.tvName.setText(order.getName());
        holder.mItemAdminOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemAdminOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemAdminOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemAdminOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemAdminOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemAdminOrderBinding.tvPayment.setText(paymentMethod);
        holder.mItemAdminOrderBinding.chbStatus.setOnClickListener(
                v -> mIUpdateStatusListener.updateStatus(order));
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để trả về số lượng item hóa đơn
    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Class ViewHolder dùng để ánh xạ giao diện item_admin_order.xml
    public void release() {
        mContext = null;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Class ViewHolder dùng để ánh xạ giao diện item_admin_order.xml
    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminOrderBinding mItemAdminOrderBinding;
        public AdminOrderViewHolder(@NonNull ItemAdminOrderBinding itemAdminOrderBinding) {
            super(itemAdminOrderBinding.getRoot());
            this.mItemAdminOrderBinding = itemAdminOrderBinding;
        }
    }
}
