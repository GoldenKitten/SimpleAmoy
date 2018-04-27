package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVObject;
import com.xhm.simpleamoy.data.entity.LoginUser;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.utils.CheckUserUtil;

import java.util.List;

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
        new CheckUserUtil(mLoginUser.getUsername()) {
            @Override
            public void getRegistState(boolean msg) {
                if (msg)
                {
                    loginFaild("用户名错误，请重新输入！");

                }
            }

            @Override
            public void checkPassword(List list) {
                AVObject user=(AVObject)list.get(0);
                String   passWord=user.getString("password");
                if (passWord.equals(mLoginUser.getPassword()))
                {
                    loginSucess();

                }
                else {loginFaild("密码错误，请重新输入！");}
            }
        };

    }
    public abstract void loginSucess();
    public abstract void loginFaild(String msg);
}
