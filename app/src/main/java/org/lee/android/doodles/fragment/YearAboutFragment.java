package org.lee.android.doodles.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.lee.android.doodles.R;
import org.lee.android.test.FragmentLog;
import org.lee.android.util.Log;

/**
 * 指定年份的关于页面
 */
public class YearAboutFragment extends FragmentLog {

    public static YearAboutFragment newInstance(YearsFragment.Year year) {
        YearAboutFragment fragment = new YearAboutFragment();
        Bundle args = new Bundle();
        args.putString("year", new Gson().toJson(year));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_year_about, null, false);
    }

    private YearsFragment.Year mYear;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        String json = getArguments().getString("year");
        mYear = new Gson().fromJson(json, YearsFragment.Year.class);

        TextView titleText = (TextView) view.findViewById(R.id.Title);
        NetworkImageView doodleImageView = (NetworkImageView) view.findViewById(R.id.ImageView);
        titleText.setText(mYear.year + "年");
        doodleImageView.setDefaultImageResId(R.drawable.ic_google_birthday);

        Log.anchor(mYear.url);
//        String packageName = getActivity().getPackageName();
//        int drawable = getResources().getIdentifier(mYear.url, "drawable", packageName);
//        doodleImageView.setImageResource(drawable);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("data");
            Log.anchor(savedInstanceState.toString());
        }
    }

}