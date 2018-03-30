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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.jsutech.zyzz.smucard.fragments.UserInfoFramgment;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;

public class HomeActivity extends SMUBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SMUClient smuClient;
    private DrawerLayout drawer;
    private Fragment userInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 设置界面
        setupUI();

        // 获取客户端对象
        smuClient = ((SMUApplication)getApplication()).getClient();
        // 设置handler对象
        SMUHandler smuHandler = new SMUHandler(this);
        smuClient.setSmuHandler(smuHandler);

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

        // fragments
        userInfoFragment = new UserInfoFramgment();
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
                Toast.makeText(this, "nav_user_info", Toast.LENGTH_SHORT).show();
                replaceFragment(userInfoFragment);
                break;
            case R.id.nav_charging:
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();

    }
}
