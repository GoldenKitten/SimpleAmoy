package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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
import com.xhm.simpleamoy.data.db.CancelSellGoodsFun;
import com.xhm.simpleamoy.data.db.GetMySellGoodsItemFun;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.SellGoods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的卖出界面
 */
public class MySellActivity extends BaseActivity {

    @BindView(R.id.rv_ams)
    RecyclerView rvAms;
    @BindView(R.id.bt_ams_cancel)
    Button btAmsCancel;
    private MySellAdapter mMySellAdapter;
    private List<SellGoods> mSellGoodsList;
    private List<SellGoods> mSellGoodsCancelList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sell);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolbar("我的卖出", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
        init();
    }

    private void init() {
        RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() -> new GetMySellGoodsItemFun(RxSPTool.getString(
                MyApp.newInstance(),
                C.Splash.USERNAME
        )){
            @Override
            public void getMySellGoodsItemSucess(List<SellGoods> sellGoods) {
                rxDialogLoading.cancel();
                mSellGoodsList = sellGoods;
                Event<Object> event=new Event<Object>(
                        "GetSellGoodsSucess",null);
                EventBus.getDefault().post(event);
            }

            @Override
            public void getMySellGoodsItemFaild(String msg) {
                rxDialogLoading.cancel();
                RxToast.error(msg);

            }
        }).start();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void myEvent(Event<Object> event){
        if(event.getMsg().equals("GetSellGoodsSucess")){
            mMySellAdapter = new MySellAdapter(
                    R.layout.activity_my_sell_item,mSellGoodsList);
            rvAms.setLayoutManager(new LinearLayoutManager(this));
            rvAms.setAdapter(mMySellAdapter);
            if(mSellGoodsList.size()==0){
                RxToast.success("暂无卖出商品");
                btAmsCancel.setEnabled(false);
            }
            else {
                btAmsCancel.setEnabled(true);
            }
            mMySellAdapter.setOnItemClickListener((adapter, view, position) -> {
                mSellGoodsList.get(position).setChecked(
                        !mSellGoodsList.get(position).isChecked()
                );
                mMySellAdapter.notifyDataSetChanged();
            });
        }
        if(event.getMsg().equals("CancelSellGoodsFun")){
            RxToast.success("取消预定成功");
            init();
        }
    }
    @OnClick(R.id.bt_ams_cancel)
    public void onViewClicked() {
        RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
        rxDialogLoading.setLoadingText("删除中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        mSellGoodsCancelList=new ArrayList<>();
        for(SellGoods sellGoods:mSellGoodsList){
            if(sellGoods.isChecked()){
                mSellGoodsCancelList.add(sellGoods);
            }
        }
        new Thread(() -> new CancelSellGoodsFun(mSellGoodsCancelList){
            @Override
            public void cancelSellGoodsSucess() {
                rxDialogLoading.cancel();
                Event<Object> event=new Event<Object>(
                        "CancelSellGoodsFun",
                        null);
                EventBus.getDefault().post(event);
            }

            @Override
            public void cancelSellGoodsFaild(String msg) {
                rxDialogLoading.cancel();
                RxToast.error(msg);
            }
        }).start();
    }

    class MySellAdapter extends BaseQuickAdapter<SellGoods,
            BaseViewHolder> {
        public MySellAdapter(int layoutResId,
                            @Nullable List<SellGoods> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SellGoods item) {
            if (item != null) {
                helper.setText(R.id.tv_amsi_buy_username,
                        "买家:" + item.getBuyGoodsUserName())
                        .setText(R.id.tv_amsi_title, item.getSellGoodsTitle())
                        .setText(R.id.tv_amsi_price, item.getSellPrice())
                        .setChecked(R.id.cb_amsi, item.isChecked());
                Glide.with(mContext)
                        .load(item.getSellMainPic())
                        .into((ImageView) helper.getView(R.id.iv_amsi));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
