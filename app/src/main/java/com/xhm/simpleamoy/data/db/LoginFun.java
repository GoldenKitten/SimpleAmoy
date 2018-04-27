package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.LoginUser;
import com.xhm.simpleamoy.data.entity.RegistUser;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class LoginFun {
    private LoginUser  mLoginUser;
    public LoginFun(LoginUser loginUser) {
        mLoginUser=loginUser;
        login();
    }

    public void login(){

    }
    public abstract void loginSucess();
    public abstract void loginFaild(String msg);
}
