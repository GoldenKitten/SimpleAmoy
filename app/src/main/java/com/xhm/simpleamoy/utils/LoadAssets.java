package com.xhm.simpleamoy.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xhm on 2018/5/4.
 */

public class LoadAssets {
    public static void initData(Context context,String fileName){
        new Thread(() -> {
            InputStream stream=null;
            FileOutputStream fos=null;
            File files=context.getFilesDir();
            File file=new File(files,fileName);
            if(file.exists()){
                return;
            }
            else {
                //读取第三方资产目录下的文件
                try {
                    stream = context.getAssets().open(fileName);
                    fos = new FileOutputStream(file);
                    byte[] bs = new byte[1024];
                    int temp = -1;
                    while ((temp = stream.read(bs)) != -1) {
                        fos.write(bs, 0, temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null && fos != null) {
                        try {
                            stream.close();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
