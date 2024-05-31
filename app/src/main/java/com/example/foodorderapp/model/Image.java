package com.example.foodorderapp.model;

import java.io.Serializable;

// Người đảm nhận: Đặng Minh Nhật
// Class Image để lưu thông tin ảnh
// Các thông tin bao gồm: url
public class Image implements Serializable {
    private String url;

    public Image() {
    }

    public Image(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
