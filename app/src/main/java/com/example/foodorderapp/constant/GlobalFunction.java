package com.example.foodorderapp.constant;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.AddFoodActivity;
import com.example.foodorderapp.activity.AdminMainActivity;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.listener.IGetDateListener;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class GlobalFunction {

    // Người đảm nhận: Đặng Phú Quý
    // Hàm startActivity() chuyển sang màn hình mới
    // Đặt flag FLAG_ACTIVITY_CLEAR_TOP và FLAG_ACTIVITY_NEW_TASK
    // Để xóa hết các Activity trước đó và tạo một Activity mới
    // Để tránh việc quay lại màn hình trước đó
    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm startActivity() chuyển sang màn hình mới và truyền dữ liệu
    // Đặt flag FLAG_ACTIVITY_CLEAR_TOP và FLAG_ACTIVITY_NEW_TASK
    // Để xóa hết các Activity trước đó và tạo một Activity mới
    // Để tránh việc quay lại màn hình trước đó
    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm gotoMainActivity() chuyển sang màn hình MainActivity hoặc AdminMainActivity
    // Nếu người dùng là admin thì chuyển sang AdminMainActivity ngược lại chuyển sang MainActivity
    public static void gotoMainActivity(Context context) {
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startActivity(context, AdminMainActivity.class);
        } else {
            GlobalFunction.startActivity(context, MainActivity.class);
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm hideSoftKeyboard() ẩn bàn phím
    // Ẩn bàn phím khi click vào bất kỳ vị trí nào trên màn hình
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getTextSearch() loại bỏ dấu tiếng Việt
    // Chuyển chuỗi có dấu thành chuỗi không dấu
    public static String getTextSearch(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static void showDatePicker(Context context, String currentDate, final IGetDateListener getDateListener) {
        Calendar mCalendar = Calendar.getInstance();
        int currentDay = mCalendar.get(Calendar.DATE);
        int currentMonth = mCalendar.get(Calendar.MONTH);
        int currentYear = mCalendar.get(Calendar.YEAR);
        mCalendar.set(currentYear, currentMonth, currentDay);

        if (!StringUtil.isEmpty(currentDate)) {
            String[] split = currentDate.split("/");
            currentDay = Integer.parseInt(split[0]);
            currentMonth = Integer.parseInt(split[1]);
            currentYear = Integer.parseInt(split[2]);
            mCalendar.set(currentYear, currentMonth - 1, currentDay);
        }

        DatePickerDialog.OnDateSetListener callBack = (view, year, monthOfYear, dayOfMonth) -> {
            String date = StringUtil.getDoubleNumber(dayOfMonth) + "/" +
                    StringUtil.getDoubleNumber(monthOfYear + 1) + "/" + year;
            getDateListener.getDate(date);
        };
        DatePickerDialog datePicker = new DatePickerDialog(context,
                callBack, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE));
        datePicker.show();
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static int getPositionCategory(AddFoodActivity addFoodActivity, String category) {
        String[] arrayCategory = addFoodActivity.getResources().getStringArray(R.array.category_array);
        for (int i = 0; i < arrayCategory.length; i++) {
            if (arrayCategory[i].equals(category)) {
                return i;
            }
        }
        return 0;
    }
}