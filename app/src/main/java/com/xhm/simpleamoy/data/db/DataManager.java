package com.xhm.simpleamoy.data.db;

import java.util.List;

/**
 * Created by xhm on 2018/4/29.
 */

public abstract class DataManager {
    private List mList;
    private String mUserName;
    public DataManager(String userName,int code) {
        mUserName=userName;
        if(code==0){
            getAddressAndImage();
        }
    }
    public void getAddressAndImage(){

    }
    public abstract void getDataSucess(List list);
    public abstract void getDataFaild(String msg);
}
