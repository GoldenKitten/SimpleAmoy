package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.xhm.simpleamoy.data.entity.IssueGoods;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.utils.CheckUserUtil;

import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class IssueFun {
    private IssueGoods mIssueGoods;
    public IssueFun(IssueGoods issueGoods) {
        mIssueGoods=issueGoods;
        issue();
    }

    public void issue(){
    }
    public abstract void issueSucess();
    public abstract void issueFaild(String msg);
}
