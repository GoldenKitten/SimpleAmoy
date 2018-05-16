package com.xhm.simpleamoy.data.db;

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













    }
    public abstract void cancelSellGoodsSucess();
    public abstract void cancelSellGoodsFaild(String msg);
}
