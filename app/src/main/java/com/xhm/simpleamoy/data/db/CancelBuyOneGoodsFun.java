package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.BuyGoods;

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

    }
    public abstract void cancelBuyOneGoodsSucess();
    public abstract void cancelBuyOneGoodsFaild(String msg);
}
