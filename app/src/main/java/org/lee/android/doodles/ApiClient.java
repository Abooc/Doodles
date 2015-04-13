package org.lee.android.doodles;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.lee.android.util.Log;

public class ApiClient {

    //    String url = "http://www.woolom.com";
    public static String GOOGLE_DOODLES_ROOT = "https://www.google.com/doodles/";
    public static String GOOGLE_SEARCH = "https://www.google.com/search?q=d%";
    public static String GOOGLE_DOODLES_SEARCH = "https://www.google.com/doodles/search?q=d%s=d%";
    private String ROOT_url = "https://www.google.com/doodles/json/d%/d%?hl=zh_CN";

    public static final AsyncHttpClient httpClient = new AsyncHttpClient(true, 80, 443);

    /**
     * 获取Doodles列表
     * @param year
     * @param monthOfYear
     * @param callbacks
     */
    public void requestDoodles(int year, int monthOfYear, AsyncHttpResponseHandler callbacks) {
        httpClient.setTimeout(5*1000);
//        ROOT_url = fixUrl(year, monthOfYear);
        String url = String.format(ROOT_url, year, monthOfYear);
        Log.anchor(url);
        httpClient.get(url, null, callbacks);
    }

    public void searchDoodles(String q, int start, AsyncHttpResponseHandler callbacks){
        httpClient.setTimeout(5*1000);
//        String url = String.format(Locale.CHINA, GOOGLE_DOODLES_SEARCH, q, start+"");
        String url = GOOGLE_DOODLES_SEARCH.replace("q=d%", "q="+q);
        url = url.replace("s=d%", "s="+start);
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