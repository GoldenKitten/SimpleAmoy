package com.xhm.simpleamoy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetMyBuyGoodsItemFun;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.fragment.BuyGoodsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBuyActivity extends BaseActivity {
    @BindView(R.id.rv_amb)
    RecyclerView rvAmb;
    @BindView(R.id.fl_amb)
    FrameLayout flAmb;
    private Activity mActivity;
    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);
        ButterKnife.bind(this);
        mActivity = this;
        mFragmentManager = getSupportFragmentManager();
        initToolbar("我的购买", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
        //initData();
    }

    private void initData() {
        rvAmb.setLayoutManager(new LinearLayoutManager(mActivity));
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() ->
                new GetMyBuyGoodsItemFun(
                        RxSPTool.getString(
                                MyApp.newInstance(),
                                C.Splash.USERNAME)) {
                    @Override
                    public void getMyBuyGoodsItemSucess(List<FirstPagerGoods> firstPagerGoods) {
                        rxDialogLoading.cancel();
                        FirstPagerAdapter firstPagerAdapter = new FirstPagerAdapter(
                                R.layout.fragment_first_pager_item,
                                firstPagerGoods);
                        firstPagerAdapter.setOnItemClickListener((adapter, view, position) -> {
                            flAmb.setVisibility(View.VISIBLE);
                            rvAmb.setVisibility(View.GONE);
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            BuyGoodsFragment buyGoodsFragment = BuyGoodsFragment
                                    .newInstance(mFragmentManager,
                                            firstPagerGoods.get(position).getUserName(),
                                            firstPagerGoods.get(position).getGoodsUUID());
                            transaction.replace(R.id.fl_amb, buyGoodsFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            //initToolbar("首页");

                        });
                        rvAmb.setAdapter(firstPagerAdapter);

                    }

                    @Override
                    public void getMyBuyGoodsItemFaild(String msg) {
                        rxDialogLoading.cancel();
                        RxToast.error(msg);
                    }
                }).start();
    }

    class FirstPagerAdapter extends
            BaseQuickAdapter<FirstPagerGoods, BaseViewHolder> {
        public FirstPagerAdapter(int layoutResId,
                                 @Nullable List<FirstPagerGoods> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FirstPagerGoods item) {
            if (item != null) {
                helper.setText(R.id.tv_ffpi_title, item.getGoodsTitle());
                helper.setText(R.id.tv_ffpi_price, item.getGoodsPrice());
                Glide.with(mActivity)
                        .load(item.getGoodsImage())
                        .into((ImageView) helper.getView(R.id.iv_ffpi));
            }
        }
    }

}
