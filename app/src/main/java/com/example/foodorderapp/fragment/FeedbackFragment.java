package com.example.foodorderapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodorderapp.ControllerApplication;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activity.MainActivity;
import com.example.foodorderapp.constant.GlobalFunction;
import com.example.foodorderapp.databinding.FragmentFeedbackBinding;
import com.example.foodorderapp.model.Feedback;
import com.example.foodorderapp.prefs.DataStoreManager;
import com.example.foodorderapp.utils.StringUtil;

public class FeedbackFragment extends BaseFragment {

    private FragmentFeedbackBinding mFragmentFeedbackBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false);

        mFragmentFeedbackBinding.edtEmail.setText(DataStoreManager.getUser().getEmail());
        mFragmentFeedbackBinding.tvSendFeedback.setOnClickListener(v -> onClickSendFeedback());
        mFragmentFeedbackBinding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating <= 1) {
                mFragmentFeedbackBinding.tvRatingTitle.setText(getString(R.string.one_star));
                mFragmentFeedbackBinding.edtComment.setText(getString(R.string.one_star));
            } else if (rating <= 2) {
                mFragmentFeedbackBinding.tvRatingTitle.setText(getString(R.string.two_stars));
                mFragmentFeedbackBinding.edtComment.setText(getString(R.string.two_stars));
            } else if (rating <= 3) {
                mFragmentFeedbackBinding.tvRatingTitle.setText(getString(R.string.three_stars));
                mFragmentFeedbackBinding.edtComment.setText(getString(R.string.three_stars));
            } else if (rating <= 4) {
                mFragmentFeedbackBinding.tvRatingTitle.setText(getString(R.string.four_stars));
                mFragmentFeedbackBinding.edtComment.setText(getString(R.string.four_stars));
            } else {
                mFragmentFeedbackBinding.tvRatingTitle.setText(getString(R.string.five_stars));
                mFragmentFeedbackBinding.edtComment.setText(getString(R.string.five_stars));
            }
        });

        return mFragmentFeedbackBinding.getRoot();
    }

    private void onClickSendFeedback() {
        if (getActivity() == null) {
            return;
        }
        MainActivity activity = (MainActivity) getActivity();

        String strName = mFragmentFeedbackBinding.edtName.getText().toString();
        String strPhone = mFragmentFeedbackBinding.edtPhone.getText().toString();
        String strEmail = mFragmentFeedbackBinding.edtEmail.getText().toString();
        String strComment = mFragmentFeedbackBinding.edtComment.getText().toString();
        String strDate = GlobalFunction.getCurrentDateTime();
        double rating = mFragmentFeedbackBinding.ratingBar.getRating();


        if (StringUtil.isEmpty(strName)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.name_require));
        } else if (StringUtil.isEmpty(strComment)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.comment_require));
        } else {
            activity.showProgressDialog(true);
            Feedback feedback = new Feedback(strName, strPhone, strEmail, strComment, strDate, rating);
            ControllerApplication.get(getActivity()).getFeedbackDatabaseReference()
                    .child(String.valueOf(System.currentTimeMillis()))
                    .setValue(feedback, (databaseError, databaseReference) -> {
                        activity.showProgressDialog(false);
                        sendFeedbackSuccess();
                    });
        }
    }

    public void sendFeedbackSuccess() {
        GlobalFunction.hideSoftKeyboard(getActivity());
        GlobalFunction.showToastMessage(getActivity(), getString(R.string.send_feedback_success));
        mFragmentFeedbackBinding.edtName.setText("");
        mFragmentFeedbackBinding.edtPhone.setText("");
        mFragmentFeedbackBinding.edtComment.setText("");
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.feedback));
        }
    }
}
