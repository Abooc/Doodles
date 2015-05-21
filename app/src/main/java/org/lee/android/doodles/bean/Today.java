package org.lee.android.doodles.bean;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-6.
 */
public class Today {

    public int year;
    public int monthOfYear;
    public int dayOfMonth;

    public Today(int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    @Override
    public String toString() {
        return "year:" + year + ", monthOfYear:" + monthOfYear + ", dayOfMonth:" + dayOfMonth;
    }

}
