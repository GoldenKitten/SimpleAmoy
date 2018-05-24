package com.xhm.simpleamoy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetPersonDataFun;
import com.xhm.simpleamoy.data.db.ModifyPersonData;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.School;
import com.xhm.simpleamoy.data.entity.User;
import com.xhm.simpleamoy.utils.Validator;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonDataActivity extends BaseActivity {
    @BindView(R.id.et_apd_username)
    EditText etApdUsername;
    @BindView(R.id.et_apd_school)
    EditText etApdSchool;
    @BindView(R.id.et_apd_mail)
    EditText etApdMail;
    @BindView(R.id.bt_apd_modify)
    Button btApdModify;
    private Activity mActivity;
    private School mSchool;
    private List<String> mProList;
    private List<String> mCityList;
    private List<String> mSchoolList;
    private String mSchoolAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mActivity = this;
        initToolbar("我的购买", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
        mSchoolAddress=RxSPTool.getString(MyApp.newInstance(),C.Splash.SCHOOLADDRESS);
        initData();
    }

    private void initData() {
        File file=new File(getFilesDir(),"school.json");
        Gson gson=new Gson();
        try {
            Reader reader=new FileReader(file);
            mSchool = gson.fromJson(reader,School.class);
            mProList=new ArrayList<>();
            mCityList=new ArrayList<>();
            mSchoolList=new ArrayList<>();
            mProList.add("");
            for(School.DataBean dataBean:mSchool.getData()){
                mProList.add(dataBean.getDepartName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() -> new GetPersonDataFun(RxSPTool.getString(MyApp.newInstance(),
                C.Splash.USERNAME)){
            @Override
            public void getPersonDataSucess(User user) {
                rxDialogLoading.cancel();
                Event<Object> event=new Event<Object>("GetPersonDataSucess",user);
                EventBus.getDefault().post(event);
            }

            @Override
            public void getPersonDataFaild(String msg) {
                rxDialogLoading.cancel();
                RxToast.error(msg);
                RxActivityTool.finishActivity(mContext);
            }
        }).start();
    }
@Subscribe(threadMode = ThreadMode.MAIN)
public void eventDeal(Event<Object> event){
        if(event.getMsg().equals("GetPersonDataSucess")){
            User user= (User) event.getData();
            etApdUsername.setText(user.getUsername());
            etApdSchool.setText(user.getSchool());
            etApdMail.setText(user.getMail());
            etApdUsername.setEnabled(false);
            etApdSchool.setFocusable(false);
            etApdSchool.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final RxDialog rxDialog = new RxDialog(mContext);//提示弹窗
                    View view=View.inflate(mContext,R.layout.custom_school_choose,null);
                    NiceSpinner nsPro=(NiceSpinner) view.findViewById(R.id.ns_pro);
                    NiceSpinner nsCity=(NiceSpinner)view.findViewById(R.id.ns_city);
                    NiceSpinner nsSchool=(NiceSpinner)view.findViewById(R.id.ns_school);
                    Button btSumbit=(Button)view.findViewById(R.id.bt_csc_sumbit);
                    nsPro.attachDataSource(mProList);
                    rxDialog.setContentView(view);
                    rxDialog.setCancelable(false);
                    rxDialog.setFullScreen();
                    rxDialog.show();
                    nsPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mCityList.removeAll(mCityList);
                            mCityList.add("");
                            for(School.DataBean.CollegeLocationsBean coll:
                                    mSchool.getData().get(position-1).getCollegeLocations()){
                                mCityList.add(coll.getLocationName());
                            }
                            nsCity.attachDataSource(mCityList);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    nsCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mSchoolList.removeAll(mSchoolList);
                            mSchoolList.add("");
                            for(School.DataBean.CollegeLocationsBean.CollegeNamesBean schoolName:
                                    mSchool.getData()
                                            .get(nsPro.getSelectedIndex()-1)
                                            .getCollegeLocations()
                                            .get(nsCity.getSelectedIndex()-1)
                                            .getCollegeNames()
                                    )
                                mSchoolList.add(schoolName.getName());
                            nsSchool.attachDataSource(mSchoolList);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    nsSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mSchoolAddress=mSchoolList.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    btSumbit.setOnClickListener(v1 -> {
                        rxDialog.cancel();
                        etApdSchool.setText(mSchoolAddress);
                    });
                }
            });
        }
}
    @OnClick(R.id.bt_apd_modify)
    public void onViewClicked() {
    if(!TextUtils.isEmpty(etApdMail.getText().toString())){
        if(!Validator.isEmail(etApdMail.getText().toString())) {
            RxToast.error("请输入正确的邮箱");
        }
       else {
            User user=new User();
            user.setSchool(mSchoolAddress);
            user.setMail(etApdMail.getText().toString());
            user.setUsername(RxSPTool.getString(MyApp.newInstance(),
                    C.Splash.USERNAME));
            RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
            rxDialogLoading.setLoadingText("修改中 ...");
            rxDialogLoading.setCancelable(false);
            rxDialogLoading.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ModifyPersonData(user){
                        @Override
                        public void modifySucess() {
                           rxDialogLoading.cancel();
                           RxToast.success("修改成功");
                           RxSPTool.putString(MyApp.newInstance(),
                                   C.Splash.SCHOOLADDRESS,mSchoolAddress);
                        }

                        @Override
                        public void modifyFailed(String msg) {
                            rxDialogLoading.cancel();
                            RxToast.error(msg);
                        }
                    };
                }
            }).start();
        }
    }
    else {
            RxToast.error("邮箱不能为空");
        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
