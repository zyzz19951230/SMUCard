package com.jsutech.zyzz.smucard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;
import com.jsutech.zyzz.smucard.network.exceptions.NetworkException;

public class LoginActivity extends SMUBaseActivity {

    private final static String TAG = "LoginActivity";

    private EditText usernameTxt;
    private EditText passwordTxt;
    private EditText checkCodeTxt;
    private ImageView checkCodeImg;
    private Button loginBtn;
    protected SMUClient smuClient;

    // 进度对话框：正在刷新验证码
    MaterialDialog refreshingCheckCodeDialog;
    // 进度对话框：正在登录
    MaterialDialog loggingDialog;
    // 对话框显示错误信息
    MaterialDialog errorDialog;

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
        setupUI();
        // 设置事件监听
        setupUIEventListener();
    }

    @Override
    public void onUIUpdateMessageReceived(int msgId, Object data) {
        switch (msgId){
            // 刷新验证码：收到新的验证码
            case SMUHandler.UIUpdateMessages.RECEIVE_CHECK_CODE:
                // 先回收过期的图片资源
                releaseImageViewResource(checkCodeImg);
                // 再设置新的验证码图片
                checkCodeImg.setImageBitmap((Bitmap) data);
                if (refreshingCheckCodeDialog.isShowing()){
                    refreshingCheckCodeDialog.dismiss();
                }
                unfreezeUI();
                break;
            // 网络错误：由网络连通性、超时等原因引起
            case SMUHandler.UIUpdateMessages.NETWORK_ERROR:
                if (refreshingCheckCodeDialog.isShowing()){
                    refreshingCheckCodeDialog.dismiss();
                }
                if (loggingDialog.isShowing()){
                    loggingDialog.dismiss();
                }
                errorDialog.setTitle("网络错误");
                errorDialog.setContent("发生一个网络错误：\n" + ((NetworkException)data).getMessage());
                errorDialog.show();
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 服务器返回错误码：如404，403等
            case SMUHandler.UIUpdateMessages.SERVER_ERROR:
                if (refreshingCheckCodeDialog.isShowing()){
                    refreshingCheckCodeDialog.dismiss();
                }
                if (loggingDialog.isShowing()){
                    loggingDialog.dismiss();
                }
                errorDialog.setTitle("服务器错误");
                errorDialog.setContent("服务器返回一个错误码：\n" + ((NetworkException)data).getMessage());
                errorDialog.show();
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 验证码错误
            case SMUHandler.UIUpdateMessages.CHECK_CODE_WRONG:
                if (refreshingCheckCodeDialog.isShowing()){
                    refreshingCheckCodeDialog.dismiss();
                }
                if (loggingDialog.isShowing()){
                    loggingDialog.dismiss();
                }
                errorDialog.setTitle("用户登录");
                errorDialog.setContent("您输入的验证码有误，请重新输入！");
                errorDialog.show();
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 用户名或密码错误
            case SMUHandler.UIUpdateMessages.USR_OR_PWD_WRONG:
                if (refreshingCheckCodeDialog.isShowing()){
                    refreshingCheckCodeDialog.dismiss();
                }
                if (loggingDialog.isShowing()){
                    loggingDialog.dismiss();
                }
                errorDialog.setTitle("用户登录");
                errorDialog.setContent("用户名或者密码错误，请重新输入正确的用户名或密码！");
                errorDialog.show();
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 用户已登录
            case SMUHandler.UIUpdateMessages.ALREADY_LOGIN:
                break;
            // 用户登录成功
            case SMUHandler.UIUpdateMessages.LOGIN_SUCCESS:
                break;
            default:
                releaseImageViewResource(checkCodeImg);
                resetCheckCodImg();
                unfreezeUI();
        }
    }

    private void setupUI(){
        usernameTxt = findViewById(R.id.usernameEditText);
        passwordTxt = findViewById(R.id.passwordEditText);
        checkCodeTxt = findViewById(R.id.checkCodeEditText);
        checkCodeImg = findViewById(R.id.checkCodeImageView);
        loginBtn = findViewById(R.id.loginButton);
        // 设置对话框
        refreshingCheckCodeDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .title("验证码")
                .content("正在刷新验证码，请稍后...")
                .progress(true, 0)
                .build();
        loggingDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .title("用户登录")
                .content("正在登录，请稍后...")
                .progress(true, 0)
                .build();

        errorDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .positiveText("确定")
                .negativeText("取消")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();

    }
    private void setupUIEventListener(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.checkCodeImageView:
                        refreshCheckCode();
                        break;
                    case R.id.loginButton:
                        login();
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

    private void resetCheckCodImg(){
       Bitmap refreshCheckCodeBp = BitmapFactory.decodeResource(getResources(), R.drawable.refresh_check_code);
        checkCodeImg.setImageBitmap(refreshCheckCodeBp);
    }

    public static void releaseImageViewResource(ImageView imageView) {
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

    // 客户端网络请求的简单封装
    private void refreshCheckCode(){
        freezeUI();
        refreshingCheckCodeDialog.show();
        smuClient.refreshCheckCode();
    }

    private void login(){
        String username = usernameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String checkCode = checkCodeTxt.getText().toString();

        String info = "";
        if (username.equals(""))
            info += "用户名不能为空\n";
        if (password.equals(""))
            info += "密码不能为空\n";
        if (checkCode.equals(""))
            info += "验证码不能为空";

        if (!info.equals("")){
            errorDialog.setTitle("用户登录");
            errorDialog.setContent(info);
            errorDialog.show();
            return;
        }

        freezeUI();
        loggingDialog.show();
        smuClient.login(username, password, checkCode);
    }
}
