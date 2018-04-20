package com.xhm.simpleamoy;

import android.app.Application;
import android.os.Environment;
import com.vondear.rxtools.RxTool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by xhm on 2018/4/12.
 */

public class MyApp extends Application {
    private static MyApp mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        //初始化工具类
        RxTool.init(this);
        //捕获全局异常
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + ".errorlog";
            File file = new File(path);
            try {
                PrintWriter printWriter = new PrintWriter(file);
                e.printStackTrace(printWriter);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            System.exit(0);
        });
    }
    public static MyApp newInstance(){return mContext;}
}
