package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ItemFeedbackBinding;
import com.example.foodorderapp.model.Feedback;
import com.example.foodorderapp.utils.DateTimeUtils;

import java.util.List;

public class AdminFeedbackAdapter extends RecyclerView.Adapter<AdminFeedbackAdapter.FeedbackViewHolder> {

    private final List<Feedback> mListFeedback;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm khởi tạo Adapter quản lý danh sách phản hồi đánh giá
    // Nhận vào danh sách phản hồi đánh giá
    public AdminFeedbackAdapter(List<Feedback> mListFeedback) {
        this.mListFeedback = mListFeedback;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để tạo ra ViewHolder để hiển thị dữ liệu đánh giá phản hồi của khách hàng lên RecycleView và trả về ViewHolder đó
    // Sử dụng data binding để ánh xạ giao diện item_feedback.xml
    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFeedbackBinding itemFeedbackBinding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FeedbackViewHolder(itemFeedbackBinding);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để hiển thị các thông tin của 1 item phản hồi đánh giá lên RecycleView
    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = mListFeedback.get(position);
        if (feedback == null) {
            return;
        }
        holder.mItemFeedbackBinding.tvEmail.setText(feedback.getEmail());
        holder.mItemFeedbackBinding.tvFeedback.setText(feedback.getComment());
        holder.mItemFeedbackBinding.tvTime.setText(DateTimeUtils.convertTimeStampToDate(feedback.getId()));
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để trả về số lượng item phản hồi đánh giá
    @Override
    public int getItemCount() {
        if (mListFeedback != null) {
            return mListFeedback.size();
        }
        return 0;
    }

    // Người đảm nhận: Trần Quốc Phương
    // Class ViewHolder dùng để ánh xạ giao diện item_feedback.xml
    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        private final ItemFeedbackBinding mItemFeedbackBinding;
        public FeedbackViewHolder(@NonNull ItemFeedbackBinding itemFeedbackBinding) {
            super(itemFeedbackBinding.getRoot());
            this.mItemFeedbackBinding = itemFeedbackBinding;
        }
    }
}
