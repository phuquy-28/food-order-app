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
    private final List<Food> mListFoods;

    public PaymentAdapter(List<Food> mListFoods) {
        this.mListFoods = mListFoods;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaymentBinding itemPaymentBinding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaymentAdapter.PaymentViewHolder(itemPaymentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        GlideUtils.loadUrl(food.getImage(), holder.mItemPaymentBinding.imgFoodCart);
        holder.mItemPaymentBinding.tvFoodNameCart.setText(food.getName());


        String strFoodPriceCart = food.getPrice() + Constant.CURRENCY;
        if (food.getSale() > 0) {
            strFoodPriceCart = food.getRealPrice() + Constant.CURRENCY;
        }
        holder.mItemPaymentBinding.tvFoodPriceCart.setText(strFoodPriceCart);
    }

    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final ItemPaymentBinding mItemPaymentBinding;
        public PaymentViewHolder(ItemPaymentBinding itemPaymentBinding) {
            super(itemPaymentBinding.getRoot());
            this.mItemPaymentBinding = itemPaymentBinding;
        }
    }
}
