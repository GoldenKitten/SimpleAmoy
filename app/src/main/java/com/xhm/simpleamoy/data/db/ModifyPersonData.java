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

    }
    public abstract void modifySucess();
    public abstract void modifyFailed(String msg);
}
