package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
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
        Log.i( "!!!deleteMyIssueItem: ", String.valueOf(mFirstPagerGoodsList.size()));
        ArrayList arrayList=new ArrayList();
        for(int i=0;i<mFirstPagerGoodsList.size();i++)
        {
            String id=mFirstPagerGoodsList.get(i).getGoodsUUID();
            Log.i( "id: ", id);
            arrayList.add(id);

        }
        Log.i( "deleteMyIssueItem: ", String.valueOf(arrayList.size()));
        AVQuery<AVObject> avQuery = new AVQuery<>("Products");
        avQuery.whereContainedIn("goodsUUID",arrayList);
        avQuery.findInBackground(new FindCallback<AVObject>() {

            public void done(List<AVObject> list, AVException e) {

                Log.i( "list: ", String.valueOf(list.size()));
                AVObject.deleteAllInBackground(list, new DeleteCallback() {

                    public void done(AVException e) {
                        if (e != null) {
                            deleteMyIssueItemFaild(e.getMessage());
                        } else {
                            deleteMyIssueItemSucess();
                        }
                    }
                });
            }
        });

    }
    public abstract void deleteMyIssueItemSucess();
    public abstract void deleteMyIssueItemFaild(String msg);
}
