package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.FrameLayout;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.fragment.FirstPagerFragment;
import com.xhm.simpleamoy.fragment.IssueFragment;
import com.xhm.simpleamoy.fragment.PersonFragment;
import com.xhm.simpleamoy.utils.BottomNavigationViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
