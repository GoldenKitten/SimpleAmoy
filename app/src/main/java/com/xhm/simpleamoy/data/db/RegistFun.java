package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.utils.CheckUserUtil;

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
        new CheckUserUtil(mRegistUser.getUserName()){
            @Override
            public void getRegistState(boolean msg) {
                if(msg){
                    user.put("userName", mRegistUser.getUserName());
                    user.put("password", mRegistUser.getPassword());
                    user.put("schoolAddress", mRegistUser.getSchoolAddress());
                    user.put("email", mRegistUser.getEmail());
                    user.put("headImage", new AVFile("RegistUserPic", mRegistUser.getHeadImage()));
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                registSucess();
                            } else {
                                e.getStackTrace();
                            }
                        }
                    });
                }
                else {
                    registFaild("用户名存在，请重新输入！");
                }
            }
        };

    }
    public abstract void registSucess();
    public abstract void registFaild(String msg);
}
