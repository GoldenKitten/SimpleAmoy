package com.xhm.simpleamoy.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxRegTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxCaptcha;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.RegistFun;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.data.entity.School;
import com.xhm.simpleamoy.utils.FileUtil;
import com.xhm.simpleamoy.utils.LogUtil;
import com.xhm.simpleamoy.utils.Validator;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.angmarch.views.NiceSpinner;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.vondear.rxtools.view.RxCaptcha.TYPE.CHARS;
import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

public class RegistActivity extends BaseActivity {

    @BindView(R.id.et_ar_username)
    EditText etArUsername;
    @BindView(R.id.et_ar_password)
    EditText etArPassword;
    @BindView(R.id.et_ar_affirm_password)
    EditText etArAffirmPassword;
    @BindView(R.id.et_ar_school_address)
    EditText etArSchoolAddress;
    @BindView(R.id.et_ar_idcode)
    EditText etArIdcode;
    @BindView(R.id.tv_ar_get_code)
    TextView tvArGetCode;
    @BindView(R.id.ib_ar_refresh_code)
    ImageButton ibArRefreshCode;
    @BindView(R.id.bt_ar_up_head_image)
    Button btArUpHeadImage;
    @BindView(R.id.iv_ar_head_image)
    CircleImageView ivArHeadImage;
    @BindView(R.id.bt_ar_regist)
    Button btArRegist;
    @BindView(R.id.et_ar_email)
    EditText etArEmail;
    private Uri resultUri;
    private byte[] mHeadImage;
    private String code;
    private RegistUser mRegistUser;
    private School mSchool;
    private List<String> mProList;
    private List<String> mCityList;
    private List<String> mSchoolList;
    private String mSchoolAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        initData();
        initView();
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
    }

    private void initView() {
        Resources r = this.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceTypeName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceEntryName(R.drawable.circle_elves_ball));
        etArSchoolAddress.setFocusable(false);
        etArSchoolAddress.setOnClickListener(v -> {

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
                etArSchoolAddress.setText(mSchoolAddress);
            });

        });
    }

    @OnClick(R.id.tv_ar_get_code)
    public void onTvArGetCodeClicked() {
        tvArGetCode.setVisibility(View.GONE);
        ibArRefreshCode.setVisibility(View.VISIBLE);
        onIbArRefreshCodeClicked();
    }

    @OnClick(R.id.ib_ar_refresh_code)
    public void onIbArRefreshCodeClicked() {
        RxCaptcha.build()
                .backColor(0xffffff)
                .codeLength(4)
                .fontSize(40)
                .lineNumber(0)
                .size(150, 50)
                .type(CHARS)
                .into(ibArRefreshCode);
        code=RxCaptcha.build().getCode();
    }

    @OnClick(R.id.bt_ar_up_head_image)
    public void onBtArUpHeadImageClicked() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(this, TITLE);
        dialogChooseImage.show();
    }

    @OnClick(R.id.bt_ar_regist)
    public void onBtArRegistClicked() {
        if(mRegistUser==null){
            mRegistUser=new RegistUser();
        }
        if(mHeadImage==null){
            Drawable drawable=getResources().getDrawable(
                    R.drawable.circle_elves_ball);
            mHeadImage=RxImageTool.drawable2Bytes(drawable, Bitmap.CompressFormat.PNG);

        }
            mRegistUser.setUserName(etArUsername.getText().toString());
            mRegistUser.setPassword(etArPassword.getText().toString());
            mRegistUser.setSchoolAddress(etArSchoolAddress.getText().toString());
            mRegistUser.setEmail(etArEmail.getText().toString());
            mRegistUser.setHeadImage(mHeadImage);
            if(checkRegistInfo(mRegistUser)) {
                RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
                rxDialogLoading.setLoadingText("注册中...");
                rxDialogLoading.setCancelable(false);
                rxDialogLoading.show();
                new Thread(() -> new RegistFun(mRegistUser) {
                    @Override
                    public void registSucess() {
                        rxDialogLoading.cancel();
                        RxToast.success("注册成功");
                        finish();
                    }

                    @Override
                    public void registFaild(String msg) {
                        rxDialogLoading.cancel();
                        RxToast.error(msg);
                    }
                }).start();
            }
        }

    private boolean checkRegistInfo(RegistUser registUser) {
        if(TextUtils.isEmpty(registUser.getUserName())){
            RxToast.error("用户名不能为空");
            return false;
        }
        if(!RxRegTool.isUsername(registUser.getUserName())){
            RxToast.error("用户名取值范围为a-z,A-Z,0-9," +
                    "\"_\",汉字，不能以\"_\"结尾," +
                    "用户名必须是6-20位");
            return false;
        }
        if(TextUtils.isEmpty(registUser.getPassword())){
            RxToast.error("密码不能为空");
            return false;
        }
        if(!Validator.isPassword(registUser.getPassword())){
            RxToast.error("密码取值范围为a-z,A-Z,0-9,不包含特殊字符，长度大于6");
            return false;
        }
        if(TextUtils.isEmpty(etArAffirmPassword.getText().toString())){
            RxToast.error("确认密码不能为空");
            return false;
        }
        if(!registUser.getPassword().equals(
                etArAffirmPassword.getText().toString())){
            RxToast.error("密码不一致");
            return false;
        }
        if(TextUtils.isEmpty(registUser.getSchoolAddress())){
            RxToast.error("学校不能为空");
            return false;
        }
        if(TextUtils.isEmpty(registUser.getEmail())){
            RxToast.error("邮箱不能为空");
            return false;
        }
        if(!Validator.isEmail(registUser.getEmail())){
            RxToast.error("请输入正确的邮箱");
            return false;
        }
        if(TextUtils.isEmpty(etArIdcode.getText().toString())){
            RxToast.error("验证码不能为空");
            return false;
        }
        if(!code.equals(etArIdcode.getText().toString())){
            RxToast.error("验证码不正确");
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);

                    mHeadImage= FileUtil.getBytesFromFile(roadImageView(resultUri, ivArHeadImage));

                    RxSPTool.putContent(this, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(this).
                load(uri).
                thumbnail(0.5f).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setCircleDimmedLayer(true);
        //设置是否展示矩形裁剪框
        //options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

}
