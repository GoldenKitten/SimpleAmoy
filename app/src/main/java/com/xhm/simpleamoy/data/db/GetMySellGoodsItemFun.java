package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.data.entity.SellGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class GetMySellGoodsItemFun {
    private List<SellGoods> mSellGoods;
    private String mUserName;
    public GetMySellGoodsItemFun(String userName) {
        mUserName=userName;
        getMySellGoodsItem();
    }

    public void getMySellGoodsItem()
    {
      mSellGoods=new ArrayList<SellGoods>();



    }
    public abstract void getMySellGoodsItemSucess(List<SellGoods> sellGoods);
    public abstract void getMySellGoodsItemFaild(String msg);
}
