package com.xhm.simpleamoy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.xhm.simpleamoy.data.db.BuyOneGoodsFun;
import com.xhm.simpleamoy.data.db.CancelBuyOneGoodsFun;
import com.xhm.simpleamoy.data.db.GetOneGoodsFun;
import com.xhm.simpleamoy.data.entity.BuyGoods;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBuyItemActivity extends BaseActivity {

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
    @BindView(R.id.bt_fbg_cancel_schedule)
    Button btFbgCancelSchedule;
    @BindView(R.id.bt_fbg_schedule_center)
    Button btFbgScheduleCenter;
    private BuyGoods mBuyGoods;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy_item);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolbar("我的购买", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventDeal(Event<BuyGoods> event) {
        if (event.getMsg().equals("MYBUYITEM")) {
            mBuyGoods=event.getData();
            RxDialogLoading rxDialogLoading = new RxDialogLoading(this);
            rxDialogLoading.setLoadingText("加载中 ...");
            rxDialogLoading.setCancelable(false);
            rxDialogLoading.show();
            new Thread(() -> new GetOneGoodsFun(mBuyGoods.getSellUserName(),
                    mBuyGoods.getSellUUID()){
                @Override
                public void getGoodsItemSucess(IssueGoods issueGoods) {
                    rxDialogLoading.cancel();
                    Event<IssueGoods> event=new Event<IssueGoods>(
                            "MyBuyGoods",issueGoods
                    );
                    EventBus.getDefault().post(event);
                }

                @Override
                public void getGoodsItemFaild(String msg) {
                    rxDialogLoading.cancel();
                   RxToast.error(msg);
                   RxActivityTool.finishActivity(mContext);
                }
            }).start();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getOneGoods(Event<Object> event){
        if(event.getMsg().equals("MyBuyGoods")){
            IssueGoods issueGoods= (IssueGoods) event.getData();
            Glide.with(this)
                    .load(issueGoods.getMainGoodsPic())
                    .into(ivFbgMainGoodsPic);
            tvFbgGoodsName
                    .setText(issueGoods.getGoodsName());
            tvFbgGoodsDes
                    .setText(issueGoods.getGoodsDes());
            tvFbgGoodsPrice
                    .setText(issueGoods.getGoodsPrice());
            tvFbgSchoolAddress
                    .setText(issueGoods.getSchoolAddress());
            if(TextUtils.isEmpty(issueGoods.getQq())){
                tvFbgQq.setVisibility(View.GONE);
            }
            else {
                tvFbgQq.setText(issueGoods.getQq());
            }
            if(TextUtils.isEmpty(issueGoods.getWeixing())){
                tvFbgWeixing.setVisibility(View.GONE);
            }
            else {
                tvFbgWeixing.setText(issueGoods.getWeixing());
            }
            if(TextUtils.isEmpty(issueGoods.getMobile())){
                tvFbgMobile.setVisibility(View.GONE);
            }
            else {
                tvFbgMobile.setText(issueGoods.getMobile());
            }
            if(issueGoods.isByBuy()&&
                    issueGoods.getBuyUserName()
                            .equals(RxSPTool.getString(
                                    MyApp.newInstance(),
                                    C.Splash.USERNAME))){
                btFbgScheduleCenter.setVisibility(View.GONE);
                btFbgCancelSchedule.setVisibility(View.VISIBLE);
            }
            tvFbgUsername.setText(issueGoods.getUserName());
            rvFbgGoodsPic.setLayoutManager(
                    new LinearLayoutManager(this));
            BuyGoodsAdapter buyGoodsAdapter=new BuyGoodsAdapter(
                    R.layout.fragment_buy_goods_item,
                    issueGoods.getGoodsPic()
            );
            rvFbgGoodsPic.setAdapter(buyGoodsAdapter);
        }
        if(event.getMsg().equals("BUYONEGOODSSUCESS")){
            btFbgScheduleCenter.setVisibility(View.GONE);
            btFbgCancelSchedule.setVisibility(View.VISIBLE);
        }
        if(event.getMsg().equals("CANCELBUYONEGOODSSUCESS")){
            btFbgScheduleCenter.setVisibility(View.VISIBLE);
            btFbgCancelSchedule.setVisibility(View.GONE);
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
                Glide.with(mContext)
                        .load(item)
                        .into((ImageView) helper.getView(R.id.iv_fbgi));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(this);
    }


    @OnClick(R.id.bt_fbg_cancel_schedule)
    public void onBtFbgCancelScheduleClicked() {
        RxDialogLoading rxDialogLoadingb = new RxDialogLoading(this);
        rxDialogLoadingb.setLoadingText("取消预定 ...");
        rxDialogLoadingb.setCancelable(false);
        rxDialogLoadingb.show();
        BuyGoods buyGoods=new BuyGoods();
        buyGoods.setBuyUserName(RxSPTool.getString(MyApp.newInstance(),
                C.Splash.USERNAME));
        buyGoods.setSellUserName(mBuyGoods.getSellUserName());
        buyGoods.setSellUUID(mBuyGoods.getSellUUID());
        new Thread(() -> new CancelBuyOneGoodsFun(buyGoods){
            @Override
            public void cancelBuyOneGoodsSucess() {
                rxDialogLoadingb.cancel();
                RxToast.success("取消成功");
                Event<Object> event=new Event<Object>("CANCELBUYONEGOODSSUCESS",null);
                EventBus.getDefault().post(event);
            }

            @Override
            public void cancelBuyOneGoodsFaild(String msg) {
                rxDialogLoadingb.cancel();
                RxToast.error(msg);
            }
        }).start();
    }

    @OnClick(R.id.bt_fbg_schedule_center)
    public void onBtFbgScheduleCenterClicked() {
        RxDialogLoading rxDialogLoadinga = new RxDialogLoading(this);
        rxDialogLoadinga.setLoadingText("预定中 ...");
        rxDialogLoadinga.setCancelable(false);
        rxDialogLoadinga.show();
        BuyGoods buyGoods=new BuyGoods();
        buyGoods.setBuyUserName(RxSPTool.getString(MyApp.newInstance(),
                C.Splash.USERNAME));
        buyGoods.setSellUserName(mBuyGoods.getSellUserName());
        buyGoods.setSellUUID(mBuyGoods.getSellUUID());
        new Thread(() -> new BuyOneGoodsFun(buyGoods){
            @Override
            public void buyOneGoodsSucess() {
                rxDialogLoadinga.cancel();
                RxToast.success("预定成功");
                Event<Object> event=new Event<Object>("BUYONEGOODSSUCESS",null);
                EventBus.getDefault().post(event);
            }

            @Override
            public void buyOneGoodsFaild(String msg) {
                rxDialogLoadinga.cancel();
                RxToast.error(msg);
            }
        }).start();
    }
}
