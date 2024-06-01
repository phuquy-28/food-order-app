package com.example.foodorderapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorderapp.prefs.DataStoreManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.FoodDetailActivity;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.adapter.FoodGridAdapter;
import com.example.foodorderapp.adapter.FoodPopularAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentHomeBinding;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    // Biến mFragmentHomeBinding tham chiếu đến layout fragment_home.xml
    private FragmentHomeBinding mFragmentHomeBinding;

    // Biến mListFood là danh sách thức ăn
    private List<Food> mListFood;

    // Biến mListFoodPopular là danh sách thức ăn phổ biến
    private List<Food> mListFoodPopular;

    // Biến REQUEST_CODE_SPEECH_INPUT là mã request khi nhận kết quả từ Speech to Text
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    // Biến RECORD_AUDIO_PERMISSION_REQUEST_CODE là mã request khi xin quyền ghi âm
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1001;

    // Biến mHandlerBanner là Handler để xử lý banner
    private final Handler mHandlerBanner = new Handler();

    // Biến mRunnableBanner là Runnable để chạy banner
    private final Runnable mRunnableBanner = new Runnable() {
        // Người đảm nhận: Đặng Phú Quý
        // Hàm run() chạy banner
        // Nếu danh sách thức ăn phổ biến rỗng hoặc null thì không làm gì cả
        // Nếu đang ở vị trí cuối cùng thì chuyển về vị trí đầu tiên
        // Nếu không thì chuyển sang vị trí tiếp theo
        @Override
        public void run() {
            if (mListFoodPopular == null || mListFoodPopular.isEmpty()) {
                return;
            }
            if (mFragmentHomeBinding.viewpager2.getCurrentItem() == mListFoodPopular.size() - 1) {
                mFragmentHomeBinding.viewpager2.setCurrentItem(0);
                return;
            }
            mFragmentHomeBinding.viewpager2.setCurrentItem(mFragmentHomeBinding.viewpager2.getCurrentItem() + 1);
        }
    };

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onCreateView() được gọi khi Fragment được tạo
    // Khởi tạo giao diện HomeFragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        // Hiển thị email người dùng
        mFragmentHomeBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());
        // Lấy danh sách thức ăn từ Firebase
        getListFoodFromFirebase("");
        // Khởi tạo listener
        initListener();

        return mFragmentHomeBinding.getRoot();
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm initToolbar() khởi tạo thanh toolbar
    @Override
    protected void initToolbar() {
        // Nếu Activity không null thì hiển thị thanh toolbar
        // Tiêu đề thanh toolbar là "Trang chủ"
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(true, getString(R.string.home));
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm initListener() khởi tạo listener
    private void initListener() {
        // Khi thay đổi text trong edtSearchName thì gọi hàm getListFoodFromFirebase()
        mFragmentHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mListFood != null) mListFood.clear();
                    getListFoodFromFirebase("");
                }
            }
        });

        // Khi click vào imgSearch thì gọi hàm searchFood()
        mFragmentHomeBinding.imgSearch.setOnClickListener(view -> {
            searchFood();
        });

        // Khi click vào imgMic thì gọi hàm startSpeechRecognition()
        mFragmentHomeBinding.imgMic.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},
                        RECORD_AUDIO_PERMISSION_REQUEST_CODE);
            } else {
                startSpeechRecognition();
            }

        });

        // Khi click vào các icon thì gọi hàm getFoodFromFirebaseByCategory() với loại thức ăn là Cơm.
        mFragmentHomeBinding.ivRice.setOnClickListener(v -> {
            if (mListFood != null) mListFood.clear();
            getFoodFromFirebaseByCategory("Cơm");
        });

        // Khi click vào các icon thì gọi hàm getFoodFromFirebaseByCategory() với loại thức ăn là Bún/Phở.
        mFragmentHomeBinding.ivNoodle.setOnClickListener(v -> {
            if (mListFood != null) mListFood.clear();
            getFoodFromFirebaseByCategory("Bún/Phở");
        });

        // Khi click vào các icon thì gọi hàm getFoodFromFirebaseByCategory() với loại thức ăn là Mì.
        mFragmentHomeBinding.ivSpagetti.setOnClickListener(v -> {
            if (mListFood != null) mListFood.clear();
            getFoodFromFirebaseByCategory("Mì");
        });

        // Khi click vào các icon thì gọi hàm getFoodFromFirebaseByCategory() với loại thức ăn là Đồ uống.
        mFragmentHomeBinding.ivDrinks.setOnClickListener(v -> {
            if (mListFood != null) mListFood.clear();
            getFoodFromFirebaseByCategory("Đồ uống");
        });

        // Khi click vào edtSearchName và nhấn Enter thì gọi hàm searchFood()
        mFragmentHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm startSpeechRecognition() bắt đầu nhận giọng nói
    public void startSpeechRecognition() {
        // Xóa text trong edtSearchName
        mFragmentHomeBinding.edtSearchName.setText("");

        // Tạo Intent RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        // Đặt ngôn ngữ là tiếng Việt
        // Hiển thị thông báo "Speak to text"
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        // Thực hiện Intent
        // Nếu không có Activity nào xử lý Intent thì hiển thị thông báo lỗi
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Xử lý kết quả trả về từ việc nhận dạng giọng nói
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == getActivity().RESULT_OK && null != data) {
                // Lấy kết quả nhận dạng được trả về
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mFragmentHomeBinding.edtSearchName.setText(result.get(0));
                GlobalFunction.hideSoftKeyboard(getActivity());
                searchFood();
            }
        }
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm displayListFoodPopular() hiển thị danh sách thức ăn phổ biến
    private void displayListFoodPopular() {
        // Tạo adapter FoodPopularAdapter và gán vào viewpager2
        FoodPopularAdapter mFoodPopularAdapter = new FoodPopularAdapter(getListFoodPopular(), this::goToFoodDetail);
        mFragmentHomeBinding.viewpager2.setAdapter(mFoodPopularAdapter);

        // Tạo indicator cho viewpager2
        mFragmentHomeBinding.indicator3.setViewPager(mFragmentHomeBinding.viewpager2);

        // Khi chuyển trang thì dừng banner
        // Sau 3 giây chuyển sang trang tiếp theo
        mFragmentHomeBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm displayListFoodSuggest() hiển thị danh sách thức ăn gợi ý
    private void displayListFoodSuggest() {
        // Tạo GridLayoutManager với 2 cột và gán vào rcvFood
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFragmentHomeBinding.rcvFood.setLayoutManager(gridLayoutManager);

        // Tạo adapter FoodGridAdapter và gán vào rcvFood
        FoodGridAdapter mFoodGridAdapter = new FoodGridAdapter(mListFood, this::goToFoodDetail);
        mFragmentHomeBinding.rcvFood.setAdapter(mFoodGridAdapter);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getListFoodPopular() lấy danh sách thức ăn phổ biến
    // Nếu danh sách thức ăn rỗng hoặc null thì trả về danh sách thức ăn phổ biến rỗng
    // Duyệt danh sách thức ăn và thêm vào danh sách thức ăn phổ biến
    private List<Food> getListFoodPopular() {
        mListFoodPopular = new ArrayList<>();
        if (mListFood == null || mListFood.isEmpty()) {
            return mListFoodPopular;
        }
        for (Food food : mListFood) {
            if (food.isPopular()) {
                mListFoodPopular.add(food);
            }
        }
        return mListFoodPopular;
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getListFoodFromFirebase() lấy danh sách thức ăn từ Firebase
    // Nếu Activity null thì không làm gì cả
    // Duyệt danh sách thức ăn từ Firebase
    // Nếu key rỗng thì thêm thức ăn vào đầu danh sách
    // Nếu key không rỗng thì kiểm tra tên thức ăn có chứa key không
    // Nếu có thì thêm thức ăn vào đầu danh sách
    private void getListFoodFromFirebase(String key) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mFragmentHomeBinding.layoutContent.setVisibility(View.VISIBLE);
                mListFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        mListFood.add(0, food);
                    } else {
                        if (GlobalFunction.getTextSearch(food.getName()).toLowerCase().trim()
                                .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())) {
                            mListFood.add(0, food);
                        }
                    }
                }
                displayListFoodPopular();
                displayListFoodSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm getFoodFromFirebaseByCategory() lấy thức ăn từ Firebase theo loại
    private void getFoodFromFirebaseByCategory(String category) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food == null) {
                        return;
                    }

                    String foodCategory = food.getCategory();
                    if (foodCategory != null && foodCategory.equalsIgnoreCase(category)) {
                        mListFood.add(0, food);
                    }
                }
                displayListFoodPopular();
                displayListFoodSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm searchFood() tìm kiếm thức ăn
    // Lấy key từ edtSearchName
    // Nếu danh sách thức ăn không rỗng thì xóa danh sách thức ăn
    // Gọi hàm getListFoodFromFirebase() với key
    private void searchFood() {
        String strKey = mFragmentHomeBinding.edtSearchName.getText().toString().trim();
        if (mListFood != null) mListFood.clear();
        getListFoodFromFirebase(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm goToFoodDetail() chuyển sang màn hình chi tiết thức ăn
    // Gửi thức ăn được chọn sang màn hình chi tiết
    private void goToFoodDetail(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), FoodDetailActivity.class, bundle);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onPause() dừng banner
    @Override
    public void onPause() {
        super.onPause();
        mHandlerBanner.removeCallbacks(mRunnableBanner);
    }

    // Người đảm nhận: Đặng Phú Quý
    // Hàm onResume() chạy banner
    // Chạy banner sau 3 giây
    @Override
    public void onResume() {
        super.onResume();
        mHandlerBanner.postDelayed(mRunnableBanner, 3000);
    }
}
