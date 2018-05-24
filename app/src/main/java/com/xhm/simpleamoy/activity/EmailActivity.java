package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxRegTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetMailInfo;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.MailInfo;
import com.xhm.simpleamoy.data.entity.MailSenderInfo;
import com.xhm.simpleamoy.utils.SimpleMailSender;
import com.xhm.simpleamoy.utils.Validator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码界面
 */
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
        EventBus.getDefault().register(this);
        initToolbar("忘记密码", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
    }

    @OnClick(R.id.bt_ok)
    public void onViewClicked() {
        if(checkInfo(tietUsername.getText().toString(),
                tletEmail.getText().toString())){
            final RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
            rxDialogLoading.setLoadingText("验证中...");
            rxDialogLoading.setCancelable(false);
            rxDialogLoading.show();
            new Thread(() -> new GetMailInfo(tietUsername.getText().toString(),
                    tletEmail.getText().toString()){
                @Override
                public void getMailInfoSucess(MailInfo mailInfo) {
                    rxDialogLoading.cancel();
                    Event<Object> event=new Event<Object>("GetMainSucess",mailInfo);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void getMailInfoFaild(String msg) {
                    rxDialogLoading.cancel();
                    RxToast.error(msg);
                }
            }).start();


        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void myEvent(Event<Object> event){
        if(event.getMsg().equals("GetMainSucess")){
            MailInfo mailInfo= (MailInfo) event.getData();
            final RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
            rxDialogLoading.setCancelable(false);
            rxDialogLoading.setLoadingText("邮件发送中...");
            rxDialogLoading.show();
            try {
                MailSenderInfo mailSenderInfo = new MailSenderInfo();
                mailSenderInfo.setMailServerHost("smtp.163.com");
                mailSenderInfo.setMailServerPort("25");
                mailSenderInfo.setValidate(true);
                mailSenderInfo.setUserName("simpleamoyofficial@163.com");  //你的邮箱地址
                mailSenderInfo.setPassword("SimpleAmoy163");//您的邮箱密码
                mailSenderInfo.setFromAddress("simpleamoyofficial@163.com");
                //和上面username的邮箱地址一致
                mailSenderInfo.setToAddress(mailInfo.getEmailAddress());
                mailSenderInfo.setSubject("恭喜"+mailInfo.getUserName()+"成功找回密码");
                mailSenderInfo.setContent("你的密码是:"+mailInfo.getPassword());

                //这个类主要来发送邮件
                new SimpleMailSender(mailSenderInfo){
                    @Override
                    public void sendSucess() {
                        rxDialogLoading.cancel();
                        Event<Object> event1=new Event<Object>("EmailSendSucess",null);
                        EventBus.getDefault().post(event1);

                    }

                    @Override
                    public void sendFailed(String msg) {
                        rxDialogLoading.cancel();
                        Event<Object> event1=new Event<Object>("EmailSendFailed",msg);
                        EventBus.getDefault().post(event1);

                    }
                };

            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
                rxDialogLoading.cancel();
                RxToast.error(mContext,e.toString());
            }
        }
        if(event.getMsg().equals("EmailSendSucess")){
            RxToast.success("请前往你的邮箱获取密码,如果未收到邮件," +
                    "请查看垃圾箱里是否有该邮件");
        }
        if(event.getMsg().equals("EmailSendFailed")){
            RxToast.error("邮件发送失败，请重新发送");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
