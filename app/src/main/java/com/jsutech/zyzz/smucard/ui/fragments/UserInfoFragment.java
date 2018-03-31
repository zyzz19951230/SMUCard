package com.jsutech.zyzz.smucard.ui.fragments;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jsutech.zyzz.smucard.BaseSMUActivity;
import com.jsutech.zyzz.smucard.R;
import com.jsutech.zyzz.smucard.db.models.UserProfile;
import com.jsutech.zyzz.smucard.network.SMUClient;

/**
 * 用户信息展示界面
 */
public class UserInfoFragment extends BaseSMUFragment {
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
    private TextView uidLabel;
    private TextView pidLabel;
    private TextView creditLabel;
    private TextView createdTimeLabel;
    private TextView accountStatusLabel;

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
    }

    private void setupEventListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.refresh_info_btn){
                    refreshInfo();
                    Log.d(TAG, "current handler:"  + getSMUClient().getCurrentSMUHandler());

                }
            }
        };
        refreshInfoBtn.setOnClickListener(onClickListener);
    }


    // 刷新用户信息
    private void refreshInfo() {
        freezeUI();
        SMUClient smuClient = getSMUClient();
        if (smuClient != null){
            smuClient.refreshUserInfo();
        }
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
        uidLabel = findViewById(R.id.suid_label);
        pidLabel = findViewById(R.id.pid_label);
        creditLabel = findViewById(R.id.credit_label);
        createdTimeLabel = findViewById(R.id.created_date_label);
        accountStatusLabel = findViewById(R.id.account_status_label);

    }

    public void onUserInfoUpdated(UserProfile profile){
        usernameLabel.setText(String.format("%s:%s", getResources().getText(R.string.username_text), profile.getName()));
        genderLabel.setText(String.format("%s:%s", getResources().getText(R.string.gender_text), profile.getGender()));
        nationalityLabel.setText(String.format("%s:%s", getResources().getText(R.string.nationality_text), profile.getNationality()));
        accountStatusLabel.setText(String.format("%s:%s", getResources().getText(R.string.account_status_text), profile.getAccountStatus()));
        pidLabel.setText(String.format("%s:%s", getResources().getText(R.string.pid_text), profile.getPID()));
        uidLabel.setText(String.format("%s:%s", getResources().getText(R.string.suid_text), profile.getSUID()));
        createdTimeLabel.setText(String.format("%s:%s", getResources().getText(R.string.created_time_text), profile.getCreatedDate()));
        creditLabel.setText(String.format("%s:%s", getResources().getText(R.string.credit_text), profile.getCreatedDate()));
        classificationLabel.setText(String.format("%s:%s", getResources().getText(R.string.classification_text), profile.getClassification()));
        addressLabel.setText(String.format("%s:%s", getResources().getText(R.string.address_text), profile.getAddress()));
        roleLabel.setText(String.format("%s:%s", getResources().getText(R.string.role_text), profile.getRole()));
        departmentLabel.setText(String.format("%s:%s", getResources().getText(R.string.department_text), profile.getDepartment()));
        depositLabel.setText(String.format("%s:%s", getResources().getText(R.string.deposit_text), profile.getDeposit()));
        unfreezeUI();
    }

    private  <T extends View> T findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }

    @Override
    public void onClientMessageReceived(int msgId, Object data) {
        Log.d(TAG, "" + msgId);
        Toast.makeText(getContext(), data.toString(), Toast.LENGTH_SHORT).show();
        unfreezeUI();
    }

    private void initLabels(){

    }

}
