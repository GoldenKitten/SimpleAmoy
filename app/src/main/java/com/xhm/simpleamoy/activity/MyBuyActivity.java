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
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.Base.BaseActivity;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetMyBuyGoodsItemFun;
import com.xhm.simpleamoy.data.entity.BuyGoods;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.fragment.BuyGoodsFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBuyActivity extends BaseActivity {
    @BindView(R.id.rv_amb)
    RecyclerView rvAmb;
    private Activity mActivity;
    private FirstPagerAdapter mFirstPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy);
        ButterKnife.bind(this);
        mActivity = this;
        initToolbar("我的购买", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivity=this;
        RxLogTool.i("InitData");
        initData();
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
                        mFirstPagerAdapter = new FirstPagerAdapter(
                                R.layout.fragment_first_pager_item,
                                firstPagerGoods);
                        mFirstPagerAdapter.setOnItemClickListener((adapter, view, position) -> {
                            BuyGoods buyGoods=new BuyGoods();
                            buyGoods.setSellUUID(firstPagerGoods.get(position).getGoodsUUID());
                            buyGoods.setSellUserName(firstPagerGoods.get(position).getUserName());
                            Event<BuyGoods> event=new Event<BuyGoods>("MYBUYITEM",buyGoods);
                            EventBus.getDefault().postSticky(event);//发送粘性事件
                            RxActivityTool.skipActivity(mContext,MyBuyItemActivity.class);

                        });
                        rvAmb.setAdapter(mFirstPagerAdapter);

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
