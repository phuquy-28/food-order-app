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
import com.example.foodorderapp.adapter.FeedbackAdapter;
import com.example.foodorderapp.databinding.FragmentAdminFeedbackBinding;
import com.example.foodorderapp.fragment.BaseFragment;
import com.example.foodorderapp.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackFragment extends BaseFragment {

    private FragmentAdminFeedbackBinding mFragmentAdminFeedbackBinding;
    private List<Feedback> mListFeedback;
    private FeedbackAdapter mFeedbackAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminFeedbackBinding = FragmentAdminFeedbackBinding.inflate(inflater, container, false);

        initView();
        getListFeedback();
        return mFragmentAdminFeedbackBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.feedback));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
    }

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
                            if (feedback != null) {
                                mListFeedback.add(0, feedback);
                            }
                        }
                        mFeedbackAdapter = new FeedbackAdapter(mListFeedback);
                        mFragmentAdminFeedbackBinding.rcvFeedback.setAdapter(mFeedbackAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}
