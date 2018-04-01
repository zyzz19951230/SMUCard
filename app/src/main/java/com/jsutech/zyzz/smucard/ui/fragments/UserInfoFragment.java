package com.jsutech.zyzz.smucard.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.jsutech.zyzz.smucard.R;
import com.jsutech.zyzz.smucard.db.models.UserProfile;

import java.util.Objects;

/**
 * 用户信息展示界面
 */
public class UserInfoFragment extends BaseFragment {
    private static final String TAG = "UserInfoFragment";
    private static final String OUTDATED = "数据未更新";
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
    private TextView createdDateLabel;
    private TextView accountStatusLabel;
    private TextView effectiveAreaLabel;
    private TextView effectiveDateLabel;
    private ImageView userPhotoImageView;

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
                if (view.getId() == R.id.refresh_info_btn) {
                    refresh();
                }
            }
        };
        refreshInfoBtn.setOnClickListener(onClickListener);
    }


    // 刷新
    private void refresh() {
        freezeUI();
        sendMessage((ICommunicator)getContext(), UpdateMessages.REFRESH_USER_PROFILE, null);
        sendMessage((ICommunicator)getContext(), UpdateMessages.REFRESH_USER_PHOTO, null);
    }

    private void freezeUI() {
        refreshInfoBtn.setEnabled(false);
    }

    private void unfreezeUI() {
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
        createdDateLabel = findViewById(R.id.created_date_label);
        accountStatusLabel = findViewById(R.id.account_status_label);
        effectiveAreaLabel = findViewById(R.id.effective_area_label);
        effectiveDateLabel = findViewById(R.id.effective_date_label);
        userPhotoImageView = findViewById(R.id.user_photo_image_view);

    }

    public void updatedUI(UserProfile profile) {

        String username = profile.getName();
        String gender = profile.getGender();
        String nationality = profile.getNationality();
        String accountStatus = profile.getAccountStatus();
        String PID = profile.getPID();
        String SUID = profile.getSUID();
        String createdDate = profile.getCreatedDate();
        String address = profile.getAddress();
        String classification = profile.getClassification();
        String role = profile.getRole();
        String department = profile.getDepartment();
        String deposit = profile.getDeposit();
        String effectiveArea = profile.getEffectiveArea();
        String effectiveDate = profile.getEffectiveDate();
        String creditCard = profile.getCreditCard();
        byte[] userPhoto = profile.getPhoto();
        if (username != null)
            usernameLabel.setText(String.format("%s:%s", getResources().getText(R.string.username_text), username));
        else
            usernameLabel.setText(String.format("%s:%s", getResources().getText(R.string.username_text), OUTDATED));
        if (gender != null)
            genderLabel.setText(String.format("%s:%s", getResources().getText(R.string.gender_text), gender));
        else
            genderLabel.setText(String.format("%s:%s", getResources().getText(R.string.gender_text), OUTDATED));

        if (nationality != null)
            nationalityLabel.setText(String.format("%s:%s", getResources().getText(R.string.nationality_text), nationality));
        else
            nationalityLabel.setText(String.format("%s:%s", getResources().getText(R.string.nationality_text), OUTDATED));

        if (accountStatus != null)
            accountStatusLabel.setText(String.format("%s:%s", getResources().getText(R.string.account_status_text), accountStatus));
        else
            accountStatusLabel.setText(String.format("%s:%s", getResources().getText(R.string.account_status_text), OUTDATED));

        if (PID != null)
            pidLabel.setText(String.format("%s:%s", getResources().getText(R.string.pid_text), PID));
        else
            pidLabel.setText(String.format("%s:%s", getResources().getText(R.string.pid_text), OUTDATED));

        if (SUID != null)
            suidLabel.setText(String.format("%s:%s", getResources().getText(R.string.suid_text), SUID));
        else
            suidLabel.setText(String.format("%s:%s", getResources().getText(R.string.suid_text), OUTDATED));
        if (createdDate != null)
            createdDateLabel.setText(String.format("%s:%s", getResources().getText(R.string.created_time_text), createdDate));
        else
            createdDateLabel.setText(String.format("%s:%s", getResources().getText(R.string.created_time_text), OUTDATED));
        if (creditCard != null)
            creditLabel.setText(String.format("%s:%s", getResources().getText(R.string.credit_text), creditCard));
        else
            creditLabel.setText(String.format("%s:%s", getResources().getText(R.string.credit_text), OUTDATED));
        if (classification != null)
            classificationLabel.setText(String.format("%s:%s", getResources().getText(R.string.classification_text), classification));
        else
            classificationLabel.setText(String.format("%s:%s", getResources().getText(R.string.classification_text), OUTDATED));

        if (address != null)
            addressLabel.setText(String.format("%s:%s", getResources().getText(R.string.address_text), address));
        else
            addressLabel.setText(String.format("%s:%s", getResources().getText(R.string.address_text), OUTDATED));

        if (role != null)
            roleLabel.setText(String.format("%s:%s", getResources().getText(R.string.role_text), role));
        else
            roleLabel.setText(String.format("%s:%s", getResources().getText(R.string.role_text), OUTDATED));

        if (department != null)
            departmentLabel.setText(String.format("%s:%s", getResources().getText(R.string.department_text), department));
        else
            departmentLabel.setText(String.format("%s:%s", getResources().getText(R.string.department_text), OUTDATED));

        if (deposit != null)
            depositLabel.setText(String.format("%s:%s", getResources().getText(R.string.deposit_text), deposit));
        else
            depositLabel.setText(String.format("%s:%s", getResources().getText(R.string.deposit_text), OUTDATED));

        if (effectiveArea != null)
            effectiveAreaLabel.setText(String.format("%s:%s", getResources().getText(R.string.effective_area_text), effectiveArea));
        else
            effectiveAreaLabel.setText(String.format("%s:%s", getResources().getText(R.string.effective_area_text), OUTDATED));

        if (effectiveDate != null)
            effectiveDateLabel.setText(String.format("%s:%s", getResources().getText(R.string.effective_date_text), effectiveDate));
        else
            effectiveDateLabel.setText(String.format("%s:%s", getResources().getText(R.string.effective_date_text), effectiveDate));

        if (userPhoto != null) {
            releaseImageViewResource(userPhotoImageView);
            userPhotoImageView.setImageBitmap(BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length));
        } else {
            releaseImageViewResource(userPhotoImageView);
            userPhotoImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.user_photo_placeholder));
        }
    }

    private <T extends View> T findViewById(@IdRes int id) {
        return Objects.requireNonNull(getView()).findViewById(id);
    }

    /**
     * Fragment 与 Activity进行通信的接口
     *
     * @param msgID
     * @param data
     * @param sender
     */
    @Override
    public void onMessageReceived(int msgID, Object data, ICommunicator sender) {
        Log.d(TAG, "" + msgID);
        switch (msgID) {
            case UpdateMessages.UPDATE_USER_PROFILE:
                updatedUI((UserProfile) data);
                break;
        }
        unfreezeUI();
    }

    private boolean nullOrEmpty(Object data) {
        return data == null || !(data instanceof CharSequence) || ((CharSequence) data).length() <= 0;

    }


    public void releaseImageViewResource(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                Log.d(TAG, "recycle unused bitmap.");
                bitmap.recycle();
            }
        }
    }
}
