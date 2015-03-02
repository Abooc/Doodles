package org.lee.android.doodles;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.lee.android.doodles.volley.VolleyLoader;
import org.lee.android.util.Toast;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 14-09-05.
 */
public class AppApplication extends Application {

    private static final String PROPERTY_ID = "UA-59448211-1";
//    private static final String GOOGLE_PLAY = "http://play.google.com/store/apps/details?id=";
//    private static final String GOOGLE_PLAY = "https://play.google.com/apps/testing/";
    private static final String GOOGLE_PLAY = "https://play.google.com/apps/testing/org.lee.android.app.adworld";

    private static final String GOOGLE_MARKET = "market://details?id=";
    @Override
    public void onCreate() {
        Toast.init(this);
        AppContext.getInstance().setContext(this);
        VolleyLoader.initialize(this);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 打开应用在Google Play的页面
     * @param context
     */
    public static void openGooglePlay(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        final String appPackageName = context.getPackageName();
        Uri GOOGLE_PLAY_URI = Uri.parse(
                GOOGLE_MARKET
                        + appPackageName
        );
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, GOOGLE_PLAY_URI));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, GOOGLE_PLAY_URI));
        }
    }


    private static String title = "分享Google涂鸦应用";
    private static String content = "分享个应用，可以看海量美女、帅哥、汽车，还有‘宅男女神’的专属图片等图片！"
            + "\n点击看一下\n" + GOOGLE_PLAY;


    public static void share(Activity activity){
        share(activity, title, "", content, "");
    }

    /**
     * 分享
     * @param activity
     * @param title
     * @param header
     * @param content
     * @param footer
     */
    public static void share(Activity activity, String title, String header,
                      String content, String footer) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_TEXT, content + "\n" + header);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, title));
    }
}
