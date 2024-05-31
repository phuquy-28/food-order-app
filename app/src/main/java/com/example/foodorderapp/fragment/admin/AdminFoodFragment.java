package com.example.foodorderapp.fragment.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.AddFoodActivity;
import com.example.foodorderapp.activity.AdminMainActivity;
import com.example.foodorderapp.adapter.AdminFoodAdapter;
import com.example.foodorderapp.constant.Constant;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentAdminFoodBinding;
import com.example.foodorderapp.fragment.BaseFragment;
import com.example.foodorderapp.listener.IOnManagerFoodListener;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminFoodFragment extends BaseFragment {

    private FragmentAdminFoodBinding mFragmentAdminFoodBinding;
    private List<Food> mListFood;
    private AdminFoodAdapter mAdminFoodAdapter;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm liên kết và trả về giao diện fragment_admin_food.xml bằng thư viện view binding của Android
    // Gọi hàm initView để khởi tạo RecyclerView để hiển thị danh sách món ăn
    // Gọi hàm initListener để gán sự kiện cho các phần tử trong Fragment
    // Gọi hàm getListFood để lấy danh sách món ăn từ Firebase Realtime Database
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminFoodBinding = FragmentAdminFoodBinding.inflate(inflater, container, false);
        initView();
        initListener();
        getListFood("");
        return mFragmentAdminFoodBinding.getRoot();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar bằng hàm setToolBar của AdminMainActivity
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.food));
        }
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo RecyclerView và gắn Adapter vào để hiển thị danh sách món ăn ở chế độ dọc (LinearLayoutManager)
    // Khởi tạo danh sách món ăn và Adapter AdminFoodAdapter
    // Gán IOnManagerFoodListener cho Adapter để xử lý sự kiện khi người dùng click vào các nút sửa, xóa món ăn
    // Ghi đè lại onClickUpdateFood và onClickDeleteFood để gọi hai hàm của Fragment này là onClickEditFood và deleteFoodItem
    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFoodBinding.rcvFood.setLayoutManager(linearLayoutManager);
        mListFood = new ArrayList<>();
        mAdminFoodAdapter = new AdminFoodAdapter(mListFood, new IOnManagerFoodListener() {
            @Override
            public void onClickUpdateFood(Food food) {
                onClickEditFood(food);
            }
            @Override
            public void onClickDeleteFood(Food food) {
                deleteFoodItem(food);
            }
        });
        mFragmentAdminFoodBinding.rcvFood.setAdapter(mAdminFoodAdapter);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để gán sự kiện cho các phần tử trong Fragment
    // Gán sự kiện cho nút thêm món ăn bằng cách gọi hàm onClickAddFood
    // Gán sự kiện cho nút tìm kiếm món ăn bằng cách gọi hàm searchFood
    // Gán sự kiện cho EditText tìm kiếm món ăn bằng cách gọi hàm searchFood
    private void initListener() {
        mFragmentAdminFoodBinding.btnAddFood.setOnClickListener(v -> onClickAddFood());
        mFragmentAdminFoodBinding.imgSearch.setOnClickListener(v -> searchFood());
        mFragmentAdminFoodBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });
        mFragmentAdminFoodBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchFood();
                }
            }
        });
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để bắt đầu activity thêm món ăn bằng cách gọi hàm startActivity của GlobalFunction
    private void onClickAddFood() {
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để bắt đầu activity chỉnh sửa món ăn bằng cách gọi hàm startActivity của GlobalFunction
    // Thêm dữ liệu món ăn vào Bundle để truyền qua activity chỉnh sửa món ăn
    private void onClickEditFood(Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class, bundle);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm thực hiện xóa món ăn
    // Cửa sổ Dialog hiện lên để hỏi xác nhận để xóa món ăn
    // Nếu người dùng chọn "Đồng ý" thì xóa món ăn đó khỏi Firebase Realtime Database
    // Nếu người dùng chọn "Hủy" thì không làm gì cả
    private void deleteFoodItem(Food food) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                            .child(String.valueOf(food.getId())).removeValue((error, ref) ->
                            Toast.makeText(getActivity(),
                                    getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để thực hiện tìm kiếm món ăn bằng hàm getListFood
    // Lấy từ khóa tìm kiếm từ EditText và gọi hàm getListFood có input là từ khóa tìm kiếm
    private void searchFood() {
        String strKey = mFragmentAdminFoodBinding.edtSearchName.getText().toString().trim();
        if (mListFood != null) {
            mListFood.clear();
        } else {
            mListFood = new ArrayList<>();
        }
        getListFood(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để lấy danh sách món ăn từ Firebase Realtime Database để load lên RecyclerView
    // Nếu có từ khóa tìm kiếm thì sẽ lấy danh sách món ăn theo từ khóa tìm kiếm
    // Nếu không có từ khóa tìm kiếm thì lấy toàn bộ danh sách món ăn
    public void getListFood(String keyword) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    // Hàm dùng để thêm món ăn vào danh sách món ăn
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null || mListFood == null || mAdminFoodAdapter == null) {
                            return;
                        }
                        if (StringUtil.isEmpty(keyword)) {
                            mListFood.add(0, food);
                        } else {
                            if (GlobalFunction.getTextSearch(food.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                                mListFood.add(0, food);
                            }
                        }
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }
                    // Hàm dùng để cập nhật món ăn trong danh sách món ăn
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null || mListFood == null
                                || mListFood.isEmpty() || mAdminFoodAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListFood.size(); i++) {
                            if (food.getId() == mListFood.get(i).getId()) {
                                mListFood.set(i, food);
                                break;
                            }
                        }
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }
                    // Hàm dùng để xóa món ăn khỏi danh sách món ăn
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null || mListFood == null
                                || mListFood.isEmpty() || mAdminFoodAdapter == null) {
                            return;
                        }
                        for (Food foodObject : mListFood) {
                            if (food.getId() == foodObject.getId()) {
                                mListFood.remove(foodObject);
                                break;
                            }
                        }
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
