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
import android.widget.TextView;

import com.vondear.rxtools.RxActivityTool;
import com.xhm.simpleamoy.R;
import com.xhm.simpleamoy.activity.MyBuyActivity;
import com.xhm.simpleamoy.activity.MyIssueActivity;
import com.xhm.simpleamoy.activity.MySellActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xhm on 2018/4/27.
 */

public class PersonFragment extends Fragment {
    private Activity mActivity;
    private static FragmentManager mFManager;
    private static PersonFragment mPersonFragment;
    private ViewHolder mViewHolder;
    public static PersonFragment newInstance(FragmentManager fManager) {
        mFManager = fManager;
        if (mPersonFragment == null) {
            mPersonFragment = new PersonFragment();
        }
        return mPersonFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person,
                container, false);
        mViewHolder=new ViewHolder(view);
        return view;
    }

    @Override
    public void onStart() {
        mViewHolder=new ViewHolder(getView());
        mViewHolder.tvFpMyIssue.setOnClickListener(v -> {
            RxActivityTool.skipActivity(mActivity, MyIssueActivity.class);
        });
        mViewHolder.tvFpMyBuy.setOnClickListener(v -> {
            RxActivityTool.skipActivity(mActivity, MyBuyActivity.class);

        });
        mViewHolder.tvFpMySell.setOnClickListener(v -> {
            RxActivityTool.skipActivity(mActivity, MySellActivity.class);
        });
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
    }

    static class ViewHolder {
        @BindView(R.id.tv_fp_my_issue)
        TextView tvFpMyIssue;
        @BindView(R.id.tv_fp_my_buy)
        TextView tvFpMyBuy;
        @BindView(R.id.tv_fp_my_sell)
        TextView tvFpMySell;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
