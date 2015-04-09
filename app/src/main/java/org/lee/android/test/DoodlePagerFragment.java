package org.lee.android.test;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.lee.android.doodles.R;
import org.lee.android.doodles.bean.Doodle;
import org.lee.android.doodles.volley.VolleyLoader;

/**
 * 测试用
 */
public class DoodlePagerFragment extends Fragment {

    private NetworkImageView mImageView;
    private Doodle mDoodle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doodles_list_item, container, false);
        mImageView = (NetworkImageView) rootView.findViewById(R.id.ImageView);
        mImageView.setImageResource(R.drawable.ic_google_birthday);
        mImageView.setDefaultImageResId(R.drawable.ic_doodle_error);
        mImageView.setErrorImageResId(R.drawable.ic_google_birthday);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String json;
        if(savedInstanceState != null){
            json = savedInstanceState.getString("doodle");
        }else{
            json = getArguments().getString("doodle");
        }
        Gson gson = new Gson();
        mDoodle = gson.fromJson(json, Doodle.class);
        mImageView.setImageUrl(mDoodle.hires_url, VolleyLoader.getInstance().getImageLoader());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("doodle", new Gson().toJson(mDoodle));
    }
}