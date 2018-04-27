package com.xhm.simpleamoy.fragment;

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

public class IssueFragment extends Fragment {
    private static FragmentManager mFManager;
    private static IssueFragment mIssueFragment;
    public static IssueFragment newInstance(FragmentManager fManager){
        mFManager=fManager;
        if(mIssueFragment==null){
            mIssueFragment=new IssueFragment();
        }
        return mIssueFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_issue,
                container,false);
        return view;
    }
}
