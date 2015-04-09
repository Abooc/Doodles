package org.lee.android.test.data;

import com.google.gson.Gson;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.DoodlePackage;
import org.lee.android.doodles.volley.FileUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 测试数据源
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-5.
 */
public class DataGeter {

    /**
     * 获取Doodles列表
     * @return
     */
    public static Doodle[] getDoodles(){
        String json = loadJsonData("data/doodles.json");
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(json, Doodle[].class);
        return doodles;
    }

    /**
     * 获取搜索Doodles
     * @return
     */
    public static DoodlePackage getSearchDoodles(){
        String json = loadJsonData("data/doodles-search.json");
        Gson gson = new Gson();
        return gson.fromJson(json, DoodlePackage.class);
    }

    /**
     * 测试数据
     */
    private static String loadJsonData(String filepath) {
        try {
            InputStream inputStream = AppContext.getContext().getAssets().open(filepath);
            return FileUtils.readInStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
