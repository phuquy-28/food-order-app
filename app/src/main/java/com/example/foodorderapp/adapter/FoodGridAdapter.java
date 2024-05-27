package com.example.foodorderapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemFoodGridBinding;
import com.example.foodorderapp.listener.IOnClickFoodItemListener;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class FoodGridAdapter extends RecyclerView.Adapter<FoodGridAdapter.FoodGridViewHolder> {

    // Danh sách thức ăn
    private final List<Food> mListFoods;
    // Interface click vào thức ăn
    public final IOnClickFoodItemListener iOnClickFoodItemListener;

    public FoodGridAdapter(List<Food> mListFoods, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListFoods = mListFoods;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    // Người đảm nhận: Đặng Phú Quý
    // Tạo view holder từ layout item_food_grid
    // Trả về một view holder chứa layout item_food_grid
    @NonNull
    @Override
    public FoodGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFoodGridBinding itemFoodGridBinding = ItemFoodGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodGridViewHolder(itemFoodGridBinding);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hiển thị dữ liệu thức ăn lên view holder
    // Hiển thị ảnh thức ăn, giá thức ăn, tên thức ăn, giảm giá thức ăn
    // Bắt sự kiện click vào thức ăn
    @Override
    public void onBindViewHolder(@NonNull FoodGridViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        GlideUtils.loadUrl(food.getImage(), holder.mItemFoodGridBinding.imgFood);
        if (food.getSale() <= 0) {
            holder.mItemFoodGridBinding.tvSaleOff.setVisibility(View.GONE);
            holder.mItemFoodGridBinding.tvPrice.setVisibility(View.GONE);

            String strPrice = food.getPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPriceSale.setText(strPrice);
        } else {
            holder.mItemFoodGridBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.mItemFoodGridBinding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = food.getSale() + "%";
            holder.mItemFoodGridBinding.tvSaleOff.setText(strSale);

            String strOldPrice = food.getPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPrice.setText(strOldPrice);
            holder.mItemFoodGridBinding.tvPrice.setPaintFlags(holder.mItemFoodGridBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = food.getRealPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPriceSale.setText(strRealPrice);
        }
        holder.mItemFoodGridBinding.tvFoodName.setText(food.getName());

        // Bắt sự kiện click vào thức ăn
        holder.mItemFoodGridBinding.layoutItem.setOnClickListener(v -> iOnClickFoodItemListener.onClickItemFood(food));
    }

    // Người đảm nhận: Đặng Phú Quý
    // Trả về số lượng thức ăn
    // Nếu danh sách thức ăn null thì trả về 0, ngược lại trả về số lượng thức ăn
    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Class chứa view holder của item_food_grid
    public static class FoodGridViewHolder extends RecyclerView.ViewHolder {
        // Binding layout item_food_grid
        private final ItemFoodGridBinding mItemFoodGridBinding;

        // Khởi tạo view holder từ layout item_food_grid
        public FoodGridViewHolder(ItemFoodGridBinding itemFoodGridBinding) {
            super(itemFoodGridBinding.getRoot());
            this.mItemFoodGridBinding = itemFoodGridBinding;
        }
    }
}
