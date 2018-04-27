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

public class FirstPagerFragment extends Fragment {
    private static FragmentManager mFManager;
    private static FirstPagerFragment mFirstPagerFragment;
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
        View view=inflater.inflate(R.layout.fragment_first_pager,
                container,false);
        return view;
    }
}
