package org.lee.android.doodles.bean;

import com.google.gson.JsonElement;

/**
 *
 */
public class DoodlePackage {
    public int results_number;
    public int next_page_start_index;
    public JsonElement featured_doodles;
    public Doodle[] doodles;

}