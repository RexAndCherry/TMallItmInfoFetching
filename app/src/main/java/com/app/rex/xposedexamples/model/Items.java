package com.app.rex.xposedexamples.model;

import android.os.Environment;

import com.app.rex.xposedexamples.Tutorial;
import com.app.rex.xposedexamples.domain.Item;
import com.app.rex.xposedexamples.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by zuoyuxiaofu on 2016/3/9.
 * 所有Item
 */
public class Items {

    private static int currentPage;

    private static ArrayList<Item> mItems;

    static {
        mItems = new ArrayList<>();


    }

    /*加载目前的页数*/
    private static void loadCurrentPage() {

    }


    public static void add(Item item) {
        if (mItems.contains(item)) {
            return;
        }

        mItems.add(item);
    }

    /**
     * 从服务器中传回的json中抽取商品信息。包括推荐和商家推荐。
     *
     * @param jsonString
     */
    public static void addNewsFromJsonString(String jsonString) {

        try {

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject componentDataMap = data.getJSONObject("componentDataMap");

            JSONObject sl_sellerRecommend_2 = componentDataMap.getJSONObject("_SL_SellerRecommend_2");
            JSONObject children = sl_sellerRecommend_2.getJSONObject("children");
            JSONObject sl_sellerRecommend_component_2 = children.getJSONObject("_SL_SellerRecommend_Component_2");
            JSONArray dataList = sl_sellerRecommend_component_2.getJSONArray("dataList");
            addArrayToList(dataList);

            JSONObject sl_seeMore = componentDataMap.getJSONObject("_SL_SeeMore");
            JSONObject children1 = sl_seeMore.getJSONObject("children");
            JSONObject sl_seeMore_component = children1.getJSONObject("_SL_SeeMore_Component");
            JSONArray dataList1 = sl_seeMore_component.getJSONArray("dataList");
            addArrayToList(dataList1);


            Logger.i("new add Items:" + mItems);

        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    /**
     * 从本地文件中获取jsonString
     *
     * @return
     */
    public static String getJsonStringFromLocalFile(String fileName) {

        try {
          /*  File file = new File();
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(file);
            int flag = 0;
            byte[] buffer = new byte[1024];
            StringBuffer stringBuffer = new StringBuffer();
            while ((flag = fileInputStream.read(buffer)) != -1) {
                stringBuffer.append(buffer);

            }*/

            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath() + File.separator+fileName));

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                stringBuffer.append(sCurrentLine);
            }


            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("getJsonStringFromLocalFile Failed!");

            return "";

        }
    }


    /**
     * 加载更多，通过获取比它早的拉取到的文件。如果没有了，则返回false。
     *
     * @return
     */
    public static String loadMore() {

        return "";
    }


    private static void addArrayToList(JSONArray dataList) throws JSONException {
        for (int i = 0; i < dataList.length(); i++) {
            Item item = new Item(dataList.getJSONObject(i));
            if (!mItems.contains(item)) {
                Items.add(item);
            }
        }
    }

    public void remove(Item item) {
        mItems.remove(item);
    }




    public static ArrayList<Item> getItems() {

        if (mItems==null||mItems.size()==0){

            loadCurrentPage();
            addNewsFromJsonString(getJsonStringFromLocalFile(Tutorial.FILE_NAME));
        }
        return mItems;
    }
}
