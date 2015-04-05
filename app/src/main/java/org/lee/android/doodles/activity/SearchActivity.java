package org.lee.android.doodles.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.lee.android.doodles.R;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-5.
 */
public class SearchActivity extends FragmentActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

}
