package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xhm.simpleamoy.data.entity.User;

import java.util.List;

/**
 * Created by xhm on 2018/5/16.
 */

public abstract class ModifyPersonData {
    private User mUser;
    public ModifyPersonData(User user) {
        mUser=user;
        modifyPersonData();
    }

    private void modifyPersonData() {
        AVQuery avQuery = new AVQuery("RegistUser");
        avQuery.whereEqualTo("userName", mUser.getUsername());
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {

                            AVObject avObject = (AVObject) list.get(0);
                            AVObject todo = AVObject.createWithoutData("RegistUser", ((AVObject) list.get(0)).getObjectId());
                            todo.put("schoolAddress", mUser.getSchool());
                            todo.put("email", mUser.getMail());
                            todo.saveInBackground();
                            modifySucess();



                    } else modifyFailed("错误！");


                } else modifyFailed(e.getMessage());

            }

        });

    }
    public abstract void modifySucess();
    public abstract void modifyFailed(String msg);
}
