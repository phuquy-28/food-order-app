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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mActivityPaymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(mActivityPaymentBinding.getRoot());

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        getDataIntent();
        displayInfoPayment();

        mActivityPaymentBinding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestZalo();
            }
        });
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        strName = intent.getStringExtra("name");
        strPhone = intent.getStringExtra("phone");
        strAddress = intent.getStringExtra("address");
        id = System.currentTimeMillis();
        strEmail = DataStoreManager.getUser().getEmail();
    }

    private void displayInfoPayment() {
        mActivityPaymentBinding.textView24.setText(strAddress);
        mActivityPaymentBinding.textView26.setText(Constant.PAYMENT_METHOD_ZALO);
        mActivityPaymentBinding.textView27.setText("Họ và tên: " + strName);
        mActivityPaymentBinding.textView28.setText("Số điện thoại: " + strPhone);
        displayListFoodInPayment();
    }

    private void displayListFoodInPayment() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityPaymentBinding.view3.setLayoutManager(linearLayoutManager);
        mActivityPaymentBinding.view3.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initDataFoodPayment();
    }

    private void initDataFoodPayment() {
        mListFoodCart = new ArrayList<>();
        mListFoodCart = FoodDatabase.getInstance(PaymentActivity.this).foodDAO().getListFoodCart();
        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            return;
        }

        mPaymentAdapter = new PaymentAdapter(mListFoodCart);
        mActivityPaymentBinding.view3.setAdapter(mPaymentAdapter);

        calculateTotalPrice();
    }

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

    public void requestZalo() {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder("10000");
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "orderzpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(PaymentActivity.this)
                                        .setTitle("Payment Success")
                                        .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();

                                Order order = new Order(id, strName, strEmail, strPhone, strAddress,
                                        mAmount, getStringListFoodsOrder(), Constant.TYPE_PAYMENT_ZALO, false);
                                ControllerApplication.get(PaymentActivity.this).getBookingDatabaseReference()
                                        .child(String.valueOf(id))
                                        .setValue(order, (error1, ref1) -> {
                                            GlobalFunction.showToastMessage(PaymentActivity.this, getString(R.string.msg_order_success));
                                            GlobalFunction.hideSoftKeyboard(PaymentActivity.this);

                                            FoodDatabase.getInstance(PaymentActivity.this).foodDAO().deleteAllFood();
                                        });

                                Intent intentGoHome = new Intent(PaymentActivity.this, OrderHistoryActivity.class);
                                startActivity(intentGoHome);
                            }
                        });
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

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