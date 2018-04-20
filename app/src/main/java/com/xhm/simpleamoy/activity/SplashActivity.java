package com.xhm.simpleamoy.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_as_root)
    RelativeLayout rlAsRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.FLAG_FULLSCREEN(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        RxPermissionsTool.with(this)
                .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.CALL_PHONE)
                .addPermission(Manifest.permission.READ_PHONE_STATE)
                .initPermission();
        initAnimation();
    }
    private void initAnimation() {
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
                if(RxSPTool.getBoolean(SplashActivity.this,
                        C.Splash.ALREADY_LOGIN)){
                    RxActivityTool.skipActivityAndFinish(SplashActivity.this,
                            MainActivity.class);
                }
                else {
                    RxActivityTool.skipActivityAndFinish(SplashActivity.this,
                            LoginActivity.class);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
