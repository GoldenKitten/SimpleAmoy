package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailActivity extends BaseActivity {

    @BindView(R.id.tiet_username)
    TextInputEditText tietUsername;
    @BindView(R.id.tl_username_wrapper)
    TextInputLayout tlUsernameWrapper;
    @BindView(R.id.tlet_email)
    TextInputEditText tletEmail;
    @BindView(R.id.tl_email_wrapper)
    TextInputLayout tlEmailWrapper;
    @BindView(R.id.bt_ok)
    Button btOk;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        ButterKnife.bind(this);
        initToolbar("忘记密码",R.drawable.ic_back);
    }

    @OnClick(R.id.bt_ok)
    public void onViewClicked() {
    }
}
