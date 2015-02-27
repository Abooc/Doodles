package org.lee.android.doodles;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.lee.android.util.Log;

public class ApiClient {

    //    String url = "http://www.woolom.com";
    public static String GOOGLE_DOODLES_ROOT = "https://www.google.com/doodles/";
    private String url = "https://www.google.com/doodles/json/{y}/{m}?hl=zh_CN";

    public static final AsyncHttpClient httpClient = new AsyncHttpClient(true, 80, 443);

    /**
     * 获取Doodles列表
     * @param year
     * @param monthOfYear
     * @param callbacks
     */
    public void requestDoodles(int year, int monthOfYear, AsyncHttpResponseHandler callbacks) {
        httpClient.setTimeout(5*1000);
        url = fixUrl(year, monthOfYear);
        Log.anchor(url);
        httpClient.get(url, null, callbacks);
    }

    public String fixUrl(int year, int monthOfYear){
        String url = "https://www.google.com/doodles/json/{y}/{m}?hl=zh_CN";
        url = url.replace("{y}", String.valueOf(year));
        url = url.replace("{m}", String.valueOf(monthOfYear));
        return url;
    }
}