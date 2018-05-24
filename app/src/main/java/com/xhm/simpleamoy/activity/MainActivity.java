package com.xhm.simpleamoy.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxProgressBar;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.vondear.rxtools.view.dialog.RxDialogSure;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.DataManager;
import com.xhm.simpleamoy.data.db.GetSoftwareUpdateInfo;
import com.xhm.simpleamoy.data.db.ModifyHeadImage;
import com.xhm.simpleamoy.data.entity.AppInfo;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.fragment.FirstPagerFragment;
import com.xhm.simpleamoy.fragment.IssueFragment;
import com.xhm.simpleamoy.fragment.PersonFragment;
import com.xhm.simpleamoy.utils.BottomNavigationViewUtil;
import com.xhm.simpleamoy.utils.FileUtil;
import com.xhm.simpleamoy.utils.LogUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;


public class MainActivity extends BaseActivity {
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    private static final int REQUEST_MAIN_IMAGE = 1;
    private static final int REQUEST_IMAGE = 2;
    private List<byte[]> mImage;
    private CircleImageView mHeadImageView;
    private Uri resultUri;
    private byte[] mHeadImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        Resources r = this.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceTypeName(R.drawable.circle_elves_ball) + "/"
                + r.getResourceEntryName(R.drawable.circle_elves_ball));
        new Thread(() -> {
            new DataManager(RxSPTool.getString(
                    MyApp.newInstance(),
                    C.Splash.USERNAME),0){
                @Override
                public void getDataSucess(List list) {
                    RxSPTool.putString(MyApp.newInstance(),
                            C.Splash.SCHOOLADDRESS,(String) list.get(1));
                    Event event=new Event("HeadImageAndUserName",list);
                    EventBus.getDefault().post(event);
                }

                @Override
                public void getDataFaild(String msg) {
                        RxToast.error(msg);
                }
            };
        }).start();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setImageAndUserName(Event<List> event){
        if(event.getMsg().equals("HeadImageAndUserName")) {
            View headView = navView.getHeaderView(0);
            ImageView imageView = (ImageView) headView.findViewById(R.id.civ_head_image);
            TextView textView = (TextView) headView.findViewById(R.id.tv_username);
            Glide.with(this)
                    .load((byte[]) event.getData().get(0))
                    .into(imageView);
            textView.setText(RxSPTool.getString(MyApp.newInstance(),
                    C.Splash.USERNAME));
            initView();
        }
    }
    private void initView() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        FirstPagerFragment fPagerFragment =FirstPagerFragment.newInstance
                (mFragmentManager);
        transaction.replace(R.id.fl_content,fPagerFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
        initToolbar("首页", R.drawable.icon_menu);
        BottomNavigationViewUtil.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, getCustomToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View view=navView.getHeaderView(0);
        mHeadImageView = (CircleImageView)
                view.findViewById(R.id.civ_head_image);
        mHeadImageView.setOnClickListener(v -> {
            RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mContext, TITLE);
            dialogChooseImage.show();
        });
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.quit:
                    final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);//提示弹窗
                    //rxDialogSureCancel.getTitleView().setBackgroundResource(R.drawable.logo);
                    rxDialogSureCancel.getSureView().setOnClickListener(v -> {
                        RxSPTool.putBoolean(MyApp.newInstance(),
                                C.Splash.IS_LOGIN,false);
                        RxActivityTool.skipActivity(
                                MainActivity.this,
                                LoginActivity.class
                        );
                        RxActivityTool.finishActivity(mContext);
                    });
                    rxDialogSureCancel.getCancelView().setOnClickListener(v -> rxDialogSureCancel.cancel());
                    rxDialogSureCancel.show();
                    break;
                case  R.id.person_data:
                    RxActivityTool.skipActivity(mContext,PersonDataActivity.class);
                    break;
                case R.id.software_info:
                    final RxDialogSure rxDialogSure = new RxDialogSure(mContext);//提示弹窗
                    rxDialogSure.setTitle("软件说明");
                    rxDialogSure.setContent("据了解，校园内存在大量资源闲置与浪费的现象。" +
                            "作为专注于大学生的团队，所有的产品与服务都围绕同学们的需求开展与调整" +
                            "，学生通过注册该app后，可以将自己不需要的物品通过平台免费发布到网上，" +
                            "也可以通过平台购买自己需要的物品，操作非常简单。");
                    rxDialogSure.getSureView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rxDialogSure.cancel();
                        }
                    });
                    rxDialogSure.show();
                    break;
                case  R.id.update_software:
                    RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
                    rxDialogLoading.setLoadingText("检查更新中...");
                    rxDialogLoading.setCancelable(false);
                    rxDialogLoading.show();
                    new Thread(() -> new GetSoftwareUpdateInfo(){
                        @Override
                        public void getSoftwareUpdateSucess(AppInfo appInfo) {
                            rxDialogLoading.cancel();
                            Event<AppInfo> event=new Event<AppInfo>("getAppInfoSucess",appInfo);
                            EventBus.getDefault().post(event);
                        }

                        @Override
                        public void getSoftwareUpdateFailed(String msg) {
                         rxDialogLoading.cancel();
                         RxToast.error(msg);
                        }
                    }).start();

            }
            drawerLayout.closeDrawers();
            return true;
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case  R.id.bottom_first_pager:
                    FragmentTransaction fPtransaction = mFragmentManager.beginTransaction();
                    FirstPagerFragment firstPagerFragment =FirstPagerFragment.newInstance
                            (mFragmentManager);
                    fPtransaction.replace(R.id.fl_content,firstPagerFragment);
                    //transaction.addToBackStack(null);
                    fPtransaction.commit();
                    initToolbar("首页");
                    break;
                case  R.id.bottom_issue:
                    drawerLayout.setBackgroundResource(R.drawable.issue_bg);
                    FragmentTransaction iTransaction = mFragmentManager.beginTransaction();
                    IssueFragment issueFragment = IssueFragment.newInstance
                            (mFragmentManager);
                    //EventBus.getDefault().register(issueFragment);
                    iTransaction.replace(R.id.fl_content,issueFragment);
                    //transaction.addToBackStack(null);
                    iTransaction.commit();
                    initToolbar("发布");
                    break;
                case  R.id.bottom_person:
                    drawerLayout.setBackgroundResource(R.drawable.person_bg);
                    FragmentTransaction pTransaction = mFragmentManager.beginTransaction();
                    PersonFragment personFragment = PersonFragment.newInstance
                            (mFragmentManager);
                    pTransaction.replace(R.id.fl_content,personFragment);
                    //transaction.addToBackStack(null);
                    pTransaction.commit();
                    initToolbar("个人");
                    break;
            }
            return true;
        });
    }
