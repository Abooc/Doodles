package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.lee.android.doodles.AppApplication;
import org.lee.android.doodles.AppFunction;
import org.lee.android.doodles.FragmentHandlerAdapter.TabInfo;
import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.properties.SettingsActivity;
import org.lee.android.util.Toast;

import java.util.ArrayList;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener,
        DrawerLayout.DrawerListener, AdapterView.OnItemClickListener {

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private ArrayList<TabInfo> mTabInfoList;

    private String[] mNames = {
//            "A",
//            "B",
            "最新涂鸦",
//            "年份",
            "涂鸦存档"
//                , "搜索更多涂鸦"
    };

    public NavigationDrawerFragment() {
        mTabInfoList = createTabInfos();
    }

    private ArrayList<TabInfo> createTabInfos() {
        int index = 0;
        ArrayList<TabInfo> tabInfos = new ArrayList<TabInfo>();

        Bundle args = new Bundle();
        args.putBoolean("hasYearPage", true);
        args.putString("year", new Gson().toJson(DoodleArchivePagerFragment.mYear));
//        tabInfos.add(new TabInfo(DoodlesListFragment.class, mNames[index++], args));
//        tabInfos.add(new TabInfo(DoodlesListFragment.class, mNames[index++], args));
        tabInfos.add(new TabInfo(TodayFragment.class, mNames[index++], null));
//        tabInfos.add(new TabInfo(YearsFragment.class, mNames[index++], null));
        tabInfos.add(new TabInfo(DoodleArchivePagerFragment.class, mNames[index++], args));
//        tabInfos.add(new TabInfo(SearchFragment.class, mNames[index++], null));
        return tabInfos;
    }

    private DrawerMenuItem[] getMenus(String[] names, int[] iconIds) {
        DrawerMenuItem[] menuItems = {
                new DrawerMenuItem(names[0], iconIds[0]),
                new DrawerMenuItem(names[1], iconIds[1])
//                ,
//                new DrawerMenuItem(names[2], iconIds[2])
        };
        return menuItems;
    }

    public ArrayList<TabInfo> getTabInfos() {
        return mTabInfoList;
    }

    private ActionBar mActionBar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActionBar = ((ActionBarActivity) activity).getSupportActionBar();
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        DrawerMenuItem[] menuItems = getMenus(mNames,
                new int[]{R.drawable.ic_menu_sort_by_size,
//                        R.drawable.ic_menu_today,
                        R.drawable.ic_menu_find});
        mDrawerMenuAdapter = new DrawerMenuAdapter(
                getActivity(), 0, menuItems);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) container.findViewById(R.id.ListView);
        mDrawerListView.setOnItemClickListener(this);
        mDrawerListView.setAdapter(mDrawerMenuAdapter);
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return container;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.Commit).setOnClickListener(this);
        view.findViewById(R.id.AboutDoodles).setOnClickListener(this);
        view.findViewById(R.id.RemoveAd).setOnClickListener(this);
        view.findViewById(R.id.Settings).setOnClickListener(this);
        view.findViewById(R.id.Evaluate).setOnClickListener(this);
        view.findViewById(R.id.Share).setOnClickListener(this);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    private boolean actionbarToggle;

    public void toggle() {
        if (actionbarToggle) {
            mDrawerToggle.onDrawerClosed(mFragmentContainerView);
            actionbarToggle = false;
        } else {
            mDrawerToggle.onDrawerOpened(mFragmentContainerView);
            actionbarToggle = true;
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void toggleDrawer() {
        if (isDrawerOpen()) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        } else {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUpDrawerMenu(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                NavigationDrawerFragment.this.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                NavigationDrawerFragment.this.onDrawerOpened(drawerView);
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        AppFunction.hideInputMethod(getActivity(), mDrawerLayout);

        if (!isAdded()) {
            return;
        }

        if (!mUserLearnedDrawer) {
            // The user manually opened the drawer; store this flag to prevent auto-showing
            // the navigation drawer automatically in the future.
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        }

        getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (!isAdded()) {
            return;
        }

        getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    public void setMenuSelection(int position) {
        mDrawerListView.performItemClick(null, position, 0);
    }

    /**
     * 侧滑菜单ListView每一项点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * 侧滑菜单打开或关闭时，
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            menu.setGroupVisible(R.id.Menu, false);
        } else {
            menu.setGroupVisible(R.id.Menu, true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * ActionBar菜单事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return mActionBar;
    }

    /**
     * 侧滑菜单部分按钮事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        switch (v.getId()) {
            case R.id.Commit:
                Toast.show("贡献我的涂鸦作品...");
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:allnet@live.cn"));
                data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
                data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(data);
                return;
            case R.id.AboutDoodles:
//                AboutDoodlesActivity.launch(getActivity(), "https://www.google.com/doodles/about");
                String AboutDoodles = getString(R.string.AboutDoodles);
                WebViewActivity.launch(getActivity(), "https://www.google.com/doodles/about", AboutDoodles);
                return;
            case R.id.RemoveAd:
                Toast.show("RemoveAd...");
                return;
            case R.id.Settings:
//                AboutUsActivity.launch(getActivity());
                SettingsActivity.launch(getActivity());
                return;
            case R.id.Evaluate:
                AppApplication.openGooglePlay(getActivity());
                return;
            case R.id.Share:
                AppApplication.share(getActivity());
                return;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    private static class DrawerMenuItem {
        final String name;
        final int iconId;

        public DrawerMenuItem(String name, int iconId) {
            this.name = name;
            this.iconId = iconId;
        }
    }

    /**
     * 侧滑菜单ListView Adapter
     */
    private DrawerMenuAdapter<DrawerMenuItem> mDrawerMenuAdapter;

    private class DrawerMenuAdapter<T> extends ArrayAdapter {

        public DrawerMenuAdapter(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_menu_item, null);
            TextView titleText = (TextView) convertView.findViewById(R.id.Title);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.ImageView);
            DrawerMenuItem menuItem = (DrawerMenuItem) getItem(position);
            titleText.setText(menuItem.name);
            iconView.setImageResource(menuItem.iconId);
            return convertView;
        }
    }
}
