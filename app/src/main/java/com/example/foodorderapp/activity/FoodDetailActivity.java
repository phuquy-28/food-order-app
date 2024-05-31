package com.example.foodorderapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.foodorderapp.fragment.CartFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.MoreImageAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.database.FoodDatabase;
import com.example.foodorderapp.databinding.ActivityFoodDetailBinding;
import com.example.foodorderapp.event.ReloadListCartEvent;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.GlideUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FoodDetailActivity extends BaseActivity {

    private ActivityFoodDetailBinding mActivityFoodDetailBinding;
    private Food mFood;

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onCreate() được gọi khi Activity được khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ánh xạ giao diện activity_food_detail.xml bằng thư viện view binding của Android
        mActivityFoodDetailBinding = ActivityFoodDetailBinding.inflate(getLayoutInflater());
        setContentView(mActivityFoodDetailBinding.getRoot());

        getDataIntent(); // Lấy dữ liệu từ Intent được truyền từ Activity trước đó
        initToolbar(); // Khởi tạo Toolbar
        setDataFoodDetail(); // Hiển thị thông tin chi tiết món ăn
        initListener(); // Khởi tạo lắng nghe sự kiện (ví dụ như click button)
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm getDataIntent() lấy dữ liệu từ Intent được truyền từ Activity trước đó
    // Nếu có dữ liệu thì gán cho biến mFood
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFood = (Food) bundle.get(Constant.KEY_INTENT_FOOD_OBJECT);
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm initToolbar() khởi tạo Toolbar (gồm nút back, tiêu đề và giỏ hàng)
    // Gán sự kiện khi người dùng click vào nút back
    private void initToolbar() {
        mActivityFoodDetailBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityFoodDetailBinding.toolbar.imgCart.setVisibility(View.VISIBLE);
        mActivityFoodDetailBinding.toolbar.tvTitle.setText(getString(R.string.food_detail_title));

        mActivityFoodDetailBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm setDataFoodDetail() hiển thị thông tin chi tiết món ăn
    // Sử dụng GlideUtils để hiển thị hình ảnh món ăn
    // Hiển thị hình ảnh, tên món ăn, mô tả, giá, giảm giá, hình ảnh khác
    // Gọi hàm displayListMoreImages() để hiển thị danh sách hình ảnh khác
    // Gọi hàm setStatusButtonAddToCart() để hiển thị trạng thái nút thêm vào giỏ hàng
    // Nếu đã có sản phẩm trong giỏ hàng thì nút "Thêm vào giỏ hàng" sẽ chuyển thành "Đã thêm vào giỏ hàng"
    private void setDataFoodDetail() {
        if (mFood == null) {
            return;
        }

        GlideUtils.loadUrlBanner(mFood.getBanner(), mActivityFoodDetailBinding.imageFood);
        if (mFood.getSale() <= 0) {
            mActivityFoodDetailBinding.tvSaleOff.setVisibility(View.GONE);

            String strPrice = mFood.getPrice() + Constant.CURRENCY;
            mActivityFoodDetailBinding.tvPriceSale.setText(strPrice);
        } else {
            mActivityFoodDetailBinding.tvSaleOff.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + mFood.getSale() + "%";
            mActivityFoodDetailBinding.tvSaleOff.setText(strSale);

            String strRealPrice = mFood.getRealPrice() + Constant.CURRENCY;
            mActivityFoodDetailBinding.tvPriceSale.setText(strRealPrice);
        }
        mActivityFoodDetailBinding.tvFoodName.setText(mFood.getName());
        mActivityFoodDetailBinding.tvFoodDescription.setText(mFood.getDescription());

        displayListMoreImages();

        setStatusButtonAddToCart();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm displayListMoreImages() hiển thị danh sách hình ảnh khác
    // Nếu danh sách hình ảnh rỗng thì ẩn label "Hình ảnh khác"
    // Sử dụng GridLayoutManager để hiển thị danh sách hình ảnh theo dạng lưới 2 cột
    // Sử dụng MoreImageAdapter để hiển thị danh sách hình ảnh
    // Gán Adapter cho RecyclerView
    private void displayListMoreImages() {
        if (mFood.getImages() == null || mFood.getImages().isEmpty()) {
            mActivityFoodDetailBinding.tvMoreImageLabel.setVisibility(View.GONE);
            return;
        }
        mActivityFoodDetailBinding.tvMoreImageLabel.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mActivityFoodDetailBinding.rcvImages.setLayoutManager(gridLayoutManager);

        MoreImageAdapter moreImageAdapter = new MoreImageAdapter(mFood.getImages());
        mActivityFoodDetailBinding.rcvImages.setAdapter(moreImageAdapter);
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm setStatusButtonAddToCart() hiển thị trạng thái nút thêm vào giỏ hàng
    // Nếu sản phẩm đã có trong giỏ hàng thì nút "Thêm vào giỏ hàng" sẽ chuyển thành "Đã thêm vào giỏ hàng"
    // Nếu sản phẩm chưa có trong giỏ hàng thì nút "Thêm vào giỏ hàng" sẽ hiển thị bình thường
    // Nếu sản phẩm đã có trong giỏ hàng thì ẩn biểu tượng giỏ hàng
    // Nếu sản phẩm chưa có trong giỏ hàng thì hiển thị biểu tượng giỏ hàng
    private void setStatusButtonAddToCart() {
        if (isFoodInCart()) {
            mActivityFoodDetailBinding.tvAddToCart.setBackgroundResource(R.drawable.bg_gray_shape_corner_6);
            mActivityFoodDetailBinding.tvAddToCart.setText(getString(R.string.added_to_cart));
            mActivityFoodDetailBinding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
            mActivityFoodDetailBinding.toolbar.imgCart.setVisibility(View.GONE);
        } else {
            mActivityFoodDetailBinding.tvAddToCart.setBackgroundResource(R.drawable.bg_green_shape_corner_6);
            mActivityFoodDetailBinding.tvAddToCart.setText(getString(R.string.add_to_cart));
            mActivityFoodDetailBinding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.white));
            mActivityFoodDetailBinding.toolbar.imgCart.setVisibility(View.VISIBLE);
        }
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm isFoodInCart() kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    // Nếu có thì trả về true, ngược lại trả về false
    // Sử dụng FoodDatabase để kiểm tra sản phẩm đã có trong giỏ hàng chưa
    private boolean isFoodInCart() {
        List<Food> list = FoodDatabase.getInstance(this).foodDAO().checkFoodInCart(mFood.getId());
        return list != null && !list.isEmpty();
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm initListener() khởi tạo lắng nghe sự kiện
    // Khi người dùng click vào nút "Thêm vào giỏ hàng" thì gọi hàm onClickAddToCart()
    // Khi người dùng click vào biểu tượng giỏ hàng thì gọi hàm onClickAddToCart()
    private void initListener() {
        mActivityFoodDetailBinding.tvAddToCart.setOnClickListener(v -> onClickAddToCart());
        mActivityFoodDetailBinding.toolbar.imgCart.setOnClickListener(v -> onClickAddToCart());
    }

    // Người đảm nhận: Đặng Minh Nhật
    // Hàm onClickAddToCart() hiển thị dialog thêm vào giỏ hàng
    // Hiển thị hình ảnh, tên món ăn, giá, số lượng, nút trừ, nút cộng, nút hủy và nút thêm vào giỏ hàng
    // Khi người dùng click vào nút trừ thì giảm số lượng món ăn
    // Khi người dùng click vào nút cộng thì tăng số lượng món ăn
    // Khi người dùng click vào nút hủy thì đóng dialog
    // Khi người dùng click vào nút thêm vào giỏ hàng thì thêm món ăn vào giỏ hàng
    // Sau khi thêm món ăn vào giỏ hàng thì đóng dialog và gửi sự kiện ReloadListCartEvent
    public void onClickAddToCart() {
        if (isFoodInCart()) {
            return;
        }
        // Tạo dialog bottom sheet
        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_cart, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);

        // Ánh xạ các view trong dialog
        ImageView imgFoodCart = viewDialog.findViewById(R.id.img_food_cart);
        TextView tvFoodNameCart = viewDialog.findViewById(R.id.tv_food_name_cart);
        TextView tvFoodPriceCart = viewDialog.findViewById(R.id.tv_food_price_cart);
        TextView tvSubtractCount = viewDialog.findViewById(R.id.tv_subtract);
        TextView tvCount = viewDialog.findViewById(R.id.tv_count);
        TextView tvAddCount = viewDialog.findViewById(R.id.tv_add);
        TextView tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        TextView tvAddCart = viewDialog.findViewById(R.id.tv_add_cart);

        // Hiển thị thông tin món ăn
        GlideUtils.loadUrl(mFood.getImage(), imgFoodCart);
        tvFoodNameCart.setText(mFood.getName());

        // Hiển thị giá món ăn
        int totalPrice = mFood.getRealPrice();
        String strTotalPrice = totalPrice + Constant.CURRENCY;
        tvFoodPriceCart.setText(strTotalPrice);

        // Hiển thị số lượng món ăn
        mFood.setCount(1);

        // Hiển thị giá món ăn
        mFood.setTotalPrice(totalPrice);

        // Gán sự kiện khi người dùng click vào nút trừ
        tvSubtractCount.setOnClickListener(v -> {
            int count = Integer.parseInt(tvCount.getText().toString());
            // Nếu số lượng món ăn <= 1 thì không thực hiện gì cả
            if (count <= 1) {
                return;
            }
            // Giảm số lượng món ăn
            int newCount = Integer.parseInt(tvCount.getText().toString()) - 1;
            tvCount.setText(String.valueOf(newCount));

            // Tính tổng giá món ăn
            int totalPrice1 = mFood.getRealPrice() * newCount;
            String strTotalPrice1 = totalPrice1 + Constant.CURRENCY;
            tvFoodPriceCart.setText(strTotalPrice1);

            // Cập nhật số lượng món ăn và tổng giá món ăn
            mFood.setCount(newCount);
            mFood.setTotalPrice(totalPrice1);
        });

        // Gán sự kiện khi người dùng click vào nút cộng
        tvAddCount.setOnClickListener(v -> {
            // Tăng số lượng món ăn
            int newCount = Integer.parseInt(tvCount.getText().toString()) + 1;
            tvCount.setText(String.valueOf(newCount));

            // Tính tổng giá món ăn
            int totalPrice2 = mFood.getRealPrice() * newCount;
            String strTotalPrice2 = totalPrice2 + Constant.CURRENCY;
            tvFoodPriceCart.setText(strTotalPrice2);

            // Cập nhật số lượng món ăn và tổng giá món ăn
            mFood.setCount(newCount);
            mFood.setTotalPrice(totalPrice2);
        });

        // Gán sự kiện khi người dùng click vào nút hủy
        tvCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Gán sự kiện khi người dùng click vào nút thêm vào giỏ hàng
        tvAddCart.setOnClickListener(v -> {
            // Sử dụng FoodDatabase để thêm món ăn vào giỏ hàng
            FoodDatabase.getInstance(FoodDetailActivity.this).foodDAO().insertFood(mFood);
            // Đóng dialog
            bottomSheetDialog.dismiss();
            // Hiển thị trạng thái nút thêm vào giỏ hàng
            setStatusButtonAddToCart();
            // Gửi sự kiện ReloadListCartEvent để cập nhật lại danh sách giỏ hàng
            EventBus.getDefault().post(new ReloadListCartEvent());
        });

        bottomSheetDialog.show();
    }
}