package com.jsutech.zyzz.smucard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jsutech.zyzz.smucard.network.ResultWrapper;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;

public class LoginActivity extends SMUBaseActivity {

    private final static String TAG = "LoginActivity";

    private EditText usernameTxt;
    private EditText passwordTxt;
    private EditText checkCodeTxt;
    private ImageView checkCodeImg;
    private Button loginBtn;
    protected SMUClient smuClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 创建Handler
        SMUHandler smuHandler = new SMUHandler(this);
        // 获取client对象
        smuClient = ((SMUCardApplication)getApplication()).getClient();
        smuClient.setSmuHandler(smuHandler);
        // 获取布局中的控件实例
        obtainUI();
        // 设置事件监听
        setupUIEventListener();
    }

    @Override
    public void onUIUpdateMessageReceived(int msgId, ResultWrapper resultWrapper) {
        Bitmap refreshCheckCodeBp;
        switch (msgId){
            case SMUHandler.UIUpdateMessages.RECEIVE_CHECK_CODE:
                // 收到新的验证码图片后的处理：
                // 先回收过期的图片资源
                releaseImageViewResouce(checkCodeImg);
                // 再设置新的验证码图片
                checkCodeImg.setImageBitmap((Bitmap)resultWrapper.getData());
                unfreezeUI();
                break;
            case SMUHandler.UIUpdateMessages.NETWORK_ERROR:
            case SMUHandler.UIUpdateMessages.SERVER_ERROR:
                break;
            default:
                releaseImageViewResouce(checkCodeImg);
                refreshCheckCodeBp = BitmapFactory.decodeResource(getResources(), R.drawable.refresh_check_code);
                checkCodeImg.setImageBitmap(refreshCheckCodeBp);
                unfreezeUI();
        }
    }

    private void obtainUI(){
        usernameTxt = findViewById(R.id.usernameEditText);
        passwordTxt = findViewById(R.id.passwordEditText);
        checkCodeTxt = findViewById(R.id.checkCodeEditText);
        checkCodeImg = findViewById(R.id.checkCodeImageView);
        loginBtn = findViewById(R.id.loginButton);
        Log.d(TAG, (loginBtn == null) + "");

    }
    private void setupUIEventListener(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.checkCodeImageView:
                        freezeUI();
                        smuClient.refreshCheckCode();
                        break;
                    case R.id.loginButton:
                        break;
                }
            }
        };
        checkCodeImg.setOnClickListener(onClickListener);
        Log.d(TAG, (loginBtn == null) + "");
        loginBtn.setOnClickListener(onClickListener);
    }

    private void freezeUI(){
        usernameTxt.setEnabled(false);
        passwordTxt.setEnabled(false);
        checkCodeTxt.setEnabled(false);
        checkCodeImg.setClickable(false);
        loginBtn.setEnabled(false);
    }

    private void unfreezeUI(){
        usernameTxt.setEnabled(true);
        passwordTxt.setEnabled(true);
        checkCodeTxt.setEnabled(true);
        checkCodeImg.setClickable(true);
        loginBtn.setEnabled(true);
    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
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
