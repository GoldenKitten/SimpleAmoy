package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.BuyGoods;
import com.xhm.simpleamoy.data.entity.IssueGoods;

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
    }
    public abstract void buyOneGoodsSucess();
    public abstract void buyOneGoodsFaild(String msg);
}
