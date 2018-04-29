package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.vondear.rxtools.view.RxToast;
import com.xhm.simpleamoy.utils.CheckUserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/29.
 */

public abstract class DataManager {
    private List mList=new ArrayList();
    private String mUserName;
    public DataManager(String userName,int code) {
        mUserName=userName;
        if(code==0){
            getAddressAndImage();
        }
    }
    public void getAddressAndImage(){
        AVQuery avQuery = new AVQuery("RegistUser");
        avQuery.whereEqualTo("userName", mUserName);
        avQuery.include("headImage");
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e==null)
                {
                    if (!list.isEmpty()) {
                        AVObject avObject = (AVObject) list.get(0);
                        AVFile  avFile=avObject.getAVFile("headImage");
                        String st = avObject.getString("schoolAddress");
                        avFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if(e==null)
                                {
                                    mList.add(bytes);
                                    mList.add(st);
                                    getDataSucess(mList);


                                }
                            }
                        });

                    }
                    else getDataFaild("账号错误");
                }
                else {e.getStackTrace();}

            }


        });


    }
    public abstract void getDataSucess(List list);
    public abstract void getDataFaild(String msg);
}
