package org.lee.android.doodles.adview;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-20.
 */
public class AdManager {

    public static String errorMessage(int errorCode) {
        String error = null;
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                error = "ERROR_CODE_INTERNAL_ERROR";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                error = "ERROR_CODE_INVALID_REQUEST";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                error = "ERROR_CODE_NETWORK_ERROR";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                error = "ERROR_CODE_NO_FILL";
                break;
        }
        return error;
    }
}
