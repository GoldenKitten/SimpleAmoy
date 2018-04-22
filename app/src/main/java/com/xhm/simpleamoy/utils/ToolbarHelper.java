package com.xhm.simpleamoy.utils;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.xhm.simpleamoy.R;

/**
 * Created by xhm on 2018/4/21.
 */

public class ToolbarHelper {
    private Toolbar mToolbar;

    public ToolbarHelper(Toolbar toolbar) {
        this.mToolbar = toolbar;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setTitle(String title) {
        TextView titleTV = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        titleTV.setText(title);
    }
    public void setIcon(int icon){
        mToolbar.setNavigationIcon(icon);
    }
}
