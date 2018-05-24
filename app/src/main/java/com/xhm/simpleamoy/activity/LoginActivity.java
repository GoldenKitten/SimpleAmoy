package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxKeyboardTool;
import com.vondear.rxtools.RxRegTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.LoginFun;
import com.xhm.simpleamoy.data.entity.LoginUser;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.iv_clean_phone)
    ImageView ivCleanPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.clean_password)
    ImageView cleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView ivShowPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.ll_al_content)
    LinearLayout llAlContent;
    @BindView(R.id.rl_al_root)
    RelativeLayout rlAlRoot;
    private LoginUser mLoginUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initView() {
        etMobile.setText(RxSPTool.getString(MyApp.newInstance(),
                C.Splash.USERNAME));
        etPassword.setText(RxSPTool.getString(MyApp.newInstance(),
                C.Splash.PASSWORD));
    }

    private void initEvent() {
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivCleanPhone.getVisibility() == View.GONE) {
                    ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    ivCleanPhone.setVisibility(View.GONE);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && cleanPassword.getVisibility() == View.GONE) {
                    cleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    cleanPassword.setVisibility(View.GONE);
                }
                if (s.toString().isEmpty())
                    return;
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    Toast.makeText(LoginActivity.this, "请输入数字或字母", Toast.LENGTH_SHORT).show();
                    s.delete(temp.length() - 1, temp.length());
                    etPassword.setSelection(s.length());
                }
            }
        });

    }

    @OnClick({R.id.iv_clean_phone, R.id.clean_password, R.id.iv_show_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                etMobile.setText("");
                break;
            case R.id.clean_password:
                etPassword.setText("");
                break;
            case R.id.iv_show_pwd:
                if (etPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowPwd.setImageResource(R.drawable.pass_visuable);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowPwd.setImageResource(R.drawable.pass_gone);
                }
                String pwd = etPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etPassword.setSelection(pwd.length());
                break;
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if(mLoginUser==null){
            mLoginUser=new LoginUser();
        }
        if(checkLoginState(mLoginUser)){
            RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
            rxDialogLoading.setLoadingText("登录中...");
            rxDialogLoading.setCancelable(false);
            rxDialogLoading.show();
            new Thread(() -> new LoginFun(mLoginUser){
                @Override
                public void loginSucess() {
                    rxDialogLoading.cancel();
                    RxToast.success("登录成功");
                    RxSPTool.putString(MyApp.newInstance(),
                            C.Splash.USERNAME,mLoginUser.getUsername());
                    RxSPTool.putString(MyApp.newInstance(),
                            C.Splash.PASSWORD,mLoginUser.getPassword());
                    RxSPTool.putBoolean(MyApp.newInstance(),
                            C.Splash.IS_LOGIN,true);
                    RxActivityTool.finishActivity(mContext);
                    RxActivityTool.skipActivity(mContext,MainActivity.class);
                }

                @Override
                public void loginFaild(String msg) {
                    rxDialogLoading.cancel();
                    RxToast.error(msg);
                }
            }).start();
        }

    }

    private boolean checkLoginState(LoginUser loginUser) {
        if(TextUtils.isEmpty(etMobile.getText().toString())){
            RxToast.error(mContext,"用户名不能为空").show();
            return false;
        }
        if(!RxRegTool.isUsername(etMobile.getText().toString())){
            RxToast.error("用户名取值范围为a-z,A-Z,0-9," +
                    "\"_\",汉字，不能以\"_\"结尾," +
                    "用户名必须是6-20位");
            return false;
        }
        if(TextUtils.isEmpty(etPassword.getText().toString())){
            RxToast.error("密码不能为空");
            return false;
        }
        if(!Validator.isPassword(etPassword.getText().toString())){
            RxToast.error("密码取值范围为a-z,A-Z,0-9,不包含特殊字符，长度大于6");
            return false;
        }
        loginUser.setUsername(etMobile.getText().toString());
        loginUser.setPassword(etPassword.getText().toString());
        return true;
    }

    @OnClick({R.id.regist, R.id.forget_password})
    public void onRegistAndForgetPsdClicked(View view) {
        switch (view.getId()) {
            case R.id.regist:
                RxActivityTool.skipActivity(this,RegistActivity.class);
                break;
            case R.id.forget_password:
                RxActivityTool.skipActivity(this,EmailActivity.class);
                break;
        }
    }
}
