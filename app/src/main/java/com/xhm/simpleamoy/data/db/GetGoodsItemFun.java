package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class GetGoodsItemFun {
    private List<FirstPagerGoods> mFirstPagerGoods;
    private String mSchoolAddress;
    public GetGoodsItemFun(String schoolAddress) {
        mSchoolAddress=schoolAddress;
        getGoodsItem();
    }

    public void getGoodsItem(){
    }
    public abstract void getGoodsItemSucess(List<FirstPagerGoods> firstPagerGoods);
    public abstract void getGoodsItemFaild(String msg);
}
