package com.xhm.simpleamoy.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import java.util.List;



/**
 * Created by ASUS on 2018/4/26.
 */

public  abstract  class CheckUserUtil
{


    public CheckUserUtil(String myUserName) {
        checkUserName(myUserName);
    }

    public   void checkUserName(String  userName) {
        AVQuery avQuery = new AVQuery("RegistUser");
        avQuery.whereEqualTo("userName", userName);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {

                if (e == null) {
                    if (list.isEmpty())
                    {
                       getRegistState(true);

                    }
                    else {
                        getRegistState(false);
                    }


                } else {
                    e.getStackTrace();
                }
            }

        });

    }

  public abstract void getRegistState(boolean msg);
}
