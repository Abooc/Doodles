package org.lee.android.doodles.activity;

import android.battleground.common.DoubleClickBackExit;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.common.view.SlidingTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.lee.android.doodles.AppFunction;
import org.lee.android.doodles.FragmentHandlerAdapter;
import org.lee.android.doodles.FragmentHandlerAdapter.TabInfo;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.fragment.DoodleArchivePagerFragment;
import org.lee.android.doodles.fragment.DoodleDetailsFragment;
import org.lee.android.doodles.fragment.FragmentRunningListener;
import org.lee.android.doodles.fragment.HidingScrollListener;
import org.lee.android.doodles.fragment.NavigationDrawerFragment;
import org.lee.android.doodles.fragment.SearchFragment;
import org.lee.android.doodles.fragment.SearchKeywordsFragment;
import org.lee.android.doodles.fragment.TodayFragment;
import org.lee.android.doodles.fragment.YearsFragment;
import org.lee.android.doodles.widget.SearchBar;
import org.lee.android.util.Log;
import org.lee.android.util.Toast;


/**
 * 应用主Activity
 * <p/>
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-22.
 */
public class MainActivity extends LoggerActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        YearsFragment.OnYearChangedListener, FragmentRunningListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private FragmentHandlerAdapter mFragmentHandler;
    private FragmentManager mFragmentManager;
    private CharSequence mTitle;
    private LinearLayout mToolbarContainer;
    private int mToolbarHeight;
    private Drawable mToolbarContainerDrawable;
    private SearchBar mSearchBar;
    private SlidingTabLayout mSlidingTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
//        initSearchBar();

        mTitle = getTitle();
        initDrawerFragment();

    }

    private void initToolbar() {
        mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);
        mToolbarContainerDrawable = mToolbarContainer.getBackground();
        mSlidingTabLayout = (SlidingTabLayout) mToolbarContainer.findViewById(R.id.SlidingTabs);
//        mSlidingTabLayout.setCustomTabView(R.layout.slid_strip_item, R.id.Text);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }

            @Override
            public int getDividerColor(int position) {
                return Color.TRANSPARENT;
            }
        });
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbarHeight = Utils.getToolbarHeight(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initSearchBar() {
//        mSearchBar = (SearchBar) findViewById(R.id.SearchBar);
        mSearchBar.setFragmentManager(mFragmentManager);
        mSearchBar.setOnSearchEventListener(new SearchBar.OnSearchEventListener() {
            @Override
            public void onSearch(String q) {
                hideToolbar();

                Toast.show("搜索..." + q);
                AppFunction.hideKeyboard(MainActivity.this);
                FragmentTransaction transaction = mFragmentManager
                        .beginTransaction();
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(android.R.id.tabcontent,
                        SearchFragment.newInstance(q));
                transaction.addToBackStack("q").commit();
            }
        });
        mSearchBar.setOnCancleClickListener(new SearchBar.OnCancleClickListener() {
            @Override
            public void onCancel() {
                getWindow().getDecorView().requestFocus();
                mFragmentManager.popBackStack();
            }
        });

        mSearchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    FragmentTransaction transaction = mFragmentManager
                            .beginTransaction();
//            transaction
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.add(android.R.id.content,
                            SearchKeywordsFragment.newInstance("汽车"));
                    transaction.addToBackStack("keywords").commit();
                }
            }
        });
    }

    private DrawerLayout drawerLayout;
    private void initDrawerFragment() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mFragmentHandler = new FragmentHandlerAdapter(mFragmentManager, this,
                mNavigationDrawerFragment.getTabInfos());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment.setUpDrawerMenu(
                R.id.navigation_drawer, drawerLayout);
        mNavigationDrawerFragment.setMenuSelection(0);
    }

    public void showToolbar() {
        mToolbarContainer.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(2)).start();
    }

    public void hideToolbar() {
        mToolbarContainer.animate().translationY(-mToolbarHeight)
                .setInterpolator(new DecelerateInterpolator(2)).start();
    }

    /**
     * 点击非输入框区域收起软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev == null) return false;
        boolean block = false;
        View v = getCurrentFocus();
        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                block = AppFunction.hideKeyboard(this);
        }
        if (block) {
            getWindow().getDecorView().requestFocus();
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public HidingScrollListener getHidingScrollListener() {
        return new HidingScrollListener(this) {


            @Override
            public void onMoved(int distance, int dx, int dy) {
//                mToolbarContainerDrawable.setAlpha(255 - Math.min(255, distance * 2));
                mToolbarContainer.setTranslationY(-distance);
                if (mMoveView != null) {
                    mMoveView.setTranslationY(-distance);
                }
            }

            @Override
            public void onShow() {
                Log.anchor();
//                mToolbarContainerDrawable.setAlpha(0);
                showToolbar();
            }

            @Override
            public void onHide() {
                Log.anchor();
//                mToolbarContainerDrawable.setAlpha(254);
                hideToolbar();
            }

        };
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mNavigationDrawerFragment.getDrawerToggle();
    }


    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    public void restoreActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.activity_main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * ActionBar菜单事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
        Log.anchor();
//        if() return true;
        switch (item.getItemId()) {
            case R.id.Settings:
                return true;
            case R.id.RemoveAd:
                return true;
            case R.id.AboutDoodles:
                return true;
            case R.id.Share:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int section;

    /**
     * 侧滑菜单ListView
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        showToolbar();
        if(position == 1){
            mSlidingTabLayout.setVisibility(View.VISIBLE);
        }

        section = position;
        TabInfo tabInfo = mFragmentHandler.getTabInfo(position);
        Fragment fragment = mFragmentHandler.getItem(position);
        mFragmentHandler.run(fragment, tabInfo.title);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private View mMoveView;

    @Override
    public void onResume(Fragment fragment) {
        String title = fragment.getTag();
        Log.anchor(title + ", " + fragment.getClass().getSimpleName());
        restoreActionBar(title);

        if (fragment instanceof DoodleArchivePagerFragment) {
            DoodleArchivePagerFragment pagerFragment = (DoodleArchivePagerFragment) fragment;
            mSlidingTabLayout.setViewPager(pagerFragment.getPager());
            return;
        }
        mMoveView = null;
        if (fragment instanceof TodayFragment) {
            mSlidingTabLayout.setViewPager(null);
            mSlidingTabLayout.setVisibility(View.GONE);

            return;
        }

        if (fragment instanceof DoodleDetailsFragment) {
            showToolbar();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mNavigationDrawerFragment.setHasOptionsMenu(false);
            return;
        }


    }

    @Override
    public void onPause(Fragment fragment) {
        Log.anchor(fragment.getClass().getSimpleName());
        if (fragment instanceof DoodleDetailsFragment) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mNavigationDrawerFragment.setHasOptionsMenu(true);
            return;
        }
    }

    private YearsFragment.Year mYear;

    /**
     * 年份切换事件
     */
    @Override
    public void onYearChanged(YearsFragment.Year newYear) {
        mYear = newYear;
        mNavigationDrawerFragment.setMenuSelection(2);
        TabInfo tabInfo = mFragmentHandler.getTabInfo(2);
        mTitle = tabInfo.title;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen()) {
            mNavigationDrawerFragment.toggleDrawer();
            return;
        }
        View view = getWindow().getDecorView();
        if (AppFunction.hideInputMethod(this, view)) {
            view.requestFocus();
            Log.anchor();
            return;
        }
        if (section == 0) {
            if (DoubleClickBackExit.onBackPressed()) {
                finish();
            } else {
                Toast.show("双击返回键退出");
            }
        } else {
            super.onBackPressed();
        }

        super.onBackPressed();

    }

}
