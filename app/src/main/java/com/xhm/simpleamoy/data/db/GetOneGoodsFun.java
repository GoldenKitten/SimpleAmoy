package com.xhm.simpleamoy.data.db;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.xhm.simpleamoy.data.entity.FirstPagerGoods;
import com.xhm.simpleamoy.data.entity.IssueGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xhm on 2018/4/21.
 */

public abstract class GetOneGoodsFun {
    private IssueGoods mIssueGoods;
    private String mUserName;
    private String mGoodsUUID;

    public GetOneGoodsFun(String userName, String goodsUUID) {
        mUserName = userName;
        mGoodsUUID = goodsUUID;
        getOneGoods();
    }

    public void getOneGoods() {
        Log.i("haha1","1");

        AVQuery avQuery = new AVQuery("Products");
        mIssueGoods = new IssueGoods();
        avQuery.whereEqualTo("userName", mUserName);
        avQuery.whereEqualTo("goodsUUID", mGoodsUUID);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        Log.i("haha3","3");
                        AVObject avObject = (AVObject) list.get(0);
                        mIssueGoods.setSchoolAddress(avObject.getString("schoolAddress"));
                        mIssueGoods.setWeixing(avObject.getString("weixing"));
                        mIssueGoods.setMobile(avObject.getString("mobile"));
                        mIssueGoods.setQq(avObject.getString("qq"));
                        mIssueGoods.setUserName(avObject.getString(mUserName));
                        mIssueGoods.setGoodsUUID(avObject.getString(mGoodsUUID));
                        mIssueGoods.setGoodsName(avObject.getString("goodsName"));
                        mIssueGoods.setGoodsPrice(avObject.getString("goodsPrice"));
                        mIssueGoods.setGoodsDes(avObject.getString("goodsDes"));
                        mIssueGoods.setByBuy(avObject.getBoolean("isByBuy"));
                        mIssueGoods.setBuyUserName(avObject.getString("buyUserName"));
                        AVFile mainpic = avObject.getAVFile("mainGoodsPic");
                        mainpic.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e)
                            {
                                if (e == null) {
                                    Log.i("haha2","2");
                                    mIssueGoods.setMainGoodsPic(bytes);
                                    AVQuery avQuery1 = new AVQuery("_File");
                                    avQuery1.whereEqualTo("name",mUserName + "." + mGoodsUUID + ".goodsPic");
                                    avQuery1.findInBackground(new FindCallback() {
                                        @Override
                                        public void done(List list, AVException e) {
                                          if (e == null)
                                          {

                                               if (!list.isEmpty())
                                               {
                                                    Log.d("", "done: "+list.size());
                                                    ArrayList arrayList = new ArrayList();
                                                    for (int i = 0; i < list.size(); i++) {
                                                        AVFile avFile = AVFile.withAVObject((AVObject) list.get(i));
                                                        avFile.getDataInBackground(new GetDataCallback() {
                                                            @Override
                                                            public void done(byte[] bytes, AVException e) {
                                                                arrayList.add(bytes);
                                                                if (arrayList.size() == list.size()) {

                                                                    mIssueGoods.setGoodsPic(arrayList);
                                                                    getGoodsItemSucess(mIssueGoods);

                                                                }

                                                            }
                                                        });
                                                    }
                                                } else {

                                                   mIssueGoods.setGoodsPic(null);
                                                   getGoodsItemSucess(mIssueGoods);
                                               }


                                            } else getGoodsItemFaild(e.getMessage());

                                        }


                                    });

                                }
                                else getGoodsItemFaild(e.getMessage());
                            }
                        });
                    } else getGoodsItemFaild("无该商品！");
                } else getGoodsItemFaild(e.getMessage());
            }
        });

    }
    public abstract void getGoodsItemSucess(IssueGoods issueGoods);
    public abstract void getGoodsItemFaild(String msg);
}
