package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xhm.simpleamoy.data.entity.IssueGoods;
import com.xhm.simpleamoy.data.entity.MailInfo;

import java.util.List;

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
        AVQuery avQuery=new AVQuery("RegistUser");
        avQuery.whereEqualTo("userName", mUserName);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {


                if(e==null)
                {
                 if (!list.isEmpty())
                 {

                     AVObject avObject= (AVObject) list.get(0);
                     if(avObject.getString("email").equals(mMailAddress))
                     {
                      mMailInfo=new MailInfo();
                      mMailInfo.setUserName(mUserName);
                      mMailInfo.setPassword(avObject.getString("password"));
                      mMailInfo.setEmailAddress(mMailAddress);
                      getMailInfoSucess(mMailInfo);

                     }
else  getMailInfoFaild("输入邮箱不符合绑定邮箱！");
                 }
                  else getMailInfoFaild("用户名不存在！");


                }
                else  getMailInfoFaild(e.getMessage());

            }


        });


    }

    public abstract void getMailInfoSucess(MailInfo mailInfo);
    public abstract void getMailInfoFaild(String msg);
}
