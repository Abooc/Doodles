package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.util.Log;

import java.io.Serializable;

/**
 */
public class CategorysFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static CategorysFragment newInstance() {
        CategorysFragment fragment = new CategorysFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onShowFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categorys, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GridView iGridView = (GridView) view.findViewById(R.id.GridView);
        iGridView.setOnItemClickListener(this);
        attachData(iGridView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            ((MainActivity) getActivity()).onShowFragment(this);
        }
    }

    private void attachData(final GridView iGridView) {
        Year[] years = getYears(getActivity());
        YearAdapter adapter = new YearAdapter(
                getActivity(), 0, years);
        iGridView.setAdapter(adapter);
    }

    private class YearAdapter extends ArrayAdapter {

        private final Resources resources;
        private final String packageName;

        public YearAdapter(Context context, int resource, Object[] objects) {
            super(context, resource, objects);
            resources = context.getResources();
            packageName = context.getPackageName();

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.year_item, null);
            }
            Year year = (Year) getItem(position);
            TextView titleText = (TextView) convertView.findViewById(R.id.Title);
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.ImageView);

            titleText.setText(year.name);
//            int drawable = resources.getIdentifier(year.url, "drawable", packageName);
//            imageView.setImageResource(drawable);
            return convertView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnYearChangedListener != null) {
            ArrayAdapter<Year> arrayAdapter = (ArrayAdapter<Year>) parent.getAdapter();
            mOnYearChangedListener.onYearChanged(arrayAdapter.getItem(position));
        }
    }

    public interface OnYearChangedListener {
        public void onYearChanged(Year newYear);
    }

    private OnYearChangedListener mOnYearChangedListener;

    public void setOnYearChangedListener(OnYearChangedListener listener) {
        mOnYearChangedListener = listener;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.anchor();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.anchor();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.anchor();
    }

    public static class Year implements Serializable{
        public String name;
        public String url;

        public Year(String name, String drawable) {
            this.name = name;
            this.url = drawable;
        }

        @Override
        public String toString() {
            return "name:" + name + ", url:" + url;
        }
    }

    private static Year[] getYears(Context context) {
        final String[] yearNames = context.getResources().getStringArray(R.array.yearNames);
        final String[] imageIds = context.getResources().getStringArray(R.array.yearDrawables);

        Year[] years = new Year[yearNames.length];
        for (int i = 0; i < yearNames.length; i++) {
            Year year = new Year(yearNames[i], imageIds[i]);
            years[i] = year;
        }
        return years;
    }

}