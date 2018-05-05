package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class GetMyBuyGoodsItemFun {
    private List<FirstPagerGoods> mFirstPagerGoods;
    private String mUserName;
    public GetMyBuyGoodsItemFun(String userName) {
        mUserName=userName;
        getMyBuyGoodsItem();
    }

    public void getMyBuyGoodsItem()
    {
      mFirstPagerGoods=new ArrayList<FirstPagerGoods>();



    }
    public abstract void getMyBuyGoodsItemSucess(List<FirstPagerGoods> firstPagerGoods);
    public abstract void getMyBuyGoodsItemFaild(String msg);
}
