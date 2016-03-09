package com.app.rex.xposedexamples.domain;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zuoyuxiaofu on 2016/3/9.
 * 商品
 */
public class Item {


    public long itemId;

    public String avType;

    public String picUrl;

    public String title;

    public double price;

    public String tips;

    public  String jumpUrl;

    public Item(JSONObject jsonObject){
        try {
            itemId = jsonObject.getLong("itemId");
            avType = jsonObject.getString("avType");
            picUrl = jsonObject.getString("picUrl");
            title = jsonObject.getString("title");
            price = jsonObject.getDouble("price");
            tips = jsonObject.getString("tips");
            jumpUrl = jsonObject.getString("jumpUrl");

            if (!picUrl.startsWith("http")){
                picUrl = "http:"+picUrl;
            }

            if (!jumpUrl.startsWith("http")){
                jumpUrl = "http:"+jumpUrl;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Item(long itemId, String avType, String picUrl, String title, double price, String tips, String jumpUrl) {
        this.itemId = itemId;
        this.avType = avType;
        this.picUrl = picUrl;
        this.title = title;
        this.price = price;
        this.tips = tips;
        this.jumpUrl = jumpUrl;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", avType='" + avType + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", tips='" + tips + '\'' +
                ", jumpUrl='" + jumpUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Item){
            return ((Item)o).itemId==this.itemId;
        }

        return super.equals(o);
    }

    //    "itemId": "13928831579",
//            "avType": "SellerRecommend",
//            "picUrl": "//img.alicdn.com/bao/uploaded/i4/TB1JP8LLVXXXXcyXpXXXXXXXXXX_!!0-item_pic.jpg",
//            "title": "快卸妆 无残留 不油",
//            "price": "39.9",
//            "tips": "5449人已买",
//            "jumpUrl": "//a.m.taobao.com/i13928831579.htm?main_itemid=14022921422&go_item_id=13928831579&scm=1007.10766.25345.1002003000000001&pvid=a96ff912-4f35-462f-b713-712e41434a41&track_params=%7B%7D"

}
