package com.example.foodorderapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.foodorderapp.activity.OrderHistoryActivity;
import com.example.foodorderapp.activity.PaymentActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.adapter.CartAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.database.FoodDatabase;
import com.example.foodorderapp.databinding.FragmentCartBinding;
import com.example.foodorderapp.event.ReloadListCartEvent;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Order;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends BaseFragment {

    private FragmentCartBinding mFragmentCartBinding;
    private CartAdapter mCartAdapter;
    private List<Food> mListFoodCart;
    private int typePayment = 1;
    private int mAmount;

    @Nullable
    @Override
    // Người đảm nhận: Đặng Minh Nhật
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Ánh xạ và trả về giao diện fragment_cart.xml
        mFragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false);

        // Kiểm tra xem EventBus đã đăng ký chưa, nếu chưa thì đăng ký
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // Hiển thị danh sách món ăn trong giỏ hàng
        displayListFoodInCart();
        // Gắn sự kiện onClickOrderCart() khi người dùng click vào button Đặt hàng
        mFragmentCartBinding.tvOrderCart.setOnClickListener(v -> onClickOrderCart());

        return mFragmentCartBinding.getRoot();
    }

    @Override
    // Người đảm nhận: Đặng Minh Nhật
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar bằng hàm setToolBar của MainActivity
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.cart));
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm hiển thị danh sách món ăn trong giỏ hàng
    // Gọi hàm initDataFoodCart để lấy danh sách món ăn trong giỏ hàng
    private void displayListFoodInCart() {
        if (getActivity() == null) {
            return;
        }
        // Khởi tạo RecyclerView để hiển thị danh sách món ăn trong giỏ hàng
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentCartBinding.rcvFoodCart.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mFragmentCartBinding.rcvFoodCart.addItemDecoration(itemDecoration);

        // Lấy danh sách món ăn trong giỏ hàng
        initDataFoodCart();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm initDataFoodCart dùng hiển thị danh sách món ăn trong giỏ hàng
    private void initDataFoodCart() {
        mListFoodCart = new ArrayList<>();
        // Lấy danh sách món ăn trong giỏ hàng từ FoodDatabase
        mListFoodCart = FoodDatabase.getInstance(getActivity()).foodDAO().getListFoodCart();
        // Nếu danh sách món ăn trong giỏ hàng không rỗng thì hiển thị danh sách món ăn
        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            return;
        }
        // Khởi tạo Adapter để hiển thị danh sách món ăn trong giỏ hàng
        mCartAdapter = new CartAdapter(mListFoodCart, new CartAdapter.IClickListener() {
            @Override
            // Hàm clickDeteteFood dùng để xóa món ăn khỏi giỏ hàng
            public void clickDeteteFood(Food food, int position) {
                deleteFoodFromCart(food, position);
            }

            @Override
            // Hàm updateItemFood dùng để cập nhật món ăn trong giỏ hàng
            public void updateItemFood(Food food, int position) {
                // Cập nhật món ăn trong giỏ hàng
                FoodDatabase.getInstance(getActivity()).foodDAO().updateFood(food);
                mCartAdapter.notifyItemChanged(position);

                calculateTotalPrice();
            }
        });
        // Set Adapter cho RecyclerView
        mFragmentCartBinding.rcvFoodCart.setAdapter(mCartAdapter);
        // Tính tổng tiền
        calculateTotalPrice();
    }

    @SuppressLint("NotifyDataSetChanged")
    // Người đảm nhận: Đặng Minh Nhật
    // Hàm clearCart dùng để xóa tất cả món ăn trong giỏ hàng
    // Xóa tất cả món ăn trong giỏ hàng và cập nhật lại giỏ hàng
    // Gọi calculateTotalPrice() để tính lại tổng tiền
    private void clearCart() {
        if (mListFoodCart != null) {
            mListFoodCart.clear();
        }
        mCartAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm calculateTotalPrice dùng để tính tổng tiền của giỏ hàng
    // Lấy danh sách món ăn trong giỏ hàng từ FoodDatabase
    // Nếu danh sách món ăn trong giỏ hàng rỗng thì hiển thị tổng tiền là 0
    // Nếu danh sách món ăn trong giỏ hàng không rỗng thì tính tổng tiền của giỏ hàng
    private void calculateTotalPrice() {
        // Lấy danh sách món ăn trong giỏ hàng từ FoodDatabase
        List<Food> listFoodCart = FoodDatabase.getInstance(getActivity()).foodDAO().getListFoodCart();
        // Nếu danh sách rỗng thì hiển thị tổng tiền là 0
        if (listFoodCart == null || listFoodCart.isEmpty()) {
            String strZero = 0 + Constant.CURRENCY;
            mFragmentCartBinding.tvTotalPrice.setText(strZero);
            mAmount = 0;
            return;
        }

        int totalPrice = 0;
        // Tính tổng tiền của giỏ hàng
        for (Food food : listFoodCart) {
            totalPrice = totalPrice + food.getTotalPrice();
        }
        // Hiển thị tổng tiền của giỏ hàng
        String strTotalPrice = totalPrice + Constant.CURRENCY;
        mFragmentCartBinding.tvTotalPrice.setText(strTotalPrice);
        mAmount = totalPrice;
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm deleteFoodFromCart dùng để xóa món ăn khỏi giỏ hàng
    // Hiển thị dialog xác nhận xóa món ăn khỏi giỏ hàng
    // Nếu người dùng chọn xóa thì xóa món ăn khỏi giỏ hàng và cập nhật lại giỏ hàng
    // Gọi calculateTotalPrice() để tính lại tổng tiền
    private void deleteFoodFromCart(Food food, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete_food))
                .setMessage(getString(R.string.message_delete_food))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    FoodDatabase.getInstance(getActivity()).foodDAO().deleteFood(food);
                    mListFoodCart.remove(position);
                    mCartAdapter.notifyItemRemoved(position);

                    calculateTotalPrice();
                })
                .setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onClickOrderCart dùng để xử lý sự kiện khi người dùng click vào button Đặt hàng
    // Hiển thị dialog đặt hàng
    // Nếu người dùng chọn phương thức thanh toán là Zalo thì chuyển sang màn hình thanh toán
    // Nếu người dùng chọn phương thức thanh toán là Tiền mặt thì thêm hóa đơn vào Firebase Realtime Database
    public void onClickOrderCart() {
        if (getActivity() == null) {
            return;
        }
        // Kiểm tra xem giỏ hàng có rỗng không
        if (mListFoodCart == null || mListFoodCart.isEmpty()) {
            GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_no_food_in_cart));
            return;
        }
        // Tạo dialog đặt hàng
        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_order, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        // Ánh xạ các view trong dialog
        TextView tvFoodsOrder = viewDialog.findViewById(R.id.tv_foods_order);
        TextView tvPriceOrder = viewDialog.findViewById(R.id.tv_price_order);
        TextView edtNameOrder = viewDialog.findViewById(R.id.edt_name_order);
        TextView edtPhoneOrder = viewDialog.findViewById(R.id.edt_phone_order);
        TextView edtAddressOrder = viewDialog.findViewById(R.id.edt_address_order);
        TextView tvCancelOrder = viewDialog.findViewById(R.id.tv_cancel_order);
        TextView tvCreateOrder = viewDialog.findViewById(R.id.tv_create_order);
        // Spinner chọn phương thức thanh toán
        String[] items = new String[]{Constant.PAYMENT_METHOD_CASH, Constant.PAYMENT_METHOD_ZALO};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        Spinner spinner = viewDialog.findViewById(R.id.spinner_payment_method);

        // Set dữ liệu cho dialog
        tvFoodsOrder.setText(getStringListFoodsOrder());
        tvPriceOrder.setText(mFragmentCartBinding.tvTotalPrice.getText().toString());
        spinner.setAdapter(adapter);

        // Set sự kiện cho spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            // Hàm onItemSelected dùng để xử lý sự kiện khi người dùng chọn phương thức thanh toán
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.e("Selected item : ", selectedItem);
                if (selectedItem.equals(Constant.PAYMENT_METHOD_CASH)) {
                    typePayment = Constant.TYPE_PAYMENT_CASH;
                } else if (selectedItem.equals(Constant.PAYMENT_METHOD_ZALO)) {
                    typePayment = Constant.TYPE_PAYMENT_ZALO;
                }
            }

            @Override
            // Hàm onNothingSelected dùng để xử lý sự kiện khi người dùng không chọn phương thức thanh toán
            public void onNothingSelected(AdapterView<?> parent) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_nothing_selected));
            }
        });

        // Bắt sự kiện click cho button Hủy
        // Đóng dialog
        tvCancelOrder.setOnClickListener(v -> bottomSheetDialog.dismiss());
        // Bắt sự kiện click cho button Đặt hàng
        // Hiển thị dialog xác nhận đặt hàng
        // Kiểm tra thông tin người dùng nhập vào
        // Kiểm tra phương thức thanh toán
        // Nếu phương thức thanh toán là Zalo thì chuyển sang màn hình thanh toán Zalo Pay
        // Nếu phương thức thanh toán là Tiền mặt thì thêm hóa đơn vào Firebase Realtime Database
        tvCreateOrder.setOnClickListener(v -> {

            String strName = edtNameOrder.getText().toString().trim();
            String strPhone = edtPhoneOrder.getText().toString().trim();
            String strAddress = edtAddressOrder.getText().toString().trim();

            if (StringUtil.isEmpty(strName)) {
                // Hiển thị thông báo lỗi khi người dùng chưa nhập tên
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.missing_name_input));
            }
            else if (StringUtil.isEmpty(strPhone)){
                // Hiển thị thông báo lỗi khi người dùng chưa nhập số điện thoại
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.missing_phone_input));
            }
            else if (StringUtil.isEmpty(strAddress)){
                // Hiển thị thông báo lỗi khi người dùng chưa nhập địa chỉ
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.missing_address_input));
            }
            else if (strPhone.length() < 10 || strPhone.length() > 11){
                // Hiển thị thông báo lỗi khi người dùng nhập số điện thoại không hợp lệ
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.phone_invalid_input));
            }
            else {
                if (typePayment == Constant.TYPE_PAYMENT_ZALO) {
                    // Tạo intent để chuyển sang màn hình thanh toán Zalo Pay
                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                    intent.putExtra("name", strName);
                    intent.putExtra("phone", strPhone);
                    intent.putExtra("address", strAddress);
                    bottomSheetDialog.dismiss();
                    startActivityForResult(intent, Constant.REQUEST_CODE_PAYMENT);
                    return;
                }
                // Thêm hóa đơn vào Firebase Realtime Database nếu phương thức thanh toán là Tiền mặt
                long id = System.currentTimeMillis();
                String strEmail = DataStoreManager.getUser().getEmail();
                Order order = new Order(id, strName, strEmail, strPhone, strAddress,
                        mAmount, getStringListFoodsOrder(), typePayment, false);
                ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                        .child(String.valueOf(id))
                        .setValue(order, (error1, ref1) -> {
                            // Hiển thị thông báo đặt hàng thành công
                            GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_order_success));
                            GlobalFunction.hideSoftKeyboard(getActivity());
                            bottomSheetDialog.dismiss();
                            // Xóa tất cả món ăn trong giỏ hàng
                            FoodDatabase.getInstance(getActivity()).foodDAO().deleteAllFood();
                            clearCart();
                        });
            }
        });
        // Hiển thị dialog
        bottomSheetDialog.show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    // Người đảm nhận: Đặng Minh Nhật
    // Annotation @Subscribe để đăng ký nhận sự kiện ReloadListCartEvent
    // Hàm onMessageEvent dùng để nhận sự kiện ReloadListCartEvent
    // Khi nhận được sự kiện ReloadListCartEvent thì gọi hàm displayListFoodInCart()
    public void onMessageEvent(ReloadListCartEvent event) {
        displayListFoodInCart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onActivityResult dùng để nhận kết quả trả về từ màn hình thanh toán Zalo Pay
    // Nếu kết quả trả về là RESULT_OK thì hiển thị thông báo đặt hàng thành công
    // Xóa tất cả món ăn trong giỏ hàng
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                // Hiển thị thông báo đặt hàng và thanh toán thành công
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_payment_success));
                GlobalFunction.hideSoftKeyboard(getActivity());
                // Xóa tất cả món ăn trong giỏ hàng
                FoodDatabase.getInstance(getActivity()).foodDAO().deleteAllFood();
                clearCart();
            }
        }
    }
}
