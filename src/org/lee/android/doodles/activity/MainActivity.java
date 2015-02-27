package org.lee.android.doodles.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import org.lee.android.doodles.DoubleClickBackExit;
import org.lee.android.doodles.FragmentHandlerAdapter;
import org.lee.android.doodles.FragmentHandlerAdapter.TabInfo;
import org.lee.android.doodles.R;
import org.lee.android.doodles.fragment.NavigationDrawerFragment;
import org.lee.android.doodles.fragment.PagerFragment;
import org.lee.android.doodles.fragment.SearchFragment;
import org.lee.android.doodles.fragment.TodayFragment;
import org.lee.android.util.Toast;

import java.util.ArrayList;


/**
 * 应用主Activity
 * <p/>
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-2-22.
 */
public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private FragmentHandlerAdapter mFragmentHandler;
    private FragmentManager mFragmentManager;
    /**
     * x
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentHandler = new FragmentHandlerAdapter(mFragmentManager, this, getTabInfos());
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUpDrawerMenu(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(mTitle);
    }

    /**
     * 侧滑菜单
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = mFragmentHandler.getItem(position);
        TabInfo tabInfo = mFragmentHandler.getTabInfo(position);
        mFragmentHandler.show(fragment, tabInfo.title);
    }

    private ArrayList<TabInfo> getTabInfos() {
        int index = 0;
        String[] names = {"今天", "涂鸦存档", "搜索更多涂鸦"};
        ArrayList<TabInfo> tabInfos = new ArrayList<TabInfo>();
        tabInfos.add(new TabInfo(TodayFragment.class, names[index++], null));
        tabInfos.add(new TabInfo(PagerFragment.class, names[index++], null));
        tabInfos.add(new TabInfo(SearchFragment.class, names[index++], null));
        return tabInfos;
    }


    /**
     * 侧滑菜单选中指定Fragment
     *
     * @param number
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getResources().getString(R.string.title_section1);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.activity_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (DoubleClickBackExit.onBackPressed()) {
            super.onBackPressed();
        } else {
            Toast.show("双击返回键退出");
        }
    }
}


//
//    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
//        private Fragment mFragment;
//        private final Activity mActivity;
//        private final String mTag;
//        private final Class<T> mClass;
//
//        /** Constructor used each time a new tab is created.
//         * @param activity  The host Activity, used to instantiate the fragment
//         * @param tag  The identifier tag for the fragment
//         * @param clz  The fragment's Class, used to instantiate the fragment
//         */
//        public TabListener(Activity activity, String tag, Class<T> clz) {
//            mActivity = activity;
//            mTag = tag;
//            mClass = clz;
//        }
//
//        @Override
//        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//            // Check if the fragment is already initialized
//            if (mFragment == null) {
//                // If not, instantiate and add it to the activity
//                mFragment = Fragment.instantiate(mActivity, mClass.getName());
//                ft.add(android.R.id.content, mFragment, mTag);
//            } else {
//                // If it exists, simply attach it in order to show it
//                ft.attach(mFragment);
//            }
//
//        }
//
//        @Override
//        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            if (mFragment != null) {
//                // Detach the fragment, because another one is being attached
//                ft.detach(mFragment);
//            }
//        }
//
//        @Override
//        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//        }
//    }


//
//    private void addTab(){
//        ActionBar actionBar = getActionBar();
//        ActionBar.Tab tab = actionBar.newTab()
//                .setText("Tab")
//                .setTabListener(new TabListener<NavigationDrawerFragment>(
//                        this, "Tab", NavigationDrawerFragment.class));
//        actionBar.addTab(tab);
//
//        tab = actionBar.newTab()
//                .setText("album")
//                .setTabListener(new TabListener<NavigationDrawerFragment>(
//                        this, "album", NavigationDrawerFragment.class));
//        actionBar.addTab(tab);
//    }