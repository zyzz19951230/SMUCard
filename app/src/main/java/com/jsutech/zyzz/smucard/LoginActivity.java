package com.jsutech.zyzz.smucard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;
import com.jsutech.zyzz.smucard.network.exceptions.NetworkException;

public class LoginActivity extends BaseSMUActivity {

    private final static String TAG = "LoginActivity";

    private EditText usernameTxt;
    private EditText passwordTxt;
    private EditText checkCodeTxt;
    private ImageView checkCodeImg;
    private Button loginBtn;

    // 进度对话框：正在刷新验证码
    MaterialDialog refreshingCheckCodeDialog;
    // 进度对话框：正在登录
    MaterialDialog loggingDialog;
    // 对话框：显示错误信息
    MaterialDialog errorDialog;
    // 对话框：显示一般信息
    MaterialDialog infoDialog;
    private SMUClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI();
        setupEventListener();
        client = ((SMUApplication)getApplication()).getClient();
        client.setSMUHandler(new SMUHandler(this));
    }

    @Override
    public void onClientMessageReceived(int msgId, Object data) {
        switch (msgId){
            // 刷新验证码：收到新的验证码
            case SMUClient.ClientMessages.RECEIVE_CHECK_CODE:
                // 设置新的验证码图片
                setCheckCodeImg((Bitmap)data);
                dismissDialogs();
                unfreezeUI();
                break;
            // 网络错误：由网络连通性、超时等原因引起
            case SMUClient.ClientMessages.NETWORK_ERROR:
                dismissDialogs();
                NetworkException e = (NetworkException) data;
                errorDialog.setContent(e.getLocalizedMessage());
                errorDialog.show();
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 服务器返回错误码：如404，503等
            case SMUClient.ClientMessages.SERVER_ERROR:
                dismissDialogs();
                errorDialog.setContent("服务器错误：\n" + ((NetworkException)data).getMessage());
                errorDialog.show();
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 验证码错误
            case SMUClient.ClientMessages.CHECK_CODE_WRONG:
                dismissDialogs();
                errorDialog.setContent("验证码错误，请重新输入！");
                errorDialog.show();
                checkCodeTxt.setText("");
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 用户名或密码错误
            case SMUClient.ClientMessages.USR_OR_PWD_WRONG:
                dismissDialogs();
                errorDialog.setContent("用户名或者密码错误！");
                errorDialog.show();
                checkCodeTxt.setText("");
                passwordTxt.setText("");
                resetCheckCodImg();
                unfreezeUI();
                break;
            // 用户已登录
            case SMUClient.ClientMessages.ALREADY_LOGIN:
                break;
            // 用户登录成功
            case SMUClient.ClientMessages.LOGIN_SUCCESS:
                dismissDialogs();
                releaseImageViewResource();
                // 跳转到主界面
                jumpToMainActivity(usernameTxt.getText().toString());
                break;
            default:
                dismissDialogs();
                resetCheckCodImg();
                unfreezeUI();
        }
    }

    @Override
    public SMUClient getClient() {
        return client;
    }

    private void setupUI(){
        usernameTxt = findViewById(R.id.usernameEditText);
        passwordTxt = findViewById(R.id.passwordEditText);
        checkCodeTxt = findViewById(R.id.checkCodeEditText);
        checkCodeImg = findViewById(R.id.checkCodeImageView);
        loginBtn = findViewById(R.id.loginButton);
        String loginID = getIntent().getStringExtra("loginID");
        if (loginID != null){
            usernameTxt.setText(loginID);
        }
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
                .title("错误")
                .positiveText("确定")
                .negativeText("取消")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();
        infoDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .title("信息")
                .positiveText("确定")
                .negativeText("取消")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).build();

    }
    private void setupEventListener(){
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
        releaseImageViewResource();
       Bitmap refreshCheckCodeBp = BitmapFactory.decodeResource(getResources(), R.drawable.refresh_check_code);
        checkCodeImg.setImageBitmap(refreshCheckCodeBp);
    }

    public void releaseImageViewResource() {
        Drawable drawable = checkCodeImg.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                Log.d(TAG, "recycle unused bitmap.");
                bitmap.recycle();
            }
        }
    }

    private void setCheckCodeImg(Bitmap bitmap){
        releaseImageViewResource();
        checkCodeImg.setImageBitmap(bitmap);
    }

    // 对客户端网络请求的简单封装
    private void refreshCheckCode(){
        freezeUI();
        refreshingCheckCodeDialog.show();
        client.refreshCheckCode();
    }

    private void dismissDialogs(){
        if (refreshingCheckCodeDialog.isShowing())
            refreshingCheckCodeDialog.dismiss();
        if (errorDialog.isShowing())
            errorDialog.dismiss();
        if (loggingDialog.isShowing())
            loggingDialog.dismiss();
        if (infoDialog.isShowing())
            infoDialog.dismiss();
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
            infoDialog.setContent(info);
            infoDialog.show();
            return;
        }
        freezeUI();
        loggingDialog.show();
        client.login(username, password, checkCode);
    }

    // 跳转到主页面
    private void jumpToMainActivity(String loginID){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("loginID", loginID);
        startActivity(intent);
        releaseImageViewResource();
        finish();
    }
}
