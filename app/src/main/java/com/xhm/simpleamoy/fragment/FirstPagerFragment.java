package com.xhm.simpleamoy.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxFragmentTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.activity.MainActivity;
import com.xhm.simpleamoy.data.db.GetGoodsItemFun;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.utils.BottomNavigationViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/27.
 */

public class FirstPagerFragment extends Fragment {
    private Activity mActivity;
    private static FragmentManager mFManager;
    private  static FirstPagerFragment mFirstPagerFragment;
    private RecyclerView mRecyclerView;
    //private byte[] mHeadImage;

    public static FirstPagerFragment newInstance(FragmentManager fManager){
        mFManager=fManager;
        mFirstPagerFragment=new FirstPagerFragment();
        return mFirstPagerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first_pager,
                container,false);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rv_ffp);
       // Drawable drawable=getResources().getDrawable(
               // R.drawable.circle_elves_ball);
       // mHeadImage= RxImageTool.drawable2Bytes(drawable, Bitmap.CompressFormat.PNG);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity=getActivity();
    }

    @Override
   public void onStart() {
        super.onStart();
        MainActivity mainActivity=(MainActivity)mActivity;
        mainActivity.initToolbar("首页", R.drawable.ic_menu);
        BottomNavigationViewUtil.disableShiftMode(mainActivity.bottomNavigationView);
        mainActivity.bottomNavigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mainActivity, mainActivity.drawerLayout, mainActivity.getCustomToolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.setToolbarNavigationClickListener(v -> mainActivity.drawerLayout.openDrawer(GravityCompat.START));
        mainActivity.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() ->
                new GetGoodsItemFun(
                        RxSPTool.getString(
                                MyApp.newInstance(),
                                C.Splash.SCHOOLADDRESS),
                        RxSPTool.getString(MyApp.newInstance(),
                                C.Splash.USERNAME)){
            @Override
            public void getGoodsItemSucess(List<FirstPagerGoods> firstPagerGoods) {
                rxDialogLoading.cancel();
                FirstPagerAdapter firstPagerAdapter=new FirstPagerAdapter(
                        R.layout.fragment_first_pager_item,
                        firstPagerGoods);
                firstPagerAdapter.setOnItemClickListener((adapter, view, position) -> {
                    FragmentTransaction transaction=mFManager.beginTransaction();
                    BuyGoodsFragment buyGoodsFragment=BuyGoodsFragment
                            .newInstance(mFManager,
                                    firstPagerGoods.get(position).getUserName(),
                                    firstPagerGoods.get(position).getGoodsUUID());
                    transaction.replace(R.id.fl_content,buyGoodsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    MainActivity mainActivity=(MainActivity)mActivity;
                    mainActivity.initToolbar("商品", R.drawable.ic_back);
                    mainActivity.getCustomToolbar()
                            .setNavigationOnClickListener(v ->
                            {
                                mFManager.popBackStack();
                                mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                                mainActivity.tvAmBn.setVisibility(View.VISIBLE);
                            });
                    mainActivity.bottomNavigationView.setVisibility(View.GONE);
                    mainActivity.tvAmBn.setVisibility(View.GONE);
                    //initToolbar("首页");


                });
                mRecyclerView.setAdapter(firstPagerAdapter);

            }

            @Override
            public void getGoodsItemFaild(String msg) {
                rxDialogLoading.cancel();
                RxToast.error(msg);
            }
        }).start();


    }
    class FirstPagerAdapter extends
            BaseQuickAdapter<FirstPagerGoods,BaseViewHolder>{
        public FirstPagerAdapter(int layoutResId,
                                 @Nullable List<FirstPagerGoods> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FirstPagerGoods item) {
            if(item!=null) {
                helper.setText(R.id.tv_ffpi_title, item.getGoodsTitle());
                helper.setText(R.id.tv_ffpi_price, item.getGoodsPrice());
                Glide.with(mActivity)
                        .load(item.getGoodsImage())
                        .into((ImageView) helper.getView(R.id.iv_ffpi));
            }
        }
    }
}