@Subscribe(threadMode = ThreadMode.MAIN)
public void updateSoftware(Event<AppInfo> event){
        if(event.getMsg().equals("getAppInfoSucess")){
            AppInfo appInfo=event.getData();
            int LocalVersionCode= RxAppTool.getAppVersionCode(mContext);
            final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(mContext);//提示弹窗
            if(LocalVersionCode<appInfo.getVersionCode()){
                rxDialogSureCancel.getContentView().setText("发现最新版本"+appInfo.getVersionName()+",是否下载更新");
            }
            else {
                rxDialogSureCancel.getContentView().setText("当前为最新版本，无需更新");
            }
            rxDialogSureCancel.getSureView().setOnClickListener(v -> {
                if(LocalVersionCode<appInfo.getVersionCode()){
                    downloadApk(appInfo.getUrl());
                }

                rxDialogSureCancel.cancel();
            });
            rxDialogSureCancel.getCancelView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(LocalVersionCode<appInfo.getVersionCode()) {
                        RxToast.showToast(mContext, "已取消最新版本的下载", 500);
                    }
                    rxDialogSureCancel.cancel();
                }
            });
            rxDialogSureCancel.show();
        }
}private  void downloadApk(String url) {
        //判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ProgressDialog progressDialog=new ProgressDialog(mContext);
            progressDialog.show();
            RequestParams params = new RequestParams(url);
            //自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
            params.setSaveFilePath(Environment.getExternalStorageDirectory() + "/myapp/");
            //自动为文件命名
            params.setAutoRename(true);
            x.http().post(params, new org.xutils.common.Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    //apk下载完成后，调用系统的安装方法
                    progressDialog.dismiss();
                    RxAppTool.installApp(mContext,result.getAbsolutePath());
                    RxActivityTool.AppExit(mContext);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    progressDialog.dismiss();
                    RxToast.error("下载失败");
                }

                @Override
                public void onCancelled(org.xutils.common.Callback.CancelledException cex) {
                    progressDialog.dismiss();
                    RxToast.info("取消下载");
                }

                @Override
                public void onFinished() {
                }

                //网络请求之前回调
                @Override
                public void onWaiting() {
                }

                //网络请求开始的时候回调
                @Override
                public void onStarted() {
                }

                //下载的时候不断回调的方法
                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    //当前进度和文件总大小
                    progressDialog.setMax((int) total);
                    progressDialog.setProgress((int)current);
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case  REQUEST_IMAGE :
                if (resultCode == RESULT_OK) {
                    // Get the result list of select image paths
                    List<String> path = data.getStringArrayListExtra(
                            MultiImageSelectorActivity.EXTRA_RESULT);
                    mImage = new ArrayList<byte[]>();
                    for (String imagePath : path) {
                        mImage.add(FileUtil.
                                getBytesFromFile(new File(imagePath)));
                    }
                    Event imageEvent = new Event("ImageEvent", mImage);
                    EventBus.getDefault().post(imageEvent);
                }
                break;

            case REQUEST_MAIN_IMAGE :
                if (resultCode == RESULT_OK) {
                    // Get the result list of select image paths

                    List<String> path = data.getStringArrayListExtra(
                            MultiImageSelectorActivity.EXTRA_RESULT);
                    byte[] mainImage = FileUtil
                            .getBytesFromFile(new File(path.get(0)));
                    Event mainImageEvent = new Event("MainImageEvent", mainImage);
                    EventBus.getDefault().post(mainImageEvent);
                }
                break;
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

                    mHeadImage= FileUtil.getBytesFromFile(roadImageView(resultUri, mHeadImageView));
                    new Thread(() -> {
                        new ModifyHeadImage(RxSPTool.getString(MyApp.newInstance(),
                                C.Splash.USERNAME),mHeadImage){
                            @Override
                            public void modifySucess() {
                                RxToast.success("修改成功");
                            }

                            @Override
                            public void modifyFailed(String msg) {
                                RxToast.error(msg);
                            }
                        };
                    }).start();

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

    }
    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, CircleImageView imageView) {
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
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
