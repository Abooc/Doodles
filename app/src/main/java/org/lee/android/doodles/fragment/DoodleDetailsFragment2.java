package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.lee.android.doodles.ApiClient;
import org.lee.android.doodles.LifecycleFragment;
import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.WebViewActivity;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.bean.Translations;
import org.lee.android.doodles.volley.VolleyLoader;
import org.lee.android.util.Log;

/**
 * Doodle详情页面
 */
public class DoodleDetailsFragment2 extends LifecycleFragment implements Animation.AnimationListener {

    private static final String ARG_DOODLE = "doodle";
    private static final String ARG_X = "x";
    private static final String ARG_Y = "y";
    private static final String ARG_WIDTH = "width";
    private static final String ARG_HEIGHT = "height";

    private TextView mTitleView;
    private Doodle mDoodle;
    private NetworkImageView mImageView;

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
    public static DoodleDetailsFragment2 newInstance(Doodle doodle,
                                                    int x, int y, int width, int height) {
        DoodleDetailsFragment2 fragment = new DoodleDetailsFragment2();
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
        FrameLayout rootView = new FrameLayout(getActivity());
        int paddingTop = Utils.getToolbarHeight(getActivity());
        rootView.setPadding(0, paddingTop, 0, 0);
        return rootView;
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

        addOriginView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getTag());
    }

    private void addOriginView(View view) {
        FrameLayout root = (FrameLayout) view;
        Context context = view.getContext();
        assert context != null;
        // This is how the fragment looks at first. Since the transition is one-way, we don't need to make
        // this a Scene.
        View doodleItemView = LayoutInflater.from(context).inflate(R.layout.fragment_doodles_list_item, root, false);
        bind(doodleItemView, mDoodle);
        // We adjust the position of the initial image with LayoutParams using the values supplied
        // as the fragment arguments.
        Bundle args = getArguments();
        FrameLayout.LayoutParams params = null;
        if (args != null) {
            params = new FrameLayout.LayoutParams(
                    args.getInt(ARG_WIDTH),
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            params.topMargin = args.getInt(ARG_Y);
            params.leftMargin = args.getInt(ARG_X);
        }
        root.addView(doodleItemView, params);
    }

    /**
     * Bind the views inside of parent with the fragment arguments.
     *
     * @param parent The parent of views to bind.
     */
    private void bind(View parent, Doodle doodle) {
        mTitleView = (TextView) parent.findViewById(R.id.Title);
        mImageView = (NetworkImageView) parent.findViewById(R.id.ImageView);
        mImageView.setErrorImageResId(R.drawable.ic_google_birthday);

        mTitleView.setText(doodle.title);
        mImageView.setImageUrl(doodle.hires_url, VolleyLoader.getInstance().getImageLoader());
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
                String q = Uri.encode(doodle.query);
                WebViewActivity.launch(getActivity(), ApiClient.GOOGLE_SEARCH + q, doodle.query);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.anchor();
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
        if (animation != null && enter) {
            animation.setAnimationListener(this);
        }
        return animation;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // This method is called at the end of the animation for the fragment transaction.
        // There is nothing we need to do in this sample.
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // This method is called at the end of the animation for the fragment transaction,
        // which is perfect time to start our Transition.
        Log.anchor("Fragment animation ended. Starting a Transition.");
        final Scene scene = Scene.getSceneForLayout((ViewGroup) getView(),
                R.layout.fragment_doodles_detail, getActivity());
        TransitionManager.go(scene);
        // Note that we need to bind views with data after we call TransitionManager.go().
        bindDetail(scene.getSceneRoot(), mDoodle);
//        bind(scene.getSceneRoot().findViewById(R.id.CardView), mDoodle);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // This method is never called in this sample because the animation doesn't repeat.
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ARG_DOODLE, new Gson().toJson(mDoodle));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}