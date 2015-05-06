package org.lee.android.doodles.bean;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-6.
 */
public class Month {

    public int year;
    public int month;

    public Month(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public String toString() {
        return "year:" + year + ", month:" + month;
    }

}
