package org.lee.android.doodles.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.lee.android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 翻译串
 */
public class Translations {

    private HashMap<String, Query> map = new HashMap<String, Query>();

    public int size(){
        return map.size();
    }

    public Query getItem(int position){
        if(map.size() == 0)
            throw new NullPointerException("");
        return map.get(map.keySet().iterator().next());

    }

    public Query getQuery(String key){
        return map.get(key);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public void parse(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(e.getMessage());
            return;
        }
        Iterator<String> iterator = jsonObject.keys();
        Gson gson = new Gson();
        try {
            while (iterator.hasNext()) {
                String key = iterator.next();
                String queryJson = jsonObject.getString(key);
                Query query = gson.fromJson(queryJson, Query.class);
                map.put(key, query);
//                Log.d(key + ":" + query.hover_text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static class Query {
        public String query;
        public String hover_text;

    }
}