package com.example.foodorderapp.model;

// Người đảm nhận: Đặng Phú Quý
// Lớp Feedback lưu thông tin phản hồi của người dùng
// Lưu thông tin tên, số điện thoại, email, bình luận, ngày gửi và số sao
public class Feedback {

    private long id;
    private String name;
    private String phone;
    private String email;
    private String comment;
    private String date;
    private double start;

    public Feedback() {
    }

    public Feedback(String name, String phone, String email, String comment, String date, double start) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.comment = comment;
        this.date = date;
        this.start = start;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }
}
