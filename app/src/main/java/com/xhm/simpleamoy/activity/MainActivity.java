package com.xhm.simpleamoy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.DataManager;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.fragment.FirstPagerFragment;
import com.xhm.simpleamoy.fragment.IssueFragment;
import com.xhm.simpleamoy.fragment.PersonFragment;
import com.xhm.simpleamoy.utils.BottomNavigationViewUtil;
import com.xhm.simpleamoy.utils.FileUtil;
import com.xhm.simpleamoy.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


public class MainActivity extends BaseActivity {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    private static final int REQUEST_MAIN_IMAGE = 1;
    private static final int REQUEST_IMAGE = 2;
    private List<byte[]> mImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        //initData();
    }

    private void initData() {
        new Thread(() -> {
            new DataManager(RxSPTool.getString(
                    MyApp.newInstance(),
                    C.Splash.USERNAME),0){
                @Override
                public void getDataSucess(List list) {
                    RxSPTool.putString(MyApp.newInstance(),
                            C.Splash.SCHOOLADDRESS,(String) list.get(1));
                    Event event=new Event("HeadImageAndUserName",list);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void getDataFaild(String msg) {
                        RxToast.error(msg);
                }
            };
        }).start();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setImageAndUserName(Event<List> event){
        if(event.getMsg().equals("HeadImageAndUserName")) {
            View headView = navView.getHeaderView(0);
            ImageView imageView = (ImageView) headView.findViewById(R.id.civ_head_image);
            TextView textView = (TextView) headView.findViewById(R.id.tv_username);
            Glide.with(this)
                    .load((byte[]) event.getData().get(0))
                    .into(imageView);
            textView.setText(RxSPTool.getString(MyApp.newInstance(),
                    C.Splash.USERNAME));
        }
    }
    private void initView() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        FirstPagerFragment fPagerFragment =FirstPagerFragment.newInstance
                (mFragmentManager);
        transaction.replace(R.id.fl_content,fPagerFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
        initToolbar("首页", R.drawable.icon_menu);
        BottomNavigationViewUtil.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, getCustomToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.quit:
                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);//提示弹窗
                    //rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
                    rxDialogSureCancel.getSureView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RxActivityTool.skipActivityAndFinish(
                                    MainActivity.this,
                                    LoginActivity.class
                            );
                        }
                    });
                    rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSureCancel.cancel();
                        }
                    });
                    rxDialogSureCancel.show();
            }
            drawerLayout.closeDrawers();
            return true;
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case  R.id.bottom_first_pager:
                    FragmentTransaction fPtransaction = mFragmentManager.beginTransaction();
                    FirstPagerFragment firstPagerFragment =FirstPagerFragment.newInstance
                            (mFragmentManager);
                    fPtransaction.replace(R.id.fl_content,firstPagerFragment);
                    //transaction.addToBackStack(null);
                    fPtransaction.commit();
                    initToolbar("首页");
                    break;
                case  R.id.bottom_issue:
                    drawerLayout.setBackgroundResource(R.drawable.issue_bg);
                    FragmentTransaction iTransaction = mFragmentManager.beginTransaction();
                    IssueFragment issueFragment = IssueFragment.newInstance
                            (mFragmentManager);
                    EventBus.getDefault().register(issueFragment);
                    iTransaction.replace(R.id.fl_content,issueFragment);
                    //transaction.addToBackStack(null);
                    iTransaction.commit();
                    initToolbar("发布");
                    break;
                case  R.id.bottom_person:
                    drawerLayout.setBackgroundResource(R.drawable.person_bg);
                    FragmentTransaction pTransaction = mFragmentManager.beginTransaction();
                    PersonFragment personFragment = PersonFragment.newInstance
                            (mFragmentManager);
                    pTransaction.replace(R.id.fl_content,personFragment);
                    //transaction.addToBackStack(null);
                    pTransaction.commit();
                    initToolbar("个人");
                    break;
            }
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(
                        MultiImageSelectorActivity.EXTRA_RESULT);
                mImage=new ArrayList<byte[]>();
                for(String imagePath:path){
                    mImage.add(FileUtil.
                            getBytesFromFile(new File(imagePath)));
                }
                Event imageEvent=new Event("ImageEvent",mImage);
                EventBus.getDefault().post(imageEvent);
            }
        }
        if(requestCode == REQUEST_MAIN_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths

                List<String> path = data.getStringArrayListExtra(
                        MultiImageSelectorActivity.EXTRA_RESULT);
                byte[] mainImage=FileUtil
                        .getBytesFromFile(new File(path.get(0)));
                Event mainImageEvent=new Event("MainImageEvent",mainImage);
                EventBus.getDefault().post(mainImageEvent);
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
