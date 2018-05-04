package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.IssueGoods;
import com.xhm.simpleamoy.data.entity.MailInfo;

/**
 * Created by xhm on 2018/5/4.
 */

public abstract class GetMailInfo {
    private MailInfo mMailInfo;
    private String mUserName;
    private String mMailAddress;
    public GetMailInfo(String userName,String mailAddress) {
        mUserName=userName;
        mMailAddress=mailAddress;
        getMailInfo();
    }

    private void getMailInfo() {
    }

    public abstract void getMailInfoSucess(MailInfo mailInfo);
    public abstract void getMailInfoFaild(String msg);
}
