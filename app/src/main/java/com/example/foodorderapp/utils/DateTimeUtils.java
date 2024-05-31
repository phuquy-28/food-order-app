package com.example.foodorderapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// Người đảm nhận: Đặng Minh Nhật
// Class này chứa các hàm xử lý về thời gian
public class DateTimeUtils {

    private static final String DEFAULT_FORMAT_DATE = "dd-MM-yyyy,hh:mm a";
    private static final String DEFAULT_FORMAT_DATE_2 = "dd/MM/yyyy";

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm chuyển đổi thời gian từ dạng timestamp sang dạng ngày tháng năm giờ phút giây
    public static String convertTimeStampToDate(long timeStamp) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE, Locale.ENGLISH);
            Date date = (new Date(timeStamp));
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm chuyển đổi thời gian từ timestamp sang dạng ngày tháng năm
    public static String convertTimeStampToDate_2(long timeStamp) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATE_2, Locale.ENGLISH);
            Date date = (new Date(timeStamp));
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm chuyển đổi thời gian từ dạng ngày tháng năm sang timestamp
    public static String convertDate2ToTimeStamp(String strDate) {
        String result = "";
        if (strDate != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT_DATE_2, Locale.ENGLISH);
                Date date = format.parse(strDate);
                if (date != null) {
                    Long timestamp = date.getTime() / 1000;
                    result = String.valueOf(timestamp);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
