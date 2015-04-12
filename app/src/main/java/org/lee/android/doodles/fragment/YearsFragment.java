package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.lee.android.doodles.R;
import org.lee.android.doodles.Utils;
import org.lee.android.doodles.activity.MainActivity;
import org.lee.android.doodles.fragment.RecyclerItemViewHolder.OnRecyclerItemClickListener;
import org.lee.android.util.Log;

import java.io.Serializable;

/**
 * 年份页面
 */
public class YearsFragment extends Fragment implements OnRecyclerItemClickListener {

    public static YearsFragment newInstance() {
        YearsFragment fragment = new YearsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mActivity;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private Year[] mYears;


    /**
     * Fragment运行状态监听
     */
    private FragmentRunningListener mFrunningListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        MainActivity mainActivity = (MainActivity) activity;
        mFrunningListener = mainActivity;
        mOnScrollListener = mainActivity.getHidingScrollListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_doodles_list, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        mYears = loadYears(mActivity);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        int toolbarHeight = Utils.getToolbarHeight(getActivity());
//        int slidingTabLayoutHeight = getActivity().findViewById(R.id.SlidingTabs).getHeight();
        recyclerView.setPadding(recyclerView.getPaddingLeft(), toolbarHeight + toolbarHeight,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        YearAdapter recyclerAdapter = new YearAdapter(mYears, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFrunningListener.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFrunningListener.onPause(this);
    }

    private static class YearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mNameText;
        final ImageView mDoodleView;
        private OnRecyclerItemClickListener listener;

        public YearViewHolder(View itemView, TextView nameText, ImageView doodleView, OnRecyclerItemClickListener listener) {
            super(itemView);
            mNameText = nameText;
            mDoodleView = doodleView;

            itemView.setOnClickListener(this);
            this.listener = listener;
        }


        public void attachData(Year year) {
            mNameText.setText(year.name + "年");
//            int drawable = resources.getIdentifier(year.url, "drawable", packageName);
//            imageView.setImageResource(drawable);
        }

        public static YearViewHolder newInstance(View view, OnRecyclerItemClickListener listener) {
            TextView nameText = (TextView) view.findViewById(R.id.Name);
            ImageView doodleView = (ImageView) view.findViewById(R.id.ImageView);
            return new YearViewHolder(view, nameText, doodleView, listener);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }


    private class YearAdapter extends RecyclerView.Adapter<YearViewHolder> {

        private Year[] mYears;
        private OnRecyclerItemClickListener mListener;

        public YearAdapter(Year[] years, OnRecyclerItemClickListener listener) {
            mYears = years;
            mListener = listener;
        }

        @Override
        public YearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.year_item, parent, false);
            return YearViewHolder.newInstance(view, mListener);
        }

        @Override
        public void onBindViewHolder(YearViewHolder viewHolder, int position) {
            Year year = mYears[position];
            viewHolder.attachData(year);
        }

        @Override
        public int getItemCount() {
            return mYears == null ? 0 : mYears.length;
        }

    }

    @Override
    public void onItemClick(View parent, int position) {
        if (mOnYearChangedListener != null) {
            mOnYearChangedListener.onYearChanged(mYears[position]);
        }
    }

    public interface OnYearChangedListener {
        void onYearChanged(Year newYear);
    }

    private OnYearChangedListener mOnYearChangedListener;

    public void setOnYearChangedListener(OnYearChangedListener listener) {
        mOnYearChangedListener = listener;
    }

    public static class Year implements Serializable {
        /**
         * 年份，如2015
         */
        public String name;
        /**
         * 这一年Doodle代表作
         */
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

    private static Year[] loadYears(Context context) {
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