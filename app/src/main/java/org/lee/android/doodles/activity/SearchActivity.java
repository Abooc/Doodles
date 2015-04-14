package org.lee.android.doodles.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import org.lee.android.doodles.FragmentLifecycle;
import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.SearchFragment;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-5.
 */
public class SearchActivity extends FragmentActivity implements FragmentLifecycle {

    public static void launch(Context context, String q) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("q", q);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_commit_search_api_mtrl_alpha);

        Intent intent = getIntent();
        if (intent != null) {
            String q = intent.getStringExtra("q");
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, SearchFragment.newInstance(q))
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (super.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onFragmentStart(Fragment fragment) {

    }

    @Override
    public void onFragmentDestroy(Fragment fragment) {

    }

}
