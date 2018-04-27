package com.xhm.simpleamoy.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.xhm.simpleamoy.R;
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
    private byte[] mHeadImage;

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
        Drawable drawable=getResources().getDrawable(
                R.drawable.circle_elves_ball);
        mHeadImage= RxImageTool.drawable2Bytes(drawable, Bitmap.CompressFormat.PNG);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<FirstPagerGoods> data=new ArrayList<FirstPagerGoods>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        for(int i=0;i<20;i++){
            FirstPagerGoods firstPagerGoods=new FirstPagerGoods();
            firstPagerGoods.setGoodsTitle("标题"+i);
            firstPagerGoods.setGoodsPrice("20."+i);
            firstPagerGoods.setGoodsImage(mHeadImage);
            data.add(firstPagerGoods);
        }
        mRecyclerView.setAdapter(new FirstPagerAdapter(
                R.layout.fragment_first_pager_item,
                data));
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
