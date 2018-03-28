package com.jsutech.zyzz.smucard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jsutech.zyzz.smucard.db.models.UserProfile;
import com.jsutech.zyzz.smucard.network.SMUClient;
import com.jsutech.zyzz.smucard.network.SMUHandler;

public class MainActivity extends SMUBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    // 已登录的ID
    private String loginID;
    private MaterialDialog infoDialog;
    private SMUClient smuClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 导航条
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 设置抽屉布局
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // 设置UI界面
        setupUI();
        // 获取客户端
        smuClient = ((SMUCardApplication)getApplication()).getClient();
        // 设置Handler
        SMUHandler smuHandler = new SMUHandler(this);
        smuClient.setSmuHandler(smuHandler);
        // 从传入的Intent中获取已登录的ID号
        loginID = getIntent().getStringExtra("loginID");
        // 若未获取到ID，则跳回到登录界面
        if (loginID == null || loginID.equals("")){
            //
            dismissDialogs();
            infoDialog.show();
        } else {
            // 请求用户基本信息
            UserProfile profile = requestUserProfile();
        }
    }

    private UserProfile requestUserProfile() {
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onUIUpdateMessageReceived(int msgId, Object data) {

    }

    // 回跳到登录界面
    private void jumpToLoginActivity(String loginID){
        Intent intent = new Intent(this, LoginActivity.class);
        if (loginID == null){
            loginID = "";
        }
        intent.putExtra("loginID", loginID);
        startActivity(intent);
        finish();
    }

    private void setupUI(){

        // 对话框
        infoDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .title("信息")
                .positiveText("确定")
                .content("用户未登录或会话已过期，请重新登录！")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        jumpToLoginActivity("");
                        dialog.dismiss();
                    }
                }).build();


    }

    private void dismissDialogs(){
        if (infoDialog.isShowing()){
            infoDialog.dismiss();
        }
    }
}
