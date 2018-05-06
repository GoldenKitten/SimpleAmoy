package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.BuyGoods;
import com.xhm.simpleamoy.data.entity.SellGoods;

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
    }
    public abstract void cancelSellGoodsSucess();
    public abstract void cancelSellGoodsFaild(String msg);
}
