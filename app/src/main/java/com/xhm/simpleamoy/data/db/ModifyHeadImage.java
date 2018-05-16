package com.xhm.simpleamoy.data.db;

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
    }
    public abstract void modifySucess();
    public abstract void modifyFailed(String msg);
}
