package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.User;

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
    }
    public abstract void getPersonDataSucess(User user);
    public abstract void getPersonDataFaild(String msg);
}
