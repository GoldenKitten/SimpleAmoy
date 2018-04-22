package com.xhm.simpleamoy.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vondear.rxtools.RxBarTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxRegTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxCaptcha;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.RegistFun;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.utils.LogUtil;
import com.xhm.simpleamoy.utils.Validator;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private File mHeadImage;
    private String code;
    private RegistUser mRegistUser;
    private boolean[] mState;
    private static final String TAG = "RegistActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mState=new boolean[]{false,false,false,false,false,false};
        btArRegist.setEnabled(false);
        Resources r = this.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceTypeName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceEntryName(R.drawable.circle_elves_ball));
        etArUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!TextUtils.isEmpty(etArUsername.getText().toString())){
                    if(!RxRegTool.isUsername(etArUsername.getText().toString())){
                        RxToast.error("用户名，" +
                                "取值范围为a-z,A-Z,0-9,\"_\",汉字，" +
                                "不能以\"_\"结尾,用户名必须是6-20位");
                        mState[0]=false;
                    }
                    mState[0]=true;
                }
            }
        });
        etArPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!TextUtils.isEmpty(etArPassword.getText().toString())){
                    if(!Validator.isPassword(etArPassword.getText().toString())){
                        RxToast.error("密码，" +
                                "取值范围为a-z,A-Z,0-9,不能为特殊字符");
                        btArRegist.setEnabled(false);
                        mState[1]=false;
                    }
                    mState[1]=true;
                }
            }
        });
        etArAffirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!TextUtils.isEmpty(etArAffirmPassword.getText().toString())){
                    if(!etArAffirmPassword.getText().toString().equals(
                            etArPassword.getText().toString())){
                        RxToast.error("密码不一致");
                        btArRegist.setEnabled(false);
                        mState[2]=false;
                    }
                    mState[2]=true;
                }
            }
        });
        etArEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!TextUtils.isEmpty(etArEmail.getText().toString())){
                    if(!RxRegTool.isEmail(etArEmail.getText().toString())){
                        RxToast.error("Email格式不正确");
                        btArRegist.setEnabled(false);
                        mState[4]=false;
                    }
                    mState[4]=true;
                }
            }
        });
        etArIdcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==4){
                    if(!TextUtils.isEmpty(etArIdcode.getText().toString())){
                        if(!etArIdcode.getText().toString().equals(code)){
                            RxToast.error("验证码不正确");
                            btArRegist.setEnabled(false);
                            mState[5]=false;
                        }
                        else {
                            mState[5]=true;
                        }
                        if(mState[0]&&mState[1]&&mState[2]&&mState[4]&&mState[5]) {
                            btArRegist.setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
        if(TextUtils.isEmpty(etArUsername.getText().toString())){
            RxToast.error("用户名不能为空");
            btArRegist.setEnabled(false);
            mState[0]=false;
            return;
        }
        else {
            mState[0]=true;
        }
        if(TextUtils.isEmpty(etArPassword.getText().toString())){
            RxToast.error("密码不能为空");
            btArRegist.setEnabled(false);
            mState[1]=false;
            return;
        }
        else {
            mState[1]=true;
        }
        if(TextUtils.isEmpty(etArAffirmPassword.getText().toString())){
            RxToast.error("确认密码不能为空");
            btArRegist.setEnabled(false);
            mState[2]=false;
            return;
        }
        else {
            mState[2]=true;
        }
        if(TextUtils.isEmpty(etArSchoolAddress.getText().toString())){
            RxToast.error("学校地址不能为空");
            btArRegist.setEnabled(false);
            mState[3]=false;
            return;
        }
        else {
            mState[3]=true;
        }
        if(TextUtils.isEmpty(etArEmail.getText().toString())){
            RxToast.error("Email不能为空");
            btArRegist.setEnabled(false);
            mState[4]=false;
            return;
        }
        else {
            mState[4]=true;
        }
        if(TextUtils.isEmpty(etArIdcode.getText().toString())){
            RxToast.error("验证码不能为空");
            btArRegist.setEnabled(false);
            mState[5]=false;
            return;
        }
        else {
            mState[5]=true;
        }
        if(mHeadImage==null){
          mHeadImage=new File(
                  RxPhotoTool.getImageAbsolutePath(this,resultUri))  ;
        }
        if(mState[0]&&mState[1]&&mState[2]&&mState[3]&&mState[4]&&mState[5]) {
            mRegistUser.setUserName(etArUsername.getText().toString());
            mRegistUser.setPassword(etArPassword.getText().toString());
            mRegistUser.setSchoolAddress(etArSchoolAddress.getText().toString());
            mRegistUser.setEmail(etArEmail.getText().toString());
            mRegistUser.setHeadImage(mHeadImage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i(TAG, String.valueOf(requestCode));
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
                    mHeadImage=roadImageView(resultUri, ivArHeadImage);
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
