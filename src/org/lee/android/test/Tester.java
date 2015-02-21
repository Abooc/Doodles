package org.lee.android.test;

import com.google.gson.Gson;

import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.Translations;
import org.lee.android.doodles.volley.FileUtils;
import org.lee.android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-21.
 */
public class Tester{

    public static void main(String[] args){
        parse();
//        JsonObj();
    }

    public static void JsonObj(){
        String json = "{\"en\":{\"hover_text\":\"Happy Tet 2015\",\"query\":\"Tet\"},\"fr\":{},\"vi\":{\"hover_text\":\"Chúc mừng năm mới 2015\",\"query\":\"Tết\"},\"zh-TW\":{}}";


        Translations translations = new Translations();
        translations.parse(json);
        String text = translations.getQuery("en").hover_text;
        Log.anchor(text);

//        try {
//            JSONObject jsonObject = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }


    public static void parse(){

        String json = null;
        try {

            String path = "/Users/dayu/Git/Abooc/Google Doodles/assets/data/doodles.json";
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            json = FileUtils.readInStream(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(json, Doodle[].class);
        print(doodles.length + "\n" +
                "========================");

        for (Doodle doodle : doodles){
            String src = doodle.name + "\n"
                    + doodle.query + "\n"
                    + doodle.title + "\n"
                    + doodle.getDate() + "\n"
                    + doodle.getTranslations().size() + "\n"
                    + doodle.url + "\n"
                    ;
            print(src);
        }

    }



    public static void print(Object s){
        System.out.println(
                s == null ?
                s :
                s.toString());
    }
}
