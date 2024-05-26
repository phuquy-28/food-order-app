package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ItemFoodPopularBinding;
import com.example.foodorderapp.listener.IOnClickFoodItemListener;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class FoodPopularAdapter extends RecyclerView.Adapter<FoodPopularAdapter.FoodPopularViewHolder> {
    // Danh sách thức ăn
    private final List<Food> mListFoods;
    // Interface click vào thức ăn
    public final IOnClickFoodItemListener iOnClickFoodItemListener;

    public FoodPopularAdapter(List<Food> mListFoods, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListFoods = mListFoods;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    // Người đảm nhận: Đặng Phú Quý
    // Tạo view holder từ layout item_food_popular
    // Trả về một view holder chứa layout item_food_popular
    @NonNull
    @Override
    public FoodPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodPopularBinding itemFoodPopularBinding = ItemFoodPopularBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodPopularViewHolder(itemFoodPopularBinding);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hiển thị dữ liệu thức ăn lên view holder
    // Hiển thị ảnh thức ăn, giảm giá thức ăn
    // Bắt sự kiện click vào thức ăn
    @Override
    public void onBindViewHolder(@NonNull FoodPopularViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        GlideUtils.loadUrlBanner(food.getBanner(), holder.mItemFoodPopularBinding.imageFood);
        if (food.getSale() <= 0) {
            holder.mItemFoodPopularBinding.tvSaleOff.setVisibility(View.GONE);
        } else {
            holder.mItemFoodPopularBinding.tvSaleOff.setVisibility(View.VISIBLE);
            String strSale = food.getSale() + "%";
            holder.mItemFoodPopularBinding.tvSaleOff.setText(strSale);
        }
        holder.mItemFoodPopularBinding.layoutItem.setOnClickListener(v -> iOnClickFoodItemListener.onClickItemFood(food));
    }

    // Người đảm nhận: Đặng Phú Quý
    // Trả về số lượng thức ăn
    @Override
    public int getItemCount() {
        if (mListFoods != null) {
            return mListFoods.size();
        }
        return 0;
    }

    // Người đảm nhận: Đặng Phú Quý
    // Class chứa view holder của item_food_popular
    public static class FoodPopularViewHolder extends RecyclerView.ViewHolder {

        // View binding của item_food_popular
        private final ItemFoodPopularBinding mItemFoodPopularBinding;

        // Khởi tạo view holder của item_food_popular
        public FoodPopularViewHolder(@NonNull ItemFoodPopularBinding itemFoodPopularBinding) {
            super(itemFoodPopularBinding.getRoot());
            this.mItemFoodPopularBinding = itemFoodPopularBinding;
        }
    }
}
