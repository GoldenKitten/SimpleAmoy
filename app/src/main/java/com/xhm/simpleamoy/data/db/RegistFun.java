package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.RegistUser;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class RegistFun {
    private RegistUser mRegistUser;
    public RegistFun(RegistUser registUser) {
        mRegistUser=registUser;
        regist();
    }

    public void regist(){

    }
    public abstract void registSucess();
    public abstract void registFaild();
}
