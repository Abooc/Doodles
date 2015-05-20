package org.lee.android.doodles.adview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import org.lee.android.doodles.AppApplication;
import org.lee.android.doodles.R;


/**
 * 设置广告属性
 *
 * @author ruiyuLee
 */
public class PropertiesActivity extends ActionBarActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PropertiesActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(AppApplication.getInstance().getCurrentTheme());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adview_properties);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
