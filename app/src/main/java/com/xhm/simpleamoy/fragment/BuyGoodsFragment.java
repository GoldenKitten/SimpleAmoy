package com.xhm.simpleamoy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhm.simpleamoy.R;

/**
 * Created by xhm on 2018/4/27.
 */

public class BuyGoodsFragment extends Fragment {
    private static FragmentManager mFManager;
    private static BuyGoodsFragment mBuyGoodsFragment;
    private Activity mActivity;
    public static BuyGoodsFragment newInstance(FragmentManager fManager){
        mFManager=fManager;
        if(mBuyGoodsFragment ==null){
            mBuyGoodsFragment =new BuyGoodsFragment();
        }
        return mBuyGoodsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity=getActivity();
        View view=inflater.inflate(R.layout.fragment_person,
                container,false);
        return view;
    }
}
