package com.example.foodorderapp.listener;

import android.os.SystemClock;
import android.view.View;

public abstract class IOnSingleClickListener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime;

    public abstract void onSingleClick(View v);

    // Người đảm nhận: Trần Quốc Phương
    // Hàm kiểm tra thời gian giữa 2 lần click của người dùng
    // Nếu thời gian giữa 2 lần click nhỏ hơn 600ms thì không thực hiện hành động
    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;
        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        onSingleClick(v);
    }
}
