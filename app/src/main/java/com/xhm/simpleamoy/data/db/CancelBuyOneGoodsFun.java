package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xhm.simpleamoy.data.entity.BuyGoods;

import java.util.List;

/**
 * Created by xhm on 2018/5/5.
 */

public abstract class CancelBuyOneGoodsFun {
    private BuyGoods mBuyGoods;
    public CancelBuyOneGoodsFun(BuyGoods buyGoods) {
        mBuyGoods=buyGoods;
        cancelBuyOneGoods();
    }

    public void cancelBuyOneGoods(){

        AVQuery avQuery = new AVQuery("Products");
        avQuery.whereEqualTo("userName", mBuyGoods.getSellUserName());
        avQuery.whereEqualTo("goodsUUID", mBuyGoods.getSellUUID());
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e==null)
                {
                    if (!list.isEmpty())
                    {

                        AVObject avObject= (AVObject) list.get(0);

                        if (avObject.getBoolean("isByBuy"))
                        {
                            AVObject todo = AVObject.createWithoutData("Products", ((AVObject) list.get(0)).getObjectId());
                            todo.put("isByBuy",false);
                            todo.put("buyUserName",null);
                            todo.saveInBackground();

                        }
                        else cancelBuyOneGoodsFaild("错误！");

                    }
                    else  cancelBuyOneGoodsFaild("错误！");


                }
                else cancelBuyOneGoodsFaild(e.getMessage());

            }
        });

    }
    public abstract void cancelBuyOneGoodsSucess();
    public abstract void cancelBuyOneGoodsFaild(String msg);
}
