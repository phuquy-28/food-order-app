package com.example.foodorderapp.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.foodorderapp.R;

// Người đảm nhận: Đặng Minh Nhật
// Class này dùng để hiển thị ảnh với tỉ lệ khung hình cố định
public class AspectRatioNoRadiusImageView extends AppCompatImageView {
    public static final int MEASUREMENT_WIDTH = 0;
    public static final int MEASUREMENT_HEIGHT = 1;

    private static final float DEFAULT_ASPECT_RATIO = 1f;
    private static final boolean DEFAULT_ASPECT_RATIO_ENABLED = false;
    private static final int DEFAULT_DOMINANT_MEASUREMENT = MEASUREMENT_WIDTH;

    private float aspectRatio;
    private boolean aspectRatioEnabled;
    private int dominantMeasurement;

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo
    public AspectRatioNoRadiusImageView(Context context) {
        this(context, null);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo
    public AspectRatioNoRadiusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm khởi tạo
    // loadStateFromAttrs() lấy giá trị từ attrs
    public AspectRatioNoRadiusImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadStateFromAttrs(attrs);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm lấy giá trị từ attrs
    // Hàm này sử dụng để lấy giá trị từ attrs
    @SuppressLint("CustomViewStyleable")
    private void loadStateFromAttrs(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray a = null;
        try {
            a = getContext().obtainStyledAttributes(attributeSet, R.styleable.AspectRatioView);
            aspectRatio = a.getFloat(R.styleable.AspectRatioView_aspectRatio, DEFAULT_ASPECT_RATIO);
            aspectRatioEnabled = a.getBoolean(R.styleable.AspectRatioView_aspectRatioEnabled,
                    DEFAULT_ASPECT_RATIO_ENABLED);
            dominantMeasurement = a.getInt(R.styleable.AspectRatioView_dominantMeasurement,
                    DEFAULT_DOMINANT_MEASUREMENT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm set tỉ lệ khung hình
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!aspectRatioEnabled) return;

        int newWidth;
        int newHeight;
        switch (dominantMeasurement) {
            case MEASUREMENT_WIDTH:
                newWidth = getMeasuredWidth();
                newHeight = (int) (newWidth * aspectRatio);
                break;

            case MEASUREMENT_HEIGHT:
                newHeight = getMeasuredHeight();
                newWidth = (int) (newHeight * aspectRatio);
                break;

            default:
                throw new IllegalStateException("Unknown measurement with ID " + dominantMeasurement);
        }

        setMeasuredDimension(newWidth, newHeight);
    }
}
