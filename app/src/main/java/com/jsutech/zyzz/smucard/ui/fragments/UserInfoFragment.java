package com.jsutech.zyzz.smucard.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsutech.zyzz.smucard.BaseSMUActivity;
import com.jsutech.zyzz.smucard.R;
import com.jsutech.zyzz.smucard.db.models.UserProfile;
import com.jsutech.zyzz.smucard.network.SMUClient;

import java.util.Objects;

/**
 * 用户信息展示界面
 */
public class UserInfoFragment extends BaseFragment {
    private static final String TAG = "UserInfoFragment";
    private ImageButton refreshInfoBtn;
    private TextView usernameLabel;
    private TextView genderLabel;
    private TextView nationalityLabel;
    private TextView roleLabel;
    private TextView departmentLabel;
    private TextView depositLabel;
    private TextView classificationLabel;
    private TextView addressLabel;
    private TextView suidLabel;
    private TextView pidLabel;
    private TextView creditLabel;
    private TextView createdTimeLabel;
    private TextView accountStatusLabel;
    private TextView effectiveAreaLabel;
    private TextView effectiveDateLabel;
    private ImageView userPhotoImageView;
    private SMUClient client;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_info_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupUI();
        setupEventListener();
        client = ((BaseSMUActivity)getContext()).getClient();
    }

    private void setupEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.refresh_info_btn){
                    refreshInfo();
                }
            }
        };
        refreshInfoBtn.setOnClickListener(onClickListener);
    }


    // 刷新用户信息
    private void refreshInfo() {
        freezeUI();
        client.refreshUserInfo();
        client.requestUserPhoto();
    }

    private void freezeUI() {
        refreshInfoBtn.setEnabled(false);
    }

    private void unfreezeUI(){
        refreshInfoBtn.setEnabled(true);
    }

    private void setupUI() {
        refreshInfoBtn = findViewById(R.id.refresh_info_btn);
        usernameLabel = findViewById(R.id.username_label);
        genderLabel = findViewById(R.id.gender_label);
        nationalityLabel = findViewById(R.id.nationality_label);
        roleLabel = findViewById(R.id.role_label);
        departmentLabel = findViewById(R.id.department_label);
        depositLabel = findViewById(R.id.deposit_label);
        classificationLabel = findViewById(R.id.classification_label);
        addressLabel = findViewById(R.id.address_label);
        suidLabel = findViewById(R.id.suid_label);
        pidLabel = findViewById(R.id.pid_label);
        creditLabel = findViewById(R.id.credit_label);
        createdTimeLabel = findViewById(R.id.created_date_label);
        accountStatusLabel = findViewById(R.id.account_status_label);
        effectiveAreaLabel = findViewById(R.id.effective_area_label);
        effectiveDateLabel = findViewById(R.id.effective_date_label);
        userPhotoImageView = findViewById(R.id.user_photo_image_view);

    }

    public void onUserInfoUpdated(UserProfile profile){
        usernameLabel.setText(String.format("%s:%s", getResources().getText(R.string.username_text), profile.getName()));
        genderLabel.setText(String.format("%s:%s", getResources().getText(R.string.gender_text), profile.getGender()));
        nationalityLabel.setText(String.format("%s:%s", getResources().getText(R.string.nationality_text), profile.getNationality()));
        accountStatusLabel.setText(String.format("%s:%s", getResources().getText(R.string.account_status_text), profile.getAccountStatus()));
        pidLabel.setText(String.format("%s:%s", getResources().getText(R.string.pid_text), profile.getPID()));
        suidLabel.setText(String.format("%s:%s", getResources().getText(R.string.suid_text), profile.getSUID()));
        createdTimeLabel.setText(String.format("%s:%s", getResources().getText(R.string.created_time_text), profile.getCreatedDate()));
        creditLabel.setText(String.format("%s:%s", getResources().getText(R.string.credit_text), profile.getCreditCard()));
        classificationLabel.setText(String.format("%s:%s", getResources().getText(R.string.classification_text), profile.getClassification()));
        addressLabel.setText(String.format("%s:%s", getResources().getText(R.string.address_text), profile.getAddress()));
        roleLabel.setText(String.format("%s:%s", getResources().getText(R.string.role_text), profile.getRole()));
        departmentLabel.setText(String.format("%s:%s", getResources().getText(R.string.department_text), profile.getDepartment()));
        depositLabel.setText(String.format("%s:%s", getResources().getText(R.string.deposit_text), profile.getDeposit()));
        effectiveAreaLabel.setText(String.format("%s:%s", getResources().getText(R.string.effective_area_text), profile.getEffectiveArea()));
        effectiveDateLabel.setText(String.format("%s:%s", getResources().getText(R.string.effective_area_text), profile.getEffectiveDate()));
        unfreezeUI();
    }

    private  <T extends View> T findViewById(@IdRes int id) {
        return Objects.requireNonNull(getView()).findViewById(id);
    }

    @Override
    public void onMessageReceived(int msgId, Object data) {
        Log.d(TAG, "" + msgId);
        switch (msgId){
            case SMUClient.ClientMessages.RECEIVE_USER_PROFILE:
                onUserInfoUpdated((UserProfile)data);
                break;
            case SMUClient.ClientMessages.RECEIVE_USER_PHOTO:
                userPhotoImageView.setImageBitmap((Bitmap)data);
                break;
        }
        unfreezeUI();
    }
}
