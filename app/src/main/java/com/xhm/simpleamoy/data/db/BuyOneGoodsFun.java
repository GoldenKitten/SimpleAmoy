package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.xhm.simpleamoy.data.entity.BuyGoods;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import java.util.List;

/**
 * Created by xhm on 2018/5/5.
 */

public abstract class BuyOneGoodsFun {
   private BuyGoods mBuyGoods;
    public BuyOneGoodsFun(BuyGoods buyGoods) {
        mBuyGoods=buyGoods;
        buyOneGoods();
    }

    public void buyOneGoods(){
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

                        if (!avObject.getBoolean("isByBuy"))
                        {
                            AVObject todo = AVObject.createWithoutData("Products", ((AVObject) list.get(0)).getObjectId());
                            todo.put("isByBuy",true);
                            todo.put("buyUserName",mBuyGoods.getBuyUserName());
                            todo.saveInBackground();
                            buyOneGoodsSucess();

                        }
                        else buyOneGoodsFaild("商品已被预定！");

                    }
                    else  buyOneGoodsFaild("错误！");


                }
                else buyOneGoodsFaild(e.getMessage());

            }
        });

    }
    public abstract void buyOneGoodsSucess();
    public abstract void buyOneGoodsFaild(String msg);
}
