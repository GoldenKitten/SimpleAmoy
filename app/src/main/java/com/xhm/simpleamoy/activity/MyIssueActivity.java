package com.xhm.simpleamoy.activity;

import android.app.Activity;
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
import com.xhm.simpleamoy.data.db.DeleteMyIssueItemFun;
import com.xhm.simpleamoy.data.db.GetMyIssueItemFun;
import com.xhm.simpleamoy.data.entity.Event;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyIssueActivity extends BaseActivity {
    @BindView(R.id.rv_ami)
    RecyclerView rvAmi;
    @BindView(R.id.bt_ami_delete)
    Button btAmiDelete;
    private List<FirstPagerGoods> mFirstPagerGoods;
    private List<FirstPagerGoods> mFirstPagerGoodsDelete;
    private MyIssueAdapter mMyIssueAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_issue);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolbar("我的发布", R.drawable.ic_back);
        getCustomToolbar().setNavigationOnClickListener(v ->
                RxActivityTool.finishActivity(this));
        init();

    }

    private void init() {
        mFirstPagerGoodsDelete=new ArrayList<FirstPagerGoods>();
        rvAmi.setLayoutManager(new LinearLayoutManager(mContext));
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() -> {
            new GetMyIssueItemFun(RxSPTool.getString(MyApp.newInstance(),
                    C.Splash.USERNAME)){
                @Override
                public void getMyIssueItemSucess(List<FirstPagerGoods> firstPagerGoods) {
                    rxDialogLoading.cancel();
                    mFirstPagerGoods=firstPagerGoods;
                    Event<List<FirstPagerGoods>> event=new Event<List<FirstPagerGoods>>(
                            "updateMyIssue",firstPagerGoods
                    );
                    EventBus.getDefault().post(event);
                }

                @Override
                public void getMyIssueItemFaild(String msg) {
                    rxDialogLoading.cancel();
                    RxToast.error(msg);
                }
            };
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMyIssue(Event<Object> event){
        if(event.getMsg().equals("updateMyIssue")) {
            mMyIssueAdapter = new MyIssueAdapter(R.layout.activity_my_common,
                    mFirstPagerGoods);
            rvAmi.setAdapter(mMyIssueAdapter);
            mMyIssueAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    mFirstPagerGoods.get(position).setChecked(
                            !mFirstPagerGoods.get(position).isChecked()
                    );
                    mMyIssueAdapter.notifyDataSetChanged();
                }
            });

        }
        if(event.getMsg().equals("deleteSucess")){
            mMyIssueAdapter.notifyDataSetChanged();
        }
        if(event.getMsg().equals("deleteFailed")){
            mFirstPagerGoods.addAll(mFirstPagerGoodsDelete);
            mMyIssueAdapter.notifyDataSetChanged();
        }
    }
    @OnClick(R.id.bt_ami_delete)
    public void onViewClicked() {
        for(FirstPagerGoods firstPagerGoods:mFirstPagerGoods){
            if(firstPagerGoods.isChecked()){
                mFirstPagerGoodsDelete.add(firstPagerGoods);
            }
        }
        mFirstPagerGoods.removeAll(mFirstPagerGoodsDelete);
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mContext);
        rxDialogLoading.setLoadingText("删除中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() -> {
            new DeleteMyIssueItemFun(mFirstPagerGoodsDelete){
                @Override
                public void deleteMyIssueItemSucess() {
                    rxDialogLoading.cancel();
                    RxToast.success("删除成功");
                    Event<Object> event=new Event<Object>(
                            "deleteSucess",null
                    );
                    EventBus.getDefault().post(event);

                }

                @Override
                public void deleteMyIssueItemFaild(String msg) {
                    rxDialogLoading.cancel();
                    RxToast.success(msg);
                    Event<Object> event=new Event<Object>(
                            "deleteFailed",null
                    );
                    EventBus.getDefault().post(event);
                }
            };
        }).start();
    }
    class MyIssueAdapter extends BaseQuickAdapter<FirstPagerGoods,
            BaseViewHolder>{
        public MyIssueAdapter(int layoutResId,
                              @Nullable List<FirstPagerGoods> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FirstPagerGoods item) {
            if(item!=null){
                helper.setText(R.id.tv_amc_title, item.getGoodsTitle());
                helper.setText(R.id.tv_amc_price, item.getGoodsPrice());
                helper.setChecked(R.id.cb_amc,item.isChecked());
                Glide.with(mContext)
                        .load(item.getGoodsImage())
                        .into((ImageView) helper.getView(R.id.iv_amc));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
