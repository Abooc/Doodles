package org.lee.android.doodles.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.lee.android.doodles.R;
import org.lee.android.doodles.FragmentLifecycle;
import org.lee.android.doodles.fragment.SearchFragment;
import org.lee.android.util.Log;

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
        getActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_commit_search_api_mtrl_alpha);

        Intent intent = getIntent();
        if(intent != null){
            String q = intent.getStringExtra("q");
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, SearchFragment.newInstance(q))
                    .commit();
        }

    }

    private boolean DetailsResume;

    @Override
    public void onFragmentStart(Fragment fragment) {
        DetailsResume = true;
        Log.anchor();

    }

    @Override
    public void onFragmentDestroy(Fragment fragment) {
        DetailsResume = false;
        Log.anchor();

    }

    @Override
    public void onBackPressed() {
        if(DetailsResume){
            Log.anchor();
            getSupportFragmentManager().popBackStackImmediate();
            return;
        }
        super.onBackPressed();
    }
}
