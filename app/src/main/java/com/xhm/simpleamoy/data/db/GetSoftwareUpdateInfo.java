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
    }
    public abstract void getSoftwareUpdateSucess(AppInfo appInfo);
    public abstract void getSoftwareUpdateFailed(String msg);
}
