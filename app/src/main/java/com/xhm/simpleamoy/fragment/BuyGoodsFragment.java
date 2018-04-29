package com.xhm.simpleamoy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetOneGoodsFun;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xhm on 2018/4/27.
 */

public class BuyGoodsFragment extends Fragment {
    private static FragmentManager mFManager;
    private static BuyGoodsFragment mBuyGoodsFragment;
    private Activity mActivity;
    private ViewHolder mViewHolder;
    private static String mUserName;
    private static String mGoodsUUID;
    public static BuyGoodsFragment newInstance(FragmentManager fManager,
                                               String userName,
                                               String goodsUUID) {
        mUserName=userName;
        mGoodsUUID=goodsUUID;
        mFManager = fManager;
        if (mBuyGoodsFragment == null) {
            mBuyGoodsFragment = new BuyGoodsFragment();
        }
        return mBuyGoodsFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_buy_goods,
                container, false);
        if(mViewHolder==null) {
            mViewHolder = new ViewHolder(view);
        }
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() -> new GetOneGoodsFun(mUserName,mGoodsUUID){
            @Override
            public void getGoodsItemSucess(IssueGoods issueGoods) {
                rxDialogLoading.cancel();
                Event<IssueGoods> event=new Event<IssueGoods>(
                        "OneGoods",issueGoods
                );
                EventBus.getDefault().post(event);
            }

            @Override
            public void getGoodsItemFaild(String msg) {
                rxDialogLoading.cancel();
                mFManager.popBackStack();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOneGoods(Event<IssueGoods> event){
        if(event.getMsg().equals("OneGoods")){
            IssueGoods issueGoods=event.getData();
            Glide.with(mActivity)
                    .load(issueGoods.getMainGoodsPic())
                    .into(mViewHolder.ivFbgMainGoodsPic);
            mViewHolder.tvFbgGoodsName
                    .setText(issueGoods.getGoodsName());
            mViewHolder.tvFbgGoodsDes
                    .setText(issueGoods.getGoodsDes());
            mViewHolder.tvFbgGoodsPrice
                    .setText(issueGoods.getGoodsPrice());
            mViewHolder.tvFbgSchoolAddress
                    .setText(issueGoods.getSchoolAddress());
            if(TextUtils.isEmpty(issueGoods.getQq())){
                mViewHolder.tvFbgQq.setVisibility(View.GONE);
            }
            else {
                mViewHolder.tvFbgQq.setText(issueGoods.getQq());
            }
            if(TextUtils.isEmpty(issueGoods.getWeixing())){
                mViewHolder.tvFbgWeixing.setVisibility(View.GONE);
            }
            else {
                mViewHolder.tvFbgWeixing.setText(issueGoods.getWeixing());
            }
            if(TextUtils.isEmpty(issueGoods.getMobile())){
                mViewHolder.tvFbgMobile.setVisibility(View.GONE);
            }
            else {
                mViewHolder.tvFbgMobile.setText(issueGoods.getMobile());
            }
            mViewHolder.tvFbgUsername.setText(issueGoods.getUserName());
            mViewHolder.rvFbgGoodsPic.setLayoutManager(
                    new LinearLayoutManager(mActivity));
            BuyGoodsAdapter buyGoodsAdapter=new BuyGoodsAdapter(
                    R.layout.fragment_buy_goods_item,
                    issueGoods.getGoodsPic()
            );
            mViewHolder.rvFbgGoodsPic.setAdapter(buyGoodsAdapter);
        }
    }
    class BuyGoodsAdapter extends
            BaseQuickAdapter<byte[],BaseViewHolder> {
        public BuyGoodsAdapter(int layoutResId,
                                 @Nullable List<byte[]> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, byte[] item) {
            if(item!=null) {
                Glide.with(mActivity)
                        .load(item)
                        .into((ImageView) helper.getView(R.id.iv_fbgi));
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    static class ViewHolder {
        @BindView(R.id.iv_fbg_main_goods_pic)
        ImageView ivFbgMainGoodsPic;
        @BindView(R.id.tv_fbg_goods_name)
        TextView tvFbgGoodsName;
        @BindView(R.id.tv_fbg_goods_price)
        TextView tvFbgGoodsPrice;
        @BindView(R.id.tv_fbg_school_address)
        TextView tvFbgSchoolAddress;
        @BindView(R.id.tv_fbg_username)
        TextView tvFbgUsername;
        @BindView(R.id.tv_fbg_qq)
        TextView tvFbgQq;
        @BindView(R.id.tv_fbg_weixing)
        TextView tvFbgWeixing;
        @BindView(R.id.tv_fbg_mobile)
        TextView tvFbgMobile;
        @BindView(R.id.tv_fbg_goods_des)
        TextView tvFbgGoodsDes;
        @BindView(R.id.rv_fbg_goods_pic)
        RecyclerView rvFbgGoodsPic;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
