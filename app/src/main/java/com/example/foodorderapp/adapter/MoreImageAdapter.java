package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ItemMoreImageBinding;
import com.example.foodorderapp.model.Image;
import com.example.foodorderapp.utils.GlideUtils;

import java.util.List;

public class MoreImageAdapter extends RecyclerView.Adapter<MoreImageAdapter.MoreImageViewHolder> {

    private final List<Image> mListImages; // Danh sách ảnh

    public MoreImageAdapter(List<Image> mListImages) {
        this.mListImages = mListImages;
    }

    @NonNull
    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Tạo view holder từ layout item_more_image
    public MoreImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMoreImageBinding itemMoreImageBinding = ItemMoreImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MoreImageViewHolder(itemMoreImageBinding);
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Hiển thị dữ liệu ảnh lên view holder
    public void onBindViewHolder(@NonNull MoreImageViewHolder holder, int position) {
        Image image = mListImages.get(position);
        if (image == null) {
            return;
        }
        // Tải ảnh lên bằng GlideUtils
        GlideUtils.loadUrl(image.getUrl(), holder.mItemMoreImageBinding.imageFood);
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Trả về số lượng ảnh
    public int getItemCount() {
        return null == mListImages ? 0 : mListImages.size();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Class MoreImageViewHolder dùng để chứa view của item_more_image
    public static class MoreImageViewHolder extends RecyclerView.ViewHolder {

        private final ItemMoreImageBinding mItemMoreImageBinding;
        public MoreImageViewHolder(ItemMoreImageBinding itemMoreImageBinding) {
            super(itemMoreImageBinding.getRoot());
            this.mItemMoreImageBinding = itemMoreImageBinding;
        }
    }
}
