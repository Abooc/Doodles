package org.lee.android.doodles.bean;

import com.google.gson.JsonElement;

/**
 * 搜索Doodles接口返回结果模型
 */
public class DoodlePackage {
    public int results_number;
    public int next_page_start_index;
    public JsonElement featured_doodles;
    public Doodle[] doodles;

    @Override
    public String toString() {
        return "[results_number:" + results_number + "],\n"
                + "[next_page_start_index:" + next_page_start_index + "],\n"
                + "[featured_doodles:" + featured_doodles + "],\n"
                + "[doodles:" + doodles + "]";
    }
}