package com.xhm.simpleamoy.Base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.vondear.rxtools.RxActivityTool;
import com.xhm.simpleamoy.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/20.
 */

public class BaseActivity extends AppCompatActivity {
    public BaseActivity mContext;
    public static List<Activity> mActivitys=new ArrayList<>();
    private ForceOfflineReceiver mReceiver;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        RxActivityTool.addActivity(this);
        //addActivity(this);
    }
    public static void addActivity(Activity activity){
        mActivitys.add(activity);
    }
    public static void removeActivity(Activity activity){
        mActivitys.remove(activity);
    }
    public static void finishAllActivity(){
        for(Activity activity:mActivitys){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
    public void sendForceOffline(){
        Intent intent=new Intent("com.xhm.simpleamoy.FORCE_OFFLINE");
        sendBroadcast(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.xhm.simpleamoy.FORCE_OFFLINE");
        mReceiver=new ForceOfflineReceiver();
        registerReceiver(mReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mReceiver!=null){
            unregisterReceiver(mReceiver);
            mReceiver=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //removeActivity(this);
    }
    class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("警告")
                    .setMessage("你被强制下线，请重新登录")
                    .setCancelable(false)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAllActivity();
                            Intent loginIntent=new Intent(mContext,
                                    LoginActivity.class);
                            mContext.startActivity(loginIntent);

                        }
                    })
                    .show();
        }
    }
}
