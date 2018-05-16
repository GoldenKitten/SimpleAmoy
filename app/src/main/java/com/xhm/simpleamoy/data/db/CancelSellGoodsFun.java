package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.xhm.simpleamoy.data.entity.BuyGoods;
import com.xhm.simpleamoy.data.entity.SellGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/5/5.
 */

public abstract class CancelSellGoodsFun {
   private List<SellGoods> mSellGoods;
    public CancelSellGoodsFun(List<SellGoods> sellGoods) {
        mSellGoods=sellGoods;
        cancelSellOneGoods();
    }

    public void cancelSellOneGoods(){
        Log.i("cancelSellOneGoods: ", String.valueOf(mSellGoods.size()));
        ArrayList arrayList=new ArrayList();
        for(int i=0;i<mSellGoods.size();i++)
        {
            String id=mSellGoods.get(i).getSellUUID();
            arrayList.add(id);

        }
        AVQuery avQuery = new AVQuery("Products");
        avQuery.whereContainedIn("goodsUUID",arrayList);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {

                if (e==null)
                {
                    if (!list.isEmpty()) {
                        for (int i=0;i<list.size();i++) {
                            AVObject avObject = (AVObject) list.get(i);
                            if (avObject.getBoolean("isByBuy")) {
                                AVObject todo = AVObject.createWithoutData("Products", ((AVObject) list.get(i)).getObjectId());
                                todo.put("isByBuy", false);
                                todo.put("buyUserName", null);
                                todo.saveInBackground();

                            } else cancelSellGoodsFaild("错误！");





                        }
                        cancelSellGoodsSucess();
                    }
                    else cancelSellGoodsFaild("!!!!!!！");


                }
                else cancelSellGoodsFaild(e.getMessage());



            }


        });












    }
    public abstract void cancelSellGoodsSucess();
    public abstract void cancelSellGoodsFaild(String msg);
}
