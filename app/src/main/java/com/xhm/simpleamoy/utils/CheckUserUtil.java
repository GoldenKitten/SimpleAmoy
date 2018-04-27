package com.xhm.simpleamoy.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
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

            public void done(List list, AVException e) {

                if (e == null) {
                    if (list.isEmpty())
                    {
                       getRegistState(true);

                    }
                    else {
                        getRegistState(false);
                        checkPassword(list);
                    }


                } else {
                    e.getStackTrace();
                }
            }

        });

    }

  public abstract void getRegistState(boolean msg);
    public  abstract  void checkPassword(List list);
}
