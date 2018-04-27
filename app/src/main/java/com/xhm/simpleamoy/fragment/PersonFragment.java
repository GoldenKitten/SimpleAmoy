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

public class PersonFragment extends Fragment {
    private static FragmentManager mFManager;
    private static PersonFragment mPersonFragment;
    public static PersonFragment newInstance(FragmentManager fManager){
        mFManager=fManager;
        if(mPersonFragment ==null){
            mPersonFragment =new PersonFragment();
        }
        return mPersonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_person,
                container,false);
        return view;
    }
}
