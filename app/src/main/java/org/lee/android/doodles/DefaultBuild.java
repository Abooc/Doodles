package org.lee.android.doodles;

import org.lee.android.doodles.fragment.YearsFragment;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-13.
 */
public class DefaultBuild {

    /**
     * 默认的年份
     * @return
     */
    public static YearsFragment.Year defaultYear(){
        return new YearsFragment.Year(2015, "i_2002");
    }
}
