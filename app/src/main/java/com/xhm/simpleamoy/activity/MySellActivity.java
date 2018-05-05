package com.xhm.simpleamoy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vondear.rxtools.RxActivityTool;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;

public class MySellActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sell);
        initToolbar("我的卖出", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
    }
}
