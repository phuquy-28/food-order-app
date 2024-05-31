package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemPaymentBinding;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>{
    private final List<Food> mListFoods; // Danh sách món ăn trong giỏ hàng

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo Adapter quản lý danh sách món ăn cần thanh toán
    public PaymentAdapter(List<Food> mListFoods) {
        this.mListFoods = mListFoods;
    }

    @NonNull
    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Tạo view holder từ layout item_payment
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaymentBinding itemPaymentBinding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaymentAdapter.PaymentViewHolder(itemPaymentBinding);
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Hiển thị dữ liệu món ăn cần thanh toán lên view holder
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        // Tải ảnh lên bằng GlideUtils
        GlideUtils.loadUrl(food.getImage(), holder.mItemPaymentBinding.imgFoodCart);
        holder.mItemPaymentBinding.tvFoodNameCart.setText(food.getName());

        // Nếu món ăn đó có giảm giá thì hiển thị giá giảm, ngược lại hiển thị giá gốc
        String strFoodPriceCart = food.getPrice() + Constant.CURRENCY;
        if (food.getSale() > 0) {
            strFoodPriceCart = food.getRealPrice() + Constant.CURRENCY;
        }
        // Hiển thị giá tiền lên view holder
        holder.mItemPaymentBinding.tvFoodPriceCart.setText(strFoodPriceCart);
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Trả về số lượng món ăn cần thanh toán
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Class PaymentViewHolder dùng để chứa view của item_payment
    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final ItemPaymentBinding mItemPaymentBinding;
        public PaymentViewHolder(ItemPaymentBinding itemPaymentBinding) {
            super(itemPaymentBinding.getRoot());
            this.mItemPaymentBinding = itemPaymentBinding;
        }
    }
}
