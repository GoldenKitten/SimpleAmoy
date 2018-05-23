package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xhm.simpleamoy.data.entity.User;

import java.util.List;

/**
 * Created by xhm on 2018/5/19.
 */

public abstract class GetPersonDataFun {
    private String mUserName;
    public GetPersonDataFun(String username) {
        mUserName=username;
        getPersonData();
    }

    private void getPersonData() {
        AVQuery avQuery = new AVQuery("RegistUser");
        avQuery.whereEqualTo("userName", mUserName);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e==null) {
                    if (list.isEmpty()) {
                        User user = new User();
                        AVObject avObject = (AVObject) list.get(0);
                        user.setSchool(avObject.getString("schoolAddress"));
                        user.setMail(avObject.getString("email"));
                        user.setUsername(mUserName);
                        getPersonDataSucess(user);
                    }
                    else getPersonDataFaild("无此用户");
                }
                else
                    getPersonDataFaild(e.getMessage());
            }


        });

    }
    public abstract void getPersonDataSucess(User user);
    public abstract void getPersonDataFaild(String msg);
}
