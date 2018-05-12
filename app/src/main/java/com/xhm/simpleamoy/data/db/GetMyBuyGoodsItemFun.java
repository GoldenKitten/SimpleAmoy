package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;

import java.util.AbstractList;
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
        AVQuery avQuery = new AVQuery("Products");
        avQuery.whereEqualTo("buyUserName", mUserName);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {

                        mFirstPagerGoods=new ArrayList<>();
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
                                            getMyBuyGoodsItemSucess(mFirstPagerGoods);
                                        }

                                    } else getMyBuyGoodsItemFaild(e.getMessage());
                                }
                            });
                        }

























                    } else getMyBuyGoodsItemFaild("暂时无购买商品！");
                } else getMyBuyGoodsItemFaild(e.getMessage());


            }


        });




    }
    public abstract void getMyBuyGoodsItemSucess(List<FirstPagerGoods> firstPagerGoods);
    public abstract void getMyBuyGoodsItemFaild(String msg);
}
