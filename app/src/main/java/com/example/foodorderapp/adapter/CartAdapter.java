package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.databinding.ItemCartBinding;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Food> mListFoods; // Danh sách món ăn trong giỏ hàng
    // Interface xử lý sự kiện khi người dùng click vào các nút xóa và cập nhật số lượng món ăn
    private final IClickListener iClickListener;

    // Người đảm nhận: Đặng Minh Nhật
    // Interface để xử lý sự kiện khi người dùng click vào các nút xóa và cập nhật số lượng món ăn
    public interface IClickListener {
        void clickDeleteFood(Food food, int position);

        void updateItemFood(Food food, int position);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo Adapter quản lý danh sách món ăn trong giỏ hàng
    // Nhận vào danh sách món ăn và listener để xử lý sự kiện khi người dùng click vào các nút xóa và cập nhật số lượng món ăn
    public CartAdapter(List<Food> mListFoods, IClickListener iClickListener) {
        this.mListFoods = mListFoods;
        this.iClickListener = iClickListener;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để tạo ra ViewHolder để hiển thị dữ liệu món ăn trong giỏ hàng lên RecycleView và trả về ViewHolder đó
    // Sử dụng data binding để ánh xạ giao diện item_cart.xml
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding itemCartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(itemCartBinding);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để hiển thị các thông tin của một món ăn trong giỏ hàng lên RecycleView
    // Gắn xử lý sự kiện khi người dùng click vào các nút cộng và trừ để cập nhật số lượng món ăn
    // Gắn xử lý sự kiện khi người dùng click vào nút xóa để xóa món ăn khỏi giỏ hàng
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Food food = mListFoods.get(position);
        if (food == null) {
            return;
        }
        // Tải ảnh lên bằng GlideUtils
        GlideUtils.loadUrl(food.getImage(), holder.mItemCartBinding.imgFoodCart);
        holder.mItemCartBinding.tvFoodNameCart.setText(food.getName());
        // Nếu món ăn đó có giảm giá thì hiển thị giá giảm, ngược lại hiển thị giá gốc
        String strFoodPriceCart = food.getPrice() + Constant.CURRENCY;
        if (food.getSale() > 0) {
            strFoodPriceCart = food.getRealPrice() + Constant.CURRENCY;
        }
        holder.mItemCartBinding.tvFoodPriceCart.setText(strFoodPriceCart);
        // Hiển thị số lượng món ăn
        holder.mItemCartBinding.tvCount.setText(String.valueOf(food.getCount()));
        // Xử lý sự kiện khi người dùng click vào nút trừ cập nhật số lượng món ăn
        holder.mItemCartBinding.tvSubtract.setOnClickListener(v -> {
            String strCount = holder.mItemCartBinding.tvCount.getText().toString();
            int count = Integer.parseInt(strCount);
            // Nếu số lượng món ăn bằng 1 thì không thể giảm nữa
            if (count <= 1) {
                return;
            }
            int newCount = count - 1;
            holder.mItemCartBinding.tvCount.setText(String.valueOf(newCount));
            // Tính tổng giá tiền của món ăn
            int totalPrice = food.getRealPrice() * newCount;
            food.setCount(newCount);
            food.setTotalPrice(totalPrice);
            // Gọi hàm updateItemFood() để cập nhật số lượng món ăn
            iClickListener.updateItemFood(food, holder.getAdapterPosition());
        });
        // Xử lý sự kiện khi người dùng click vào nút cộng cập nhật số lượng món ăn
        holder.mItemCartBinding.tvAdd.setOnClickListener(v -> {
            // Lấy số lượng món ăn hiện tại và tăng thêm 1
            int newCount = Integer.parseInt(holder.mItemCartBinding.tvCount.getText().toString()) + 1;
            // Hiển thị số lượng món ăn
            holder.mItemCartBinding.tvCount.setText(String.valueOf(newCount));
            // Tính tổng giá tiền của món ăn
            int totalPrice = food.getRealPrice() * newCount;
            food.setCount(newCount);
            food.setTotalPrice(totalPrice);
            // Gọi hàm updateItemFood() để cập nhật số lượng món ăn
            iClickListener.updateItemFood(food, holder.getAdapterPosition());
        });
        // Xử lý sự kiện khi người dùng click vào nút xóa để xóa món ăn khỏi giỏ hàng
        holder.mItemCartBinding.tvDelete.setOnClickListener(v
                -> iClickListener.clickDeleteFood(food, holder.getAdapterPosition()));
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để trả về số lượng item món ăn trong giỏ hàng
    // Nếu danh sách món ăn rỗng thì trả về 0, ngược lại trả về số lượng món ăn
    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Class ViewHolder dùng để ánh xạ giao diện item_cart.xml
    public static class CartViewHolder extends RecyclerView.ViewHolder {

        private final ItemCartBinding mItemCartBinding;
        // Hàm khởi tạo ViewHolder để ánh xạ giao diện item_cart.xml
        public CartViewHolder(ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());
            this.mItemCartBinding = itemCartBinding;
        }
    }
}
