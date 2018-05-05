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

public abstract class DeleteMyIssueItemFun {
    private List<FirstPagerGoods> mFirstPagerGoodsList;
    public DeleteMyIssueItemFun(List<FirstPagerGoods> firstPagerGoodsList) {
        mFirstPagerGoodsList=firstPagerGoodsList;
        deleteMyIssueItem();
    }

    public void deleteMyIssueItem()
    {

    }
    public abstract void deleteMyIssueItemSucess();
    public abstract void deleteMyIssueItemFaild(String msg);
}
