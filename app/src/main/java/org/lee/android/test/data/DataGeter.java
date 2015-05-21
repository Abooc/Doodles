package org.lee.android.test.data;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.lee.android.doodles.AppContext;
import org.lee.android.doodles.DefaultBuild;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.DoodlePackage;
import org.lee.android.doodles.bean.Today;
import org.lee.android.doodles.fragment.DoodleRecyclerAdapter.Card;
import org.lee.android.doodles.volley.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * 测试数据源
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-5.
 */
public class DataGeter {

    /**
     * 列表中广告显示的间隔器
     * @param position
     * @return
     */
    public static boolean adViewFilter(int position) {
        int size = DefaultBuild.ADVIEW_DIVIDER;
        if (position % size == (size - 1)) return true;
        return false;
    }

    public static Card[] toCards(Doodle[] doodles) {
        Card[] cards = new Card[doodles.length];
        for (int i = 0; i < doodles.length; i++) {
            Doodle doodle = doodles[i];
            doodle.hires_url = fixUrl(doodle.hires_url);
            cards[i] = new Card(Card.TYPE_VIEW_DOODLE, 0, doodle);
        }
        return cards;
    }

    static String fixUrl(String url){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        if(url.startsWith("//")){
            url = "http:" + url;
        }else if(url.startsWith("www.")){
            url = "http://" + url;
        }
        return url;
    }

    /**
     * 最新列表
     *
     * @param cards
     * @return
     */
    public static Card[] getTodayListCards(Card[] cards, Today today) {
        ArrayList<Card> cardList = new ArrayList<>();
        int adSize = 0;
        for (int i = 0; i < cards.length; i++) {
            if (adViewFilter(i)) {
                Card card = new Card(Card.TYPE_VIEW_ADVIEW, 0, null);
                cardList.add(card);
                adSize++;
            } else {
                cardList.add(cards[i - adSize]);
            }
        }
        Card card = new Card(Card.TYPE_VIEW_HEADER, 0, null);
        cardList.add(0, card);

        card = new Card(Card.TYPE_VIEW_FOOTER, 0, today);
        cardList.add(card);
        Card[] newCards = new Card[cardList.size()];
        return cardList.toArray(newCards);
    }

    /**
     * 月份列表
     *
     * @param cards
     * @return
     */
    public static Card[] getListCards(Card[] cards, Today today) {
        ArrayList<Card> cardList = new ArrayList<>();
        int adSize = 0;
        for (int i = 0; i < cards.length; i++) {
            if (adViewFilter(i)) {
                Card card = new Card(Card.TYPE_VIEW_ADVIEW, 0, null);
                cardList.add(card);
                adSize++;
            } else {
                cardList.add(cards[i - adSize]);
            }
        }
        Card card = new Card(Card.TYPE_VIEW_FOOTER, 0, today);
        cardList.add(card);
        Card[] newCards = new Card[cardList.size()];
        return cardList.toArray(newCards);
    }

    /**
     * 获取Doodles列表
     *
     * @return
     */
    public static Doodle[] getDoodles() {
        String json = loadJsonData("data/doodles.json");
        Gson gson = new Gson();
        Doodle[] doodles = gson.fromJson(json, Doodle[].class);
        return doodles;
    }

    /**
     * 获取搜索Doodles
     *
     * @return
     */
    public static DoodlePackage getSearchDoodles() {
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
