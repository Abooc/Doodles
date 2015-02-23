package org.lee.android.doodles;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ApiClient {

    //    String url = "http://www.woolom.com";
    private String url = "https://www.google.com/doodles/json/{y}/{m}?hl=zh_CN";

    public static final AsyncHttpClient httpClient = new AsyncHttpClient();

    /**
     * 获取Doodles列表
     * @param year
     * @param monthOfYear
     * @param callbacks
     */
    public void requestDoodles(int year, int monthOfYear, AsyncHttpResponseHandler callbacks) {
        url = fixUrl(year, monthOfYear);
        httpClient.get(url, null, callbacks);
    }

    public String fixUrl(int year, int monthOfYear){
        String url = "https://www.google.com/doodles/json/{y}/{m}?hl=zh_CN";
        url = url.replace("{y}", String.valueOf(year));
        url = url.replace("{m}", String.valueOf(monthOfYear));
        return url;
    }
}