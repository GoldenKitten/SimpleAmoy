package com.xhm.simpleamoy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.IssueFun;
import com.xhm.simpleamoy.data.db.RegistFun;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;


/**
 * Created by xhm on 2018/4/27.
 */

public class IssueFragment extends Fragment {
    private Activity mActivity;
    private static FragmentManager mFManager;
    private static IssueFragment mIssueFragment;
    private ViewHolder mViewHolder;
    private static final int REQUEST_MAIN_IMAGE = 1;
    private static final int REQUEST_IMAGE = 2;
    private IssueGoods mIssueGoods;
    private List<byte[]> mGoodsImage;
    private byte[] mMainGoodsImage;

    public static IssueFragment newInstance(FragmentManager fManager) {
        mFManager = fManager;
        if (mIssueFragment == null) {
            mIssueFragment = new IssueFragment();
        }
        return mIssueFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issue,
                container, false);
        if(mViewHolder==null) {
            mViewHolder = new ViewHolder(view);
        }
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity=getActivity();
            mViewHolder = new ViewHolder(getView());
        mViewHolder.btFiUpMainGoodsPic.setOnClickListener(v -> {
            MultiImageSelector.create()
                    .single()
                    .showCamera(false)
                    .start(mActivity,REQUEST_MAIN_IMAGE);
        });
        mViewHolder.btFiUpGoodsPic.setOnClickListener(v -> {
            // Multi image selector form an Activity
            MultiImageSelector.create()
                    .multi()
                    .showCamera(false)
                    .start(mActivity, REQUEST_IMAGE);

        });
        mViewHolder.btFiIssue.setOnClickListener(v -> {
            if(mIssueGoods==null){
                mIssueGoods=new IssueGoods();
            }
            mIssueGoods.setGoodsName(mViewHolder
                    .etFiGoodsName
                    .getText()
                    .toString());
            mIssueGoods.setGoodsDes(mViewHolder
                    .etFiGoodsDes
                    .getText()
                    .toString());
            mIssueGoods.setGoodsPrice(mViewHolder
                    .etFiGoodsPrice
                    .getText()
                    .toString());
            mIssueGoods.setMainGoodsPic(mMainGoodsImage);
            mIssueGoods.setGoodsPic(mGoodsImage);
            mIssueGoods.setWeixing(mViewHolder
                    .etFiWeixing
                    .getText()
                    .toString());
            mIssueGoods.setQq(mViewHolder
                    .etFiQq
                    .getText()
                    .toString());
            mIssueGoods.setMobile(mViewHolder
                    .etFiMobile
                    .getText()
                    .toString());
            mIssueGoods.setUserName(
                    RxSPTool.getString(MyApp.newInstance(),
                            C.Splash.USERNAME));
            mIssueGoods.setSchoolAddress(
                    RxSPTool.getString(MyApp.newInstance(),
                            C.Splash.SCHOOLADDRESS));
            if(checkUploadGoodsState(mIssueGoods)){
                RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
                rxDialogLoading.setLoadingText("发布中...");
                rxDialogLoading.setCancelable(false);
                rxDialogLoading.show();
                new Thread(() -> new IssueFun(mIssueGoods){
                    @Override
                    public void issueSucess() {
                        rxDialogLoading.cancel();
                        RxToast.success("发布成功");

                    }

                    @Override
                    public void issueFaild(String msg) {
                        rxDialogLoading.cancel();
                        RxToast.error(msg);
                    }
                }).start();
            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.et_fi_goods_name)
        EditText etFiGoodsName;
        @BindView(R.id.et_fi_goods_des)
        EditText etFiGoodsDes;
        @BindView(R.id.et_fi_goods_price)
        EditText etFiGoodsPrice;
        @BindView(R.id.et_fi_weixing)
        EditText etFiWeixing;
        @BindView(R.id.et_fi_qq)
        EditText etFiQq;
        @BindView(R.id.et_fi_mobile)
        EditText etFiMobile;
        @BindView(R.id.bt_fi_up_goods_pic)
        Button btFiUpGoodsPic;
        @BindView(R.id.rv_fi_goods_pic)
        RecyclerView rvFiGoodsPic;
        @BindView(R.id.bt_fi_issue)
        Button btFiIssue;
        @BindView(R.id.bt_fi_up_main_goods_pic)
        Button  btFiUpMainGoodsPic;
        @BindView(R.id.iv_fi_main_goods_pic)
        ImageView ivFiMainGoodsPic;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMainImage(Event<byte[]> event) {
        if(event.getMsg().equals("MainImageEvent")) {
            mMainGoodsImage=event.getData();
            mViewHolder.ivFiMainGoodsPic.setVisibility(View.VISIBLE);
            Glide.with(mActivity)
                    .load(mMainGoodsImage)
                    .into(mViewHolder.ivFiMainGoodsPic);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getImage(Event<List<byte[]>> event) {
        if(event.getMsg().equals("ImageEvent")) {
            mGoodsImage = event.getData();
            mViewHolder.rvFiGoodsPic.setLayoutManager(new
                    GridLayoutManager(mActivity, 3));
            IssueImageAdapter myAdapter = new IssueImageAdapter(
                    R.layout.fragment_issue_image_item,
                    mGoodsImage);
            mViewHolder.rvFiGoodsPic.setAdapter(myAdapter);
            myAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                mGoodsImage.remove(position);
                myAdapter.notifyDataSetChanged();
            });
        }
    }
    class IssueImageAdapter extends
            BaseQuickAdapter<byte[],BaseViewHolder> {
        public IssueImageAdapter(int layoutResId,
                                 @Nullable List<byte[]> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, byte[] item) {
            helper.addOnClickListener(R.id.iv_fiii_delete);
            if(item!=null) {
                Glide.with(mActivity)
                        .load(item)
                        .into((ImageView) helper.getView(R.id.iv_fiii));
            }
        }
    }
    public boolean checkUploadGoodsState(IssueGoods goods){
        if(TextUtils.isEmpty(goods.getGoodsName())){
            RxToast.error("标题不能为空");
            return false;
        }
        if(TextUtils.isEmpty(goods.getGoodsDes())){
            RxToast.error("描述不能为空");
            return false;
        }
        if(TextUtils.isEmpty(goods.getGoodsPrice())){
            RxToast.error("价格不能为空");
            return false;
        }
        if(!(RxDataTool.isInteger(goods.getGoodsPrice())||
                RxDataTool.isDouble(goods.getGoodsPrice()))){
            RxToast.error("价格格式不正确");
            return false;
        }
        if(TextUtils.isEmpty(goods.getWeixing())&&
                TextUtils.isEmpty(goods.getQq())&&
                TextUtils.isEmpty(goods.getMobile())){
            RxToast.error("联系方式三个中必填一个");
            return false;
        }
        if(goods.getMainGoodsPic()==null){
            RxToast.error("商品主图片不能为空");
            return false;
        }
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
