package com.example.foodorderapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CartAdapter;
import com.example.foodorderapp.adapter.PaymentAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.database.FoodDatabase;
import com.example.foodorderapp.databinding.ActivityPaymentBinding;
import com.example.foodorderapp.databinding.FragmentCartBinding;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;
import com.example.foodorderapp.zalo.Api.CreateOrder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding mActivityPaymentBinding;
    private PaymentAdapter mPaymentAdapter;
    private List<Food> mListFoodCart;
    private int mAmount;
    String strName;
    String strPhone;
    String strAddress;
    long id;
    String strEmail;

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        // Ánh xạ giao diện activity_payment.xml
        mActivityPaymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(mActivityPaymentBinding.getRoot());

        // Code mẫu trong document tích hợp của Zalo Pay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        // Code mẫu trong document tích hợp của Zalo Pay
        // Khởi tạo SDK ZaloPay với appId và môi trường (SANDBOX)
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        getDataIntent();
        displayInfoPayment();

        // Gắn sự kiện khi click vào nút thanh toán
        // Gọi hàm requestZalo() để thực hiện thanh toán
        mActivityPaymentBinding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestZalo();
            }
        });

        // Gắn sự kiện khi click vào nút quay lại
        mActivityPaymentBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm getDataIntent() lấy dữ liệu từ Intent
    // Lấy dữ liệu tên, số điện thoại, địa chỉ từ Intent
    // Lấy thời gian hiện tại làm id
    // Lấy email từ DataStoreManager
    private void getDataIntent() {
        Intent intent = getIntent();
        strName = intent.getStringExtra("name");
        strPhone = intent.getStringExtra("phone");
        strAddress = intent.getStringExtra("address");
        id = System.currentTimeMillis();
        strEmail = DataStoreManager.getUser().getEmail();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onActivityResult được gọi khi một Activity khác gọi startActivityForResult() đến Activity hiện tại
    // Nếu requestCode trùng với REQUEST_CODE_PAYMENT thì gọi hàm getDataIntent() để lấy dữ liệu từ Intent
    // getDataIntent() lấy các dữ liệu phục vụ cho việc thanh toán
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_PAYMENT) {
            getDataIntent();
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm displayInfoPayment() hiển thị thông tin thanh toán
    // Hiển thị thông tin tên, số điện thoại, địa chỉ, phương thức thanh toán
    // Hàm displayListFoodInPayment hiển thị danh sách món ăn đã chọn để thanh toán
    private void displayInfoPayment() {
        mActivityPaymentBinding.addressTxt.setText(strAddress);
        mActivityPaymentBinding.methodTxt.setText(Constant.PAYMENT_METHOD_ZALO);
        mActivityPaymentBinding.nameTxt.setText("Họ và tên: " + strName);
        mActivityPaymentBinding.phoneTxt.setText("Số điện thoại: " + strPhone);
        displayListFoodInPayment();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm displayListFoodInPayment() hiển thị danh sách món ăn đã chọn để thanh toán
    // Sử dụng LinearLayoutManager để hiển thị danh sách món ăn ở chế độ dọc
    // Gọi hàm initDataFoodPayment() để khởi tạo danh sách món ăn đã chọn
    private void displayListFoodInPayment() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityPaymentBinding.rcvFoodPayment.setLayoutManager(linearLayoutManager);
        mActivityPaymentBinding.rcvFoodPayment.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initDataFoodPayment();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm initDataFoodPayment() khởi tạo danh sách món ăn đã chọn
    // Lấy danh sách món ăn đã chọn từ database bằng cách gọi hàm getListFoodCart() từ FoodDatabase
    // Nếu danh sách món ăn đã chọn rỗng thì không làm gì cả
    // Nếu danh sách món ăn đã chọn không rỗng thì hiển thị danh sách món ăn đã chọn lên RecyclerView
    // Gọi hàm calculateTotalPrice() để tính tổng tiền
    private void initDataFoodPayment() {
        mListFoodCart = new ArrayList<>();
        mListFoodCart = FoodDatabase.getInstance(PaymentActivity.this).foodDAO().getListFoodCart();
        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            return;
        }

        mPaymentAdapter = new PaymentAdapter(mListFoodCart);
        mActivityPaymentBinding.rcvFoodPayment.setAdapter(mPaymentAdapter);

        calculateTotalPrice();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm calculateTotalPrice() tính tổng tiền
    // Lấy danh sách món ăn đã chọn từ database bằng cách gọi hàm getListFoodCart() từ FoodDatabase
    // Nếu danh sách món ăn đã chọn rỗng thì hiển thị tổng tiền là 0
    // Nếu danh sách món ăn đã chọn không rỗng thì tính tổng tiền từ danh sách món ăn đã chọn
    // Hiển thị tổng tiền lên giao diện
    private void calculateTotalPrice() {
        List<Food> listFoodCart = FoodDatabase.getInstance(PaymentActivity.this).foodDAO().getListFoodCart();
        if (listFoodCart == null || listFoodCart.isEmpty()) {
            String strZero = 0 + Constant.CURRENCY;
            mActivityPaymentBinding.totalTxt.setText(strZero);
            mAmount = 0;
            return;
        }

        int totalPrice = 0;
        for (Food food : listFoodCart) {
            totalPrice = totalPrice + food.getTotalPrice();
        }

        String strTotalPrice = totalPrice + Constant.CURRENCY;
        mActivityPaymentBinding.totalTxt.setText(strTotalPrice);
        mAmount = totalPrice;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Code mẫu trong document tích hợp của Zalo Pay
    // Hàm requestZalo() thực hiện thanh toán qua Zalo Pay
    // Gọi hàm createOrder() từ CreateOrder để tạo đơn hàng
    // Nếu mã trả về là 1 thì lấy mã token từ đơn hàng
    // Gọi hàm payOrder() từ ZaloPaySDK để thực hiện thanh toán
    // Lưu thông tin đơn hàng vào Firebase Realtime Database
    public void requestZalo() {
        // Code mẫu trong document tích hợp của Zalo Pay
        // Tạo đơn hàng
        CreateOrder orderApi = new CreateOrder();

        try {
            // Lấy tổng tiền
            String strTotalPrice = mActivityPaymentBinding.totalTxt.getText().toString();
            strTotalPrice = strTotalPrice.replace(Constant.CURRENCY, "");
            strTotalPrice = strTotalPrice + "000";
            // Tạo đơn hàng
            JSONObject data = orderApi.createOrder(strTotalPrice); // gán giá trị tổng tiền vào đơn hàng
            String code = data.getString("return_code"); // lấy mã trả về từ đơn hàng

            // Nếu mã trả về là 1 thì thực hiện thanh toán
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "orderzpdk://app", new PayOrderListener() {
                    // Gắn sự kiện khi thanh toán thành công
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Order order = new Order(id, strName, strEmail, strPhone, strAddress,
                                        mAmount, getStringListFoodsOrder(), Constant.TYPE_PAYMENT_ZALO, false);
                                // Lưu thông tin đơn hàng vào Firebase Realtime Database
                                ControllerApplication.get(PaymentActivity.this).getBookingDatabaseReference()
                                        .child(String.valueOf(id))
                                        .setValue(order, (error1, ref1) -> {
                                            if (error1 != null) {
                                                // Nếu lỗi thì hiển thị thông báo lỗi
                                                GlobalFunction.showToastMessage(PaymentActivity.this, "Đã xảy ra lỗi, vui lòng thử lại sau!");
                                            }
                                        });

                                Intent resultIntent = new Intent();
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        });
                    }

                    // Gắn sự kiện khi thanh toán bị hủy
                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        GlobalFunction.showToastMessage(PaymentActivity.this, "Đã hủy thanh toán!");
                    }

                    // Gắn sự kiện khi thanh toán bị lỗi
                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        GlobalFunction.showToastMessage(PaymentActivity.this, "Đã xảy ra lỗi, vui lòng thử lại sau!");
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Code mẫu trong document tích hợp của Zalo Pay
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm getStringListFoodsOrder() lấy danh sách món ăn đã chọn để hiển thị lên giao diện
    // Nếu danh sách món ăn đã chọn rỗng thì trả về chuỗi rỗng
    // Nếu danh sách món ăn đã chọn không rỗng thì trả về chuỗi danh sách món ăn đã chọn
    private String getStringListFoodsOrder() {
        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            return "";
        }
        String result = "";
        for (Food food : mListFoodCart) {
            if (StringUtil.isEmpty(result)) {
                result = "- " + food.getName() + " (" + food.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + getString(R.string.quantity) + " " + food.getCount();
            } else {
                result = result + "\n" + "- " + food.getName() + " (" + food.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + getString(R.string.quantity) + " " + food.getCount();
            }
        }
        return result;
    }
}