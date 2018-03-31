package com.jsutech.zyzz.smucard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jsutech.zyzz.smucard.db.models.UserProfile;
import com.jsutech.zyzz.smucard.ui.fragments.BaseSMUFragment;
import com.jsutech.zyzz.smucard.ui.fragments.ChargingFragment;
import com.jsutech.zyzz.smucard.ui.fragments.UserInfoFragment;
import com.jsutech.zyzz.smucard.network.ISMUClientReceiver;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseSMUActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";
    private SMUClient smuClient;
    private DrawerLayout drawer;
    private BaseSMUFragment userInfo;
    private BaseSMUFragment charging;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 设置界面
        setupUI();
        setupEventListener();

        // 获取客户端对象
        smuClient = ((SMUApplication)getApplication()).getClient();
        // 设置handler对象
        SMUHandler smuHandler = new SMUHandler(this);
        smuClient.setSMUHandler(smuHandler);
        Log.d(TAG, "current handler:"  + smuClient.getCurrentSMUHandler());
        userInfo = new UserInfoFragment();
        charging = new ChargingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_content,userInfo, UserInfoFragment.class.getName());
        transaction.add(R.id.main_content, charging, ChargingFragment.class.getName());
        transaction.hide(charging);
        transaction.show(userInfo);
        transaction.commit();


    }

    private void setupEventListener() {

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
                if (userInfo.isHidden()){
                    userInfo.switchContext();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.hide(charging);
                    transaction.show(userInfo);
                    transaction.commit();
                }
                break;
            case R.id.nav_charging:
                if (charging.isHidden()){
                    charging.switchContext();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.hide(userInfo);
                    transaction.show(charging);
                    transaction.commit();
                }
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

    @Override
    public void onClientMessageReceived(int msgId, Object data) {

    }

    @Override
    public SMUClient getSMUClient() {
        return smuClient;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();

    }


}
