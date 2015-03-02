package org.lee.android.doodles.bean;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 *
 */
public class Doodle {
    public String name;
    public String title;
    public String url;
    public String alternate_url;
    public String hires_url;
    public String[] run_date_array;
    public JsonElement translations;
    public String query;

    private Translations translationsCache;

    /**
     * 获取翻译串
     * @return
     */
    public Translations getTranslations(){
        if(translationsCache != null) return translationsCache;
        translationsCache = new Translations();
        String json = new Gson().toJson(translations);
        translationsCache.parse(json);
        return translationsCache;
    }

    /**
     * 获取日期字符串
     * @return 如2015-02-21
     */
    public String getDate() {
        return run_date_array[0]
                + "-" + run_date_array[1]
                + "-" + run_date_array[2]
                ;

    }
}