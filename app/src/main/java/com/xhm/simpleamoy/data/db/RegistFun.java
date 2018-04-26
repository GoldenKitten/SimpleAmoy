package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
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
        Log.i("regist:", " !!!!!!");
        AVObject user=new AVObject("RegistUser");
        user.put("userName",mRegistUser.getUserName());
        user.put("password",mRegistUser.getPassword());
        user.put("schoolAddress",mRegistUser.getSchoolAddress());
        user.put("email",mRegistUser.getEmail());
        user.put("headImage",new AVFile("RegistUserPic", mRegistUser.getHeadImage()));
        user.saveInBackground();


    }
    public abstract void registSucess();
    public abstract void registFaild(String msg);
}
