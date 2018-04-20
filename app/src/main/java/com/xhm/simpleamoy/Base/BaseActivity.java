package com.xhm.simpleamoy.Base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vondear.rxtools.RxActivityTool;

/**
 * Created by xhm on 2018/4/20.
 */

public class BaseActivity extends AppCompatActivity {
    public BaseActivity mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        RxActivityTool.addActivity(this);
    }


}
