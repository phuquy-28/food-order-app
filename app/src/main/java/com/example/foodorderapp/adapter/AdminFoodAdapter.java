package com.example.foodorderapp.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemAdminFoodBinding;
import com.example.foodorderapp.listener.IOnManagerFoodListener;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.AdminFoodViewHolder> {

    private final List<Food> mListFoods;
    public final IOnManagerFoodListener iOnManagerFoodListener;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm khởi tạo Adapter quản lý danh sách món ăn
    // Nhận vào danh sách món ăn và listener để xử lý sự kiện khi người dùng click vào các nút bên phải mỗi món ăn
    public AdminFoodAdapter(List<Food> mListFoods, IOnManagerFoodListener listener) {
        this.mListFoods = mListFoods;
        this.iOnManagerFoodListener = listener;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để tạo ra ViewHolder để hiển thị dữ liệu lên giao diện và trả về ViewHolder đó
    // Sử dụng data binding để ánh xạ giao diện item_admin_food.xml
    @NonNull
    @Override
    public AdminFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminFoodBinding itemAdminFoodBinding = ItemAdminFoodBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminFoodViewHolder(itemAdminFoodBinding);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để hiển thị các thông tin của một món ăn xác định lên RecycleView
    // Nếu món ăn đó không có giảm giá thì hiển thị giá gốc, ngược lại hiển thị giá giảm
    // Nếu món ăn đó có phổ biến thì hiển thị "Có" ở mục phổ biến, ngược lại hiển thị "Không"
    // Gắn xử lý sự kiện khi người dùng click vào các nút sửa, xóa và xem mô tả của món ăn đó
    @Override
    public void onBindViewHolder(@NonNull AdminFoodViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        GlideUtils.loadUrl(food.getImage(), holder.mItemAdminFoodBinding.imgFood);
        if (food.getSale() <= 0) {
            // Not Sale
            holder.mItemAdminFoodBinding.tvSaleOff.setVisibility(View.GONE);
            holder.mItemAdminFoodBinding.tvPrice.setVisibility(View.GONE);
            String strPrice = food.getPrice() + Constant.CURRENCY;
            holder.mItemAdminFoodBinding.tvPriceSale.setText(strPrice);
        } else {
            // Sale
            holder.mItemAdminFoodBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.mItemAdminFoodBinding.tvPrice.setVisibility(View.VISIBLE);
            String strSale = "Giảm " + food.getSale() + "%";
            holder.mItemAdminFoodBinding.tvSaleOff.setText(strSale);
            String strOldPrice = food.getPrice() + Constant.CURRENCY;
            holder.mItemAdminFoodBinding.tvPrice.setText(strOldPrice);
            holder.mItemAdminFoodBinding.tvPrice.setPaintFlags(holder.mItemAdminFoodBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String strRealPrice = food.getRealPrice() + Constant.CURRENCY;
            holder.mItemAdminFoodBinding.tvPriceSale.setText(strRealPrice);
        }
        holder.mItemAdminFoodBinding.tvFoodName.setText(food.getName());
        holder.mItemAdminFoodBinding.tvFoodDescription.setText(food.getDescription());
        if (food.isPopular()) {
            holder.mItemAdminFoodBinding.tvPopular.setText("Có");
        } else {
            holder.mItemAdminFoodBinding.tvPopular.setText("Không");
        }
        holder.mItemAdminFoodBinding.imgEdit.setOnClickListener(v -> iOnManagerFoodListener.onClickUpdateFood(food));
        holder.mItemAdminFoodBinding.imgDelete.setOnClickListener(v -> iOnManagerFoodListener.onClickDeleteFood(food));
        holder.mItemAdminFoodBinding.btnShowText.setOnClickListener(v -> onClickShowDecriptFood(holder,food));
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm xử lý sự kiện khi người dùng click vào nút xem mô tả của món ăn
    // Nếu mô tả đang ẩn thì hiển thị ra, nếu đang hiển thị thì ẩn đi
    private void onClickShowDecriptFood(AdminFoodViewHolder holder, Food food) {
        if (holder.mItemAdminFoodBinding.tvFoodDescription.getVisibility() == View.VISIBLE)
            holder.mItemAdminFoodBinding.tvFoodDescription.setVisibility(View.GONE);
        else if (holder.mItemAdminFoodBinding.tvFoodDescription.getVisibility() == View.GONE)
            holder.mItemAdminFoodBinding.tvFoodDescription.setVisibility(View.VISIBLE);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để trả về số lượng item trong danh sách món ăn
    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Class ViewHolder để ánh xạ và lưu trữ các View của item món ăn
    public static class AdminFoodViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminFoodBinding mItemAdminFoodBinding;
        public AdminFoodViewHolder(ItemAdminFoodBinding itemAdminFoodBinding) {
            super(itemAdminFoodBinding.getRoot());
            this.mItemAdminFoodBinding = itemAdminFoodBinding;
        }
    }
}
