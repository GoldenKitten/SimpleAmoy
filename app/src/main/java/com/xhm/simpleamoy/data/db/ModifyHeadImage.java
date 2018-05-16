package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by xhm on 2018/5/16.
 */

public abstract class ModifyHeadImage {
    private String mUserName;
    private byte[] mHeadImage;
    public ModifyHeadImage(String userName,byte[] headImage) {
        mUserName = userName;
        mHeadImage=headImage;
        modifyHeadImage();
    }

    private void modifyHeadImage() {
        AVQuery avQuery=new AVQuery("RegistUser");
        avQuery.whereEqualTo("userName",mUserName);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {

                if (e==null)
                {
                    if (!list.isEmpty())
                    {AVObject avObject=AVObject.createWithoutData("RegistUser",((AVObject) list.get(0)).getObjectId());

                    avObject.put("headImage",new AVFile(mUserName+ ".RegistUserPic",mHeadImage));
                    avObject.saveInBackground();
                    modifySucess();
                }
                }
                else modifyFailed(e.getMessage());

            }


        });
    }
    public abstract void modifySucess();
    public abstract void modifyFailed(String msg);
}
