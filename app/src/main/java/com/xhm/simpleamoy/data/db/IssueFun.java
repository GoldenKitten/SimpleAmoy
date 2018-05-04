package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.xhm.simpleamoy.data.entity.IssueGoods;
import com.xhm.simpleamoy.data.entity.RegistUser;
import com.xhm.simpleamoy.utils.CheckUserUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class IssueFun {
    private IssueGoods mIssueGoods;
    public IssueFun(IssueGoods issueGoods) {
        mIssueGoods=issueGoods;
        issue();
    }

    public void issue(){

        AVObject products =new AVObject("Products");
        products.put("userName",mIssueGoods.getUserName());
        products.put("schoolAddress",mIssueGoods.getSchoolAddress());
        products.put("goodsUUID",mIssueGoods.getGoodsUUID());
        products.put("goodsName",mIssueGoods.getGoodsName());
        products.put("goodsDes",mIssueGoods.getGoodsDes());
        products.put("goodsPrice",mIssueGoods.getGoodsPrice());
        products.put("weixing",mIssueGoods.getWeixing());
        products.put("qq",mIssueGoods.getQq());
        products.put("mobile",mIssueGoods.getMobile());
        products.put("mainGoodsPic",new AVFile(mIssueGoods.getUserName()+".productpic",mIssueGoods.getMainGoodsPic()));
        int i=mIssueGoods.getGoodsPic().size();
        ArrayList arrayList=new ArrayList();
        for (int j=0;j<i;j++)
        {
            AVFile avFile=new AVFile(mIssueGoods.getUserName()+".goodsPic",mIssueGoods.getGoodsPic().get(j));
            avFile.saveInBackground();
            arrayList.add(avFile);


        }
        products.put("goodsPicArrayList",arrayList);

        products.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e==null)
                {
                    issueSucess();
                }
                else {issueFaild(e.getMessage());}
            }
        });




    }
    public abstract void issueSucess();
    public abstract void issueFaild(String msg);
}
