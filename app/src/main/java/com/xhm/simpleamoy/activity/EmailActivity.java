package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.vondear.rxtools.RxRegTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetMailInfo;
import com.xhm.simpleamoy.data.entity.MailInfo;
import com.xhm.simpleamoy.data.entity.MailSenderInfo;
import com.xhm.simpleamoy.utils.SimpleMailSender;
import com.xhm.simpleamoy.utils.Validator;

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
        initToolbar("忘记密码", R.drawable.ic_back);
    }

    @OnClick(R.id.bt_ok)
    public void onViewClicked() {
        if(checkInfo(tietUsername.getText().toString(),
                tletEmail.getText().toString())){
            RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
            rxDialogLoading.setLoadingText("验证中...");
            rxDialogLoading.setCancelable(false);
            rxDialogLoading.show();
            new Thread(() -> new GetMailInfo(tietUsername.getText().toString(),
                    tletEmail.getText().toString()){
                @Override
                public void getMailInfoSucess(MailInfo mailInfo) {
                    rxDialogLoading.setLoadingText("邮件发送中...");
                    if(sendEmail(mailInfo)){
                        rxDialogLoading.cancel();
                        RxToast.success("请前往你的邮箱获取密码");
                    }
                    else {
                        rxDialogLoading.cancel();
                        RxToast.error("邮件发送失败，请重新发送");
                    }
                }

                @Override
                public void getMailInfoFaild(String msg) {
                    rxDialogLoading.cancel();
                    RxToast.error(msg);
                }
            }).start();
        }
    }

    private boolean checkInfo(String username,String mailAddress) {
        if(TextUtils.isEmpty(username)){
            RxToast.error("用户名不能为空");
            return false;
        }
        if(!RxRegTool.isUsername(username)){
            RxToast.error("用户名取值范围为a-z,A-Z,0-9," +
                    "\"_\",汉字，不能以\"_\"结尾," +
                    "用户名必须是6-20位");
            return false;
        }
        if(TextUtils.isEmpty(mailAddress)){
            RxToast.error("邮箱不能为空");
            return false;
        }
        if(!Validator.isEmail(mailAddress)){
            RxToast.error("请输入正确的邮箱");
            return false;
        }
        return true;
    }

    private boolean sendEmail(MailInfo myMailInfo) {
        try {
            MailSenderInfo mailInfo = new MailSenderInfo();
            mailInfo.setMailServerHost("smtp.163.com");
            mailInfo.setMailServerPort("25");
            mailInfo.setValidate(true);
            mailInfo.setUserName("SimpleAmoyOfficial@163.com");  //你的邮箱地址
            mailInfo.setPassword("SimpleAmoy163");//您的邮箱密码
            mailInfo.setFromAddress("SimpleAmoyOfficial@163.com");//和上面username的邮箱地址一致
            mailInfo.setToAddress(myMailInfo.getEmailAddress());
            mailInfo.setSubject("恭喜"+myMailInfo.getUserName()+"成功找回密码");
            mailInfo.setContent("你的密码是:"+myMailInfo.getPassword());

            //这个类主要来发送邮件
            SimpleMailSender sms = new SimpleMailSender();
            boolean b = sms.sendTextMail(mailInfo);//发送文体格式,返回是否发送成功的boolean类型

            //Log.e("MainActivity", "MainActivity sendEmail()" + b);//sms.sendHtmlMail(mailInfo);//发送html格式
            return b;
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            return false;
        }
    }
}
