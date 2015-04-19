package org.lee.android.doodles.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import org.lee.android.doodles.FragmentLifecycle;
import org.lee.android.doodles.fragment.SearchResultFragment;

/**
 * 承载搜索结果Activity
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-5.
 */
public class SearchActivity extends ActionBarActivity implements FragmentLifecycle {

    /**
     *
     * @param context
     * @param q 要搜索的关键词
     */
    public static void launch(Context context, String q) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("q", q);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            String q = intent.getStringExtra("q");
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, SearchResultFragment.newInstance(q))
                    .commit();
        }

    }

    @Override
    public void onFragmentStart(Fragment fragment) {

    }

    @Override
    public void onFragmentDestroy(Fragment fragment) {

    }

}
