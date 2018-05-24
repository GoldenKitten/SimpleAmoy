package com.xhm.simpleamoy.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.utils.LoadAssets;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动界面
 */
public class  SplashActivity extends BaseActivity {

    @BindView(R.id.rl_as_root)
    RelativeLayout rlAsRoot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.FLAG_FULLSCREEN(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //获取权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        initAnimation();
                    } else {
                        // At least one permission is denied
                        RxToast.error("该应用没有相应的权限");
                        finish();
                    }
                });
    }

    private void initData() {
        //加载学校地址数据
        LoadAssets.initData(this,"school.json");
    }

    private void initAnimation() {
        //加载动画
        //旋转
        RotateAnimation rotateAnimation=new RotateAnimation(
                0,360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );//基于自身360度旋转
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);//保持住动画结束状态
        //缩放
        ScaleAnimation scaleAnimation=new ScaleAnimation(
                0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f
        );
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        /*ObjectAnimator alpha = ObjectAnimator.ofFloat(rlSplRoot, "alpha", 0, 1)
                .setDuration(2000);
        alpha.start();*/



        //集合
        AnimationSet animationSet=new AnimationSet(false);
        // animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        rlAsRoot.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(RxSPTool.getBoolean(MyApp.newInstance(),
                        C.Splash.IS_LOGIN)){
                    RxActivityTool.finishActivity(SplashActivity.this);
                    RxActivityTool.skipActivity(SplashActivity.this,
                            MainActivity.class);
                }
                else {
                    RxSPTool.putString(MyApp.newInstance(),
                            C.Splash.USERNAME,"");
                    RxSPTool.putString(MyApp.newInstance(),
                            C.Splash.PASSWORD,"");
                    RxActivityTool.finishActivity(SplashActivity.this);
                    RxActivityTool.skipActivity(SplashActivity.this,
                            LoginActivity.class);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
