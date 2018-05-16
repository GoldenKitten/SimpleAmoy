package com.xhm.simpleamoy.data.db;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
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
        AVQuery avQuery = new AVQuery("Products");
        avQuery.whereEqualTo("userName", mUserName);
        avQuery.whereEqualTo("isByBuy", true);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        mSellGoods=new ArrayList<SellGoods>();
                        for (int i = 0; i < list.size(); i++) {
                            AVObject avObject = (AVObject) list.get(i);
                            avObject.getAVFile("mainGoodsPic").getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, AVException e) {
                                    if (e == null) {
                                        SellGoods sellGoods = new SellGoods();
                                        sellGoods.setSellUUID(avObject.getString("goodsUUID"));
                                        sellGoods.setSellUserName(mUserName);
                                        sellGoods.setSellMainPic(bytes);
                                        sellGoods.setBuyGoodsUserName(avObject.getString("buyUserName"));
                                        sellGoods.setSellGoodsTitle(avObject.getString("goodsName"));
                                        sellGoods.setSellPrice(avObject.getString("goodsPrice"));
                                        mSellGoods.add(sellGoods);

                                        if (mSellGoods.size() == list.size()) {
                                            getMySellGoodsItemSucess(mSellGoods);
                                        }

                                    } else getMySellGoodsItemFaild(e.getMessage());
                                }
                            });
                        }

                    } else {//getMyBuyGoodsItemFaild("暂时无购买商品！");
                        mSellGoods=new ArrayList<>();
                        getMySellGoodsItemSucess(mSellGoods);
                    }
                } else getMySellGoodsItemFaild(e.getMessage());


            }


        });


    }
    public abstract void getMySellGoodsItemSucess(List<SellGoods> sellGoods);
    public abstract void getMySellGoodsItemFaild(String msg);
}
