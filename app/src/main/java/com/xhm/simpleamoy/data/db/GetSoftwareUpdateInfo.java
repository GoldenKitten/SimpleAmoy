package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.AppInfo;

/**
 * Created by xhm on 2018/5/24.
 */

public abstract class GetSoftwareUpdateInfo {
    public GetSoftwareUpdateInfo() {
        getSoftwareUpdateInfo();
    }

    private void getSoftwareUpdateInfo() {
        AVQuery avQuery=new AVQuery("apk");
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
       if (e==null)
       {
           if (list.isEmpty())
           {
               AVObject avObject= (AVObject) list.get(0);
               String versionName= avObject.getString("versionName");
               int versionCode= (int) avObject.getNumber("versionCode");
               AVFile avFile=avObject.getAVFile("url");
               String url=avFile.getUrl();
               AppInfo appInfo=new AppInfo();
               appInfo.setUrl(url);
               appInfo.setVersionCode(versionCode);
               appInfo.setVersionName(versionName);
               getSoftwareUpdateSucess(appInfo);

           }
           else getSoftwareUpdateFailed("未查到数据");


       }
       else getSoftwareUpdateFailed(e.getMessage());
            }


        });
    }
    public abstract void getSoftwareUpdateSucess(AppInfo appInfo);
    public abstract void getSoftwareUpdateFailed(String msg);
}
