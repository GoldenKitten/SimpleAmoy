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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;
import com.xhm.simpleamoy.C;
import com.xhm.simpleamoy.MyApp;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.data.db.GetGoodsItemFun;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/27.
 */

public class FirstPagerFragment extends Fragment {
    private Activity mActivity;
    private static FragmentManager mFManager;
    private static FirstPagerFragment mFirstPagerFragment;
    private RecyclerView mRecyclerView;
    //private byte[] mHeadImage;

    public static FirstPagerFragment newInstance(FragmentManager fManager){
        mFManager=fManager;
        if(mFirstPagerFragment==null){
            mFirstPagerFragment=new FirstPagerFragment();
        }
        return mFirstPagerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity=getActivity();
        View view = inflater.inflate(R.layout.fragment_first_pager,
                container,false);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rv_ffp);
       // Drawable drawable=getResources().getDrawable(
               // R.drawable.circle_elves_ball);
       // mHeadImage= RxImageTool.drawable2Bytes(drawable, Bitmap.CompressFormat.PNG);
        return view;
    }

    @Override
   public void onStart() {
        super.onStart();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
      RxDialogLoading rxDialogLoading = new RxDialogLoading(mActivity);
        rxDialogLoading.setLoadingText("加载中 ...");
        rxDialogLoading.setCancelable(false);
        rxDialogLoading.show();
        new Thread(() ->
                new GetGoodsItemFun(
                        RxSPTool.getString(
                                MyApp.newInstance(),
                                C.Splash.SCHOOLADDRESS)){
            @Override
            public void getGoodsItemSucess(List<FirstPagerGoods> firstPagerGoods) {
                rxDialogLoading.cancel();
                FirstPagerAdapter firstPagerAdapter=new FirstPagerAdapter(
                        R.layout.fragment_first_pager_item,
                        firstPagerGoods);
                firstPagerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        FragmentTransaction transaction=mFManager.beginTransaction();
                        BuyGoodsFragment buyGoodsFragment=BuyGoodsFragment
                                .newInstance(mFManager,
                                        firstPagerGoods.get(position).getUserName(),
                                        firstPagerGoods.get(position).getGoodsUUID());
                        transaction.replace(R.id.fl_content,buyGoodsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        //initToolbar("首页");

                    }
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
