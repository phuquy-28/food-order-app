package com.example.foodorderapp.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.AdminMainActivity;
import com.example.foodorderapp.adapter.AdminFeedbackAdapter;
import com.example.foodorderapp.databinding.FragmentAdminFeedbackBinding;
import com.example.foodorderapp.fragment.BaseFragment;
import com.example.foodorderapp.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackFragment extends BaseFragment {

    private FragmentAdminFeedbackBinding mFragmentAdminFeedbackBinding;
    private List<Feedback> mListFeedback;
    private AdminFeedbackAdapter mAdminFeedbackAdapter;

    // Người đảm nhận: Trần Quốc Phương
    // Hàm liên kết và trả về giao diện fragment_admin_feedback.xml bằng thư viện view binding của Android
    // Gọi hàm initView để khởi tạo RecyclerView để hiển thị danh sách phản hồi đánh giá
    // Gọi hàm getListFeedback để lấy danh sách phản hồi đánh giá từ Firebase Realtime Database
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminFeedbackBinding = FragmentAdminFeedbackBinding.inflate(inflater, container, false);
        initView();
        getListFeedback();
        return mFragmentAdminFeedbackBinding.getRoot();
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo tiêu đề ban đầu cho Toolbar bằng hàm setToolBar của AdminMainActivity
    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.feedback));
        }
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để khởi tạo RecyclerView để hiển thị danh sách món ăn ở chế độ dọc (LinearLayoutManager)
    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
    }

    // Người đảm nhận: Trần Quốc Phương
    // Hàm dùng để lấy danh sách phản hồi đánh giá từ Firebase Realtime Database để load lên RecyclerView
    public void getListFeedback() {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFeedbackDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mListFeedback != null) {
                            mListFeedback.clear();
                        } else {
                            mListFeedback = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Feedback feedback = dataSnapshot.getValue(Feedback.class);
                            feedback.setId(Long.parseLong(dataSnapshot.getKey()));
                            if (feedback != null) {
                                mListFeedback.add(0, feedback);
                            }
                        }
                        mAdminFeedbackAdapter = new AdminFeedbackAdapter(mListFeedback);
                        mFragmentAdminFeedbackBinding.rcvFeedback.setAdapter(mAdminFeedbackAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
