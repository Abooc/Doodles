package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.common.view.SlidingTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.CustomFragmentPagerAdapter;
import org.lee.android.doodles.DefaultBuild;
import org.lee.android.doodles.LifecycleFragment;
import org.lee.android.doodles.OnBackPressedEnable;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.Translations;
import org.lee.android.doodles.volley.VolleyLoader;
import org.lee.android.util.Log;

import java.util.ArrayList;


/**
 * Doodle详情页面
 */
public class DoodleDetailsFragment extends LifecycleFragment implements OnBackPressedEnable {


    private static final String ARG_DOODLE = "doodle";
    private static final String ARG_X = "x";
    private static final String ARG_Y = "y";
    private static final String ARG_WIDTH = "width";
    private static final String ARG_HEIGHT = "height";

    private TextView mTitleView;
    private Doodle mDoodle;
    private NetworkImageView mImageView;
    private SlidingUpPanelLayout mLayout;


    /**
     * Create a new instance of DetailFragment.
     *
     * @param doodle The title of the image
     * @param x      The horizontal position of the grid item in pixel
     * @param y      The vertical position of the grid item in pixel
     * @param width  The width of the grid item in pixel
     * @param height The height of the grid item in pixel
     * @return a new instance of DetailFragment
     */
    public static DoodleDetailsFragment newInstance(Doodle doodle,
                                                    int x, int y, int width, int height) {
        DoodleDetailsFragment fragment = new DoodleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOODLE, new Gson().toJson(doodle));
        args.putInt(ARG_X, x);
        args.putInt(ARG_Y, y);
        args.putInt(ARG_WIDTH, width);
        args.putInt(ARG_HEIGHT, height);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doodles_detail, null);
        int paddingTop = Utils.getToolbarHeight(getActivity());
        view.setPadding(0, paddingTop, 0, 0);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String json;
        if (savedInstanceState != null) {
            json = savedInstanceState.getString(ARG_DOODLE);
        } else {
            json = getArguments().getString(ARG_DOODLE);
        }
        Gson gson = new Gson();
        mDoodle = gson.fromJson(json, Doodle.class);
        getActivity().setTitle(mDoodle.title);

        installSlidingUpPanelLayout(view);
        bindDetail(view, mDoodle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTag());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.Search).setVisible(false);
    }

    private SlidingTabLayout mSlidingTabLayout;
    private void installSlidingUpPanelLayout(View view){
        mLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        view.findViewById(R.id.Go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.9f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    }
                }
            }
        });
        mLayout.setTouchEnabled(false);

        Bundle argsSearch = new Bundle();
        argsSearch.putString("q", mDoodle.query);
        ViewPager iViewPager = (ViewPager) view.findViewById(R.id.ViewPager);
        ArrayList<CustomFragmentPagerAdapter.TabInfo> tabInfos = new ArrayList<>();
        tabInfos.add(new CustomFragmentPagerAdapter.TabInfo("Google", GoogleView.class, argsSearch));
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(
                getChildFragmentManager(), getActivity(), tabInfos
        );
        iViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.SlidingTabs);
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

        mSlidingTabLayout.setViewPager(iViewPager);
    }


    private void bindDetail(View parent, final Doodle doodle) {
        mTitleView = (TextView) parent.findViewById(R.id.Title);
        TextView iNameText = (TextView) parent.findViewById(R.id.NameText);
        TextView mDateView = (TextView) parent.findViewById(R.id.Date);
        TextView iHoverText = (TextView) parent.findViewById(R.id.HoverText);
        TextView iTranslations = (TextView) parent.findViewById(R.id.Translations);
        mImageView = (NetworkImageView) parent.findViewById(R.id.ImageView);
        mImageView.setDefaultImageResId(R.drawable.GRAY_LIGHT);
        mImageView.setErrorImageResId(R.drawable.ic_google_birthday);

        mTitleView.setText(doodle.title);
        iNameText.setText(doodle.name);
        mImageView.setImageUrl(doodle.hires_url, VolleyLoader.getInstance().getImageLoader());
        mDateView.setText(doodle.getDate());
        iTranslations.setText(iTranslations.getText().toString() + doodle.getTranslations().size() + "种");

        Translations.Query query = doodle.getTranslations().getItem(0);
        String hover = TextUtils.isEmpty(query.hover_text) ? "" : query.hover_text;
        iHoverText.setText(hover
                + "\n#" + doodle.query + "#");

        parent.findViewById(R.id.Search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String q = Uri.encode(doodle.query);
//                WebViewActivity.launch(getActivity(), ApiClient.GOOGLE_SEARCH + q, doodle.query);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStackImmediate();
                return true;
        }
        return true;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                enter ? android.R.anim.fade_in : android.R.anim.fade_out);
        return animation;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_DOODLE, new Gson().toJson(mDoodle));
    }

    @Override
    public boolean onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        } else
            return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}