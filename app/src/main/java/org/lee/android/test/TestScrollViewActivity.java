package org.lee.android.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.common.view.SlidingTabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.lee.android.doodles.CustomFragmentPagerAdapter;
import org.lee.android.doodles.DefaultBuild;
import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.YearAboutFragment;
import org.lee.android.doodles.fragment.YearsFragment;
import org.lee.android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-4-24.
 */
public class TestScrollViewActivity extends ActionBarActivity {

    private CustomScrollView mScrollView;
    private View mContentView;
    private View mCardView;
    private View mHeaderView;
    private View mFooterView;
    private TextView mXYText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_scrollview);

        forceOverflowMenuIcon(this);

        init();

        addPager();
    }

    /**
     * Helper function to force the Activity to show the three-dot overflow icon in its ActionBar.
     * @param activity Activity whose overflow icon will be forced.
     */
    private static void forceOverflowMenuIcon(Activity activity) {
        try {
            ViewConfiguration config = ViewConfiguration.get(activity);
            // Note: this field doesn't exist in 2.3, so those users will need to tap the physical menu button,
            // unless we figure out another solution.
            // This field also doesn't exist in 4.4, where the overflow icon is always shown:
            // https://android.googlesource.com/platform/frameworks/base.git/+/ea04f3cfc6e245fb415fd352ed0048cd940a46fe%5E!/
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // multiple exceptions may be thrown above, but it's not super critical if it fails.
        }
    }


    private void init() {
        mContentView = findViewById(R.id.Content);
        mCardView = findViewById(R.id.CardView);
        mXYText = (TextView) findViewById(R.id.XYTextView);
        mScrollView = (CustomScrollView) findViewById(R.id.ScrollView);
        mHeaderView = findViewById(R.id.HeaderView);
        mFooterView = findViewById(R.id.FooterView);
        mScrollView.setOnScrollListener(new CustomScrollView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(CustomScrollView recyclerView, int newState) {
                if (newState == CustomScrollView.SCROLL_STATE_SETTLING) {
                    mNewState = true;
//                    android.util.Log.d("TAG", "dx:" + dx + ", dy:" + dy);
                } else {
                    mNewState = false;
                }
            }

            @Override
            public void onScrolled(CustomScrollView recyclerView, int dx, int dy) {
//                if(mNewState){
//                    android.util.Log.d("TAG", "dx:" + dx + ", dy:" + dy);
//                }
//                    android.util.Log.d("TAG", "dx:" + dx + ", dy:" + dy);

                int bottom = mHeaderView.getBottom();
                int top = mFooterView.getTop();

                mXYText.setText("dx:" + dx + ", dy:" + dy + ", top:" + top + ", bottom:" + bottom);

                if (dy > 900) {
                    mCardView.setTranslationY(dy - 900);
                }
            }
        });
    }

    private void addPager(){

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.SlidingTabs);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }

            @Override
            public int getDividerColor(int position) {
                return getResources().getColor(R.color.TRANSLUCENT);
            }
        });

        YearsFragment.Year year = DefaultBuild.defaultYear();
        Bundle args = new Bundle();
        args.putString("year", new Gson().toJson(year));

        ViewPager mViewPager = (ViewPager) findViewById(R.id.ViewPager);
        ArrayList<CustomFragmentPagerAdapter.TabInfo> tabInfos = new ArrayList<>();
        CustomFragmentPagerAdapter.TabInfo yearTabInfo = new CustomFragmentPagerAdapter.TabInfo("Google", YearAboutFragment.class, args);
        tabInfos.add(0, yearTabInfo);
        CustomFragmentPagerAdapter.TabInfo yearTabInfo2 = new CustomFragmentPagerAdapter.TabInfo("Wiki", YearAboutFragment.class, args);
        tabInfos.add(yearTabInfo2);
        CustomFragmentPagerAdapter.TabInfo yearTabInfo3 = new CustomFragmentPagerAdapter.TabInfo("图片", YearAboutFragment.class, args);
        tabInfos.add(yearTabInfo3);
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(
                getSupportFragmentManager(), this, tabInfos);
        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    boolean mNewState ;

    int bottom;
    int top;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && bottom == 0) {
            bottom = mScrollView.getHeight();
            top = mCardView.getTop();
            Log.anchor("bottom:" + bottom + ", top:" + top);
            ViewGroup.LayoutParams params = mFooterView.getLayoutParams();
            params.height = bottom;
            mFooterView.setLayoutParams(params);
        }
        return super.dispatchTouchEvent(ev);
    }

}
