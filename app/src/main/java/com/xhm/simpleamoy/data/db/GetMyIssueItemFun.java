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

public abstract class GetMyIssueItemFun {
    private List<FirstPagerGoods> mFirstPagerGoods;
    private String mUserName;
    public GetMyIssueItemFun(String userName) {
        mUserName=userName;
        getMyIssueItem();
    }

    public void getMyIssueItem()
    {
       mFirstPagerGoods=new ArrayList<FirstPagerGoods>();
       AVQuery avQuery=new AVQuery("Products");
       avQuery.whereEqualTo("userName", mUserName);
       avQuery.findInBackground(new FindCallback()
        {
            @Override
            public void done(List list, AVException e) {
                if (e == null) {

                    if (!(list.isEmpty()))

                    {

                        for (int i = 0; i < list.size(); i++) {
                            AVObject avObject = (AVObject) list.get(i);

                            avObject.getAVFile("mainGoodsPic").getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, AVException e) {
                                    if (e == null) {
                                        FirstPagerGoods firstPagerGoods = new FirstPagerGoods();
                                        firstPagerGoods.setGoodsUUID(avObject.getString("goodsUUID"));
                                        firstPagerGoods.setUserName(avObject.getString("userName"));
                                        firstPagerGoods.setGoodsImage(bytes);
                                        firstPagerGoods.setGoodsTitle(avObject.getString("goodsName"));
                                        firstPagerGoods.setGoodsPrice(avObject.getString("goodsPrice"));
                                        mFirstPagerGoods.add(firstPagerGoods);

                                        if (mFirstPagerGoods.size() == list.size()) {
                                            getMyIssueItemSucess(mFirstPagerGoods);
                                        }

                                    } else getMyIssueItemFaild(e.getMessage());
                                }
                            });
                        }
                    } else getMyIssueItemFaild("没有商品！");
                } else getMyIssueItemFaild(e.getMessage());
            }


        });



    }
    public abstract void getMyIssueItemSucess(List<FirstPagerGoods> firstPagerGoods);
    public abstract void getMyIssueItemFaild(String msg);
}
