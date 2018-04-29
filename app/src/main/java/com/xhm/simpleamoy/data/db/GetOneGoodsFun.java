package com.xhm.simpleamoy.data.db;

import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class GetOneGoodsFun {
    private IssueGoods mIssueGoods;
    private String mUserName;
    private String mGoodsUUID;
    public GetOneGoodsFun(String userName,String goodsUUID) {
        mUserName=userName;
        mGoodsUUID=goodsUUID;
        getOneGoods();
    }

    public void getOneGoods(){
    }
    public abstract void getGoodsItemSucess(IssueGoods issueGoods);
    public abstract void getGoodsItemFaild(String msg);
}
