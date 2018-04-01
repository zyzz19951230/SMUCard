package com.jsutech.zyzz.smucard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsutech.zyzz.smucard.db.DBManager;
import com.jsutech.zyzz.smucard.db.models.UserProfile;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;
import com.jsutech.zyzz.smucard.ui.fragments.BaseFragment;
import com.jsutech.zyzz.smucard.ui.fragments.ChargingFragment;
import com.jsutech.zyzz.smucard.ui.fragments.ICommunicator;
import com.jsutech.zyzz.smucard.ui.fragments.UpdateMessages;
import com.jsutech.zyzz.smucard.ui.fragments.UserInfoFragment;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseSMUActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";
    private DrawerLayout drawer;
    private ImageView userPhotoImage;
    private TextView usernameTextView;
    private TextView suidTextView;
    private Map<String, BaseFragment> fragments;
    private FragmentManager fragmentManager;
    private SMUClient client;
    private UserProfile userProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 设置界面
        setupUI();
        setupFragments();
        setupClient();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // 获取loginID
        Intent intent = getIntent();
        String loginID = intent.getStringExtra("loginID");
         // 校验loginID的合法性
        if (loginID == null || loginID.equals("")){
            jumpToLoginActivity();
        }
        // 从数据库中查询是否已有相应的记录
        // 若已存在相应记录，则使用该记录；若不存在相应的记录则从请求网络
        userProfile = DBManager.findUserProfile(loginID);
        if (userProfile != null){
            // 使用数据库记录来更新界面
            // 首先更新Activity界面
            updateNavHeader();
            // 接着，向Fragment发送消息，更新Fragment中的界面
            sendMessage(fragments.get(UserInfoFragment.class.getName()), UpdateMessages.UPDATE_USER_PROFILE, userProfile);
            Log.d(TAG, "From DB.");
        } else {
            userProfile = new UserProfile();
            userProfile.setSUID(loginID);
            DBManager.saveOrUpdateUserProfile(userProfile);
            // 执行网络请求
            client.requestUserProfile();
            client.requestUserPhoto();
            Log.d(TAG, "From Net");
        }
    }

    private void updateNavHeader() {
        String username = userProfile.getName();
        String suid = userProfile.getSUID();
        byte[] userPhoto = userProfile.getPhoto();
        if (username != null && !username.equals("")){
            usernameTextView.setText((userProfile.getName()));
        }

        if (suid != null && suid.equals("")){
            suidTextView.setText(userProfile.getSUID());
        }

        if (userPhoto != null){
            releaseImageViewResource(userPhotoImage);
            userPhotoImage.setImageBitmap(BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length));
        }
    }


    private void jumpToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupClient() {
        client = ((SMUApplication)getApplication()).getClient();
        client.setSMUHandler(new SMUHandler(this));
    }

    private void setupFragments() {
        fragments = new HashMap<>();
        fragmentManager = getSupportFragmentManager();
        // 创建所有fragment
        BaseFragment userInfoFragment = new UserInfoFragment();
        BaseFragment chargingFragment = new ChargingFragment();
        // 将所有的fragment置入map
        fragments.put(UserInfoFragment.class.getName(), userInfoFragment);
        fragments.put(ChargingFragment.class.getName(), chargingFragment);
        // 向FragmentManager中添加所有fragment
        // 并隐藏所有fragment
        fragmentManager.beginTransaction()
                .add(R.id.main_content, userInfoFragment, UserInfoFragment.class.getName())
                .hide(userInfoFragment)
                .add(R.id.main_content, chargingFragment, ChargingFragment.class.getName())
                .hide(chargingFragment)
                .commit();
        // 切换到首页
        switchFragment(UserInfoFragment.class.getName());
    }

    private void switchFragment(String fragmentTag){
        BaseFragment fragment = fragments.get(fragmentTag);
        if (fragment == null){
            fragment = (BaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null){
                fragments.put(fragmentTag, fragment);
            } else {
                throw new RuntimeException("Fragment instance does not exist.");
            }
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (BaseFragment f : fragments.values()){
            if (f == fragment){
                fragmentTransaction.show(f);
            } else {
                fragmentTransaction.hide(f);
            }
        }
        fragmentTransaction.commit();
    }


    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.titleBar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        navigationView.addHeaderView(headerView);
        userPhotoImage = headerView.findViewById(R.id.user_photo_image_view_2);
        usernameTextView = headerView.findViewById(R.id.username_text_view);
        suidTextView = headerView.findViewById(R.id.suid_text_view);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.home_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 处理抽屉菜单点击事件
        switch (item.getItemId()){
            case R.id.nav_user_info:
                switchFragment(UserInfoFragment.class.getName());
                break;
            case R.id.nav_charging:
                switchFragment(ChargingFragment.class.getName());
                break;
            case R.id.nav_billing:
                break;
            case R.id.nav_card_manage:
                break;
            case R.id.nav_contact:
                break;
            case R.id.nav_share_app:
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_user_agreement:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *  Activity 与 SMUClient进行通信的接口
     * @param msgId
     * @param data
     */
    @Override
    public void onClientMessageReceived(int msgId, Object data) {
        // 在activity中除处理的事件
        switch (msgId){
            case SMUClient.ClientMessages.RECEIVE_USER_PHOTO:
                // 接收用户照片
                Bitmap userPhoto = (Bitmap)data;
                // 转成byte数组
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                userPhoto.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                userProfile.setPhoto(byteArrayOutputStream.toByteArray());
                updateNavHeader();
                sendMessage(fragments.get(UserInfoFragment.class.getName()), UpdateMessages.UPDATE_USER_PROFILE, userProfile);
                // 更新数据库
                DBManager.saveOrUpdateUserProfile(userProfile);
                break;
            case SMUClient.ClientMessages.RECEIVE_USER_PROFILE:
                // 接收UserProfile
                UserProfile profile = (UserProfile) data;
                userProfile.update(profile);
                // 更新界面
                updateNavHeader();
                sendMessage(fragments.get(UserInfoFragment.class.getName()), UpdateMessages.UPDATE_USER_PROFILE, userProfile);
                // 更新数据库
                DBManager.saveOrUpdateUserProfile(userProfile);
                break;
        }
    }

    @Override
    public SMUClient getClient() {
        return client;
    }

    /***
     *  Activity 与 Fragment进行通信的接口
     * @param msgID
     * @param data
     * @param sender
     */
    @Override
    public void onMessageReceived(int msgID, Object data, ICommunicator sender) {
        switch (msgID){
            case UpdateMessages.REFRESH_USER_PHOTO:
                client.requestUserPhoto();
                break;
            case UpdateMessages.REFRESH_USER_PROFILE:
                client.requestUserProfile();
                break;
        }
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
