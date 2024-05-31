package com.example.foodorderapp.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;

// Người đảm nhận: Đặng Minh Nhật
// Class này chứa các hàm xử lý về ảnh
public class GlideUtils {
    // Người đảm nhận: Đặng Minh Nhật
    // Hàm load ảnh từ url vào imageView
    public static void loadUrlBanner(String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.img_no_image);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.img_no_image)
                .dontAnimate()
                .into(imageView);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm load ảnh từ url vào imageView
    public static void loadUrl(String url, ImageView imageView) {
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.image_no_available);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.image_no_available)
                .dontAnimate()
                .into(imageView);
    }
}