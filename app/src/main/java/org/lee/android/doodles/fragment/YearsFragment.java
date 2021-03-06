package org.lee.android.doodles.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import org.lee.android.doodles.fragment.DoodleRecyclerItemHolder.OnRecyclerItemClickListener;

import java.io.Serializable;

/**
 * 年份页面
 */
public class YearsFragment extends Fragment implements OnRecyclerItemClickListener {


    public interface OnYearChangedListener {
        void onYearChanged(Year newYear);
    }

    public static YearsFragment newInstance() {
        YearsFragment fragment = new YearsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private Activity mActivity;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private OnYearChangedListener mOnYearChangedListener;
    private Year[] mYears;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        MainActivity mainActivity = (MainActivity) activity;
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
        recyclerView.setVisibility(View.VISIBLE);
        int toolbarHeight = Utils.getToolbarHeight(getActivity());
//        int slidingTabLayoutHeight = getActivity().findViewById(R.id.SlidingTabs).getHeight();
        recyclerView.setPadding(recyclerView.getPaddingLeft(), toolbarHeight + toolbarHeight,
                recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        YearAdapter recyclerAdapter = new YearAdapter(mYears, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(mOnScrollListener);
    }

    /**
     * 注册年份切换监听
     * @param listener
     */
    public void setOnYearChangedListener(OnYearChangedListener listener) {
        mOnYearChangedListener = listener;
    }

    /**
     * 年份切换事件
     */
    @Override
    public void onItemClick(View parent, int position) {
        Year year = mYears[position];
        if (mOnYearChangedListener != null) {
            mOnYearChangedListener.onYearChanged(year);
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.tabcontent,
                        DoodleArchivePagerFragment.newInstance(year, false),
                        year.year + "年"
                )
                .addToBackStack("newYear")
                .setBreadCrumbTitle(year.year + "年")
                .commit();
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
            mNameText.setText(year.year + "年");
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
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_year_item, parent, false);
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


    public static class Year implements Serializable {
        /**
         * 年份值，如2015
         */
        public int year;
        /**
         * 这一年Doodle代表作品
         */
        public String url;

        public Year(int year, String drawable) {
            this.year = year;
            this.url = drawable;
        }

        @Override
        public String toString() {
            return "year:" + year + ", url:" + url;
        }
    }

    private static Year[] loadYears(Context context) {
        final int[] yearValues = context.getResources().getIntArray(R.array.yearNames);
        final String[] imageIds = context.getResources().getStringArray(R.array.yearDrawables);

        Year[] years = new Year[yearValues.length];
        for (int i = 0; i < yearValues.length; i++) {
            Year year = new Year(yearValues[i], imageIds[i]);
            years[i] = year;
        }
        return years;
    }

}